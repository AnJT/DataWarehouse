## 数据预处理

在进行数据爬取之前，需要对数据源进行预处理：提取出用户评论数据中的60175个 asin。

具体做法为逐行读取文件中的数据，以 asin 为关键词进行正则匹配，正则表达式为 `asin": "(.*?)",`， 进而进行存储以期有效查询字段的持久化。 

## URL的拼接

要获取一个网页的内容并进行处理，首先需要得到要爬取网页的 URL ，经过归纳总结可得  Amazon 网站中的 Movie 页面及评论页面全部可以通过以下方式构造 

```Python
base_url = 'https://www.amazon.com/dp/'
review_base_url = 'https://www.amazon.com/product-reviews/'
url = base_url + asin
review_url = review_base_url + asin
```

## 反爬优化

### 伪造请求头

对于没有请求头的 httpGet 请求，会直接被 Amazon 的服务器 拒绝访问，所以还需要对请求头进行构造，我们使用了fake_useragent 库来动态伪造请求头。

```Python
from fake_useragent import UserAgent
ua = UserAgent()
headers = {"user-agent": UserAgent().random}
```

### IP代理池

使用 [proxy pool](https://github.com/jhao104/proxy_pool) 开源库以及 Redis 数据库来搭建免费 ip 池，爬虫时动态获取 ip 来应对 Amazon 的反爬机制。

```Python
def get_proxy():
    return requests.get("http://81.69.225.235:5010/get/").json()
proxy = get_proxy().get("proxy")
html = session.get(url=base_url + asin, headers=headers, proxies={"http": "http://{}".format(proxy)})
```

### 验证码处理

使用 [amazoncaptcha](https://github.com/a-maliarov/amazoncaptcha) 开源库识别 Amazon 验证码，伪造识别验证码请求。

```Python
def verify(soup, session, proxy, headers):
    from amazoncaptcha import AmazonCaptcha
    captcha = soup.find(name="div", attrs={"class" : "a-row a-text-center"})
    captcha =  AmazonCaptcha.fromlink(captcha_link)
    solution = captcha.solve()
    amaz_code = soup.find(name="input", attrs={"name" : "amzn"})['value']
    amaz_r_code = soup.find(name="input", attrs={"name" : "amzn-r"})['value']
    captcha_url = f'https://www.amazon.com/errors/validateCaptcha?amzn={amaz_code}&amzn-r={amaz_r_code}&field-keywords={solution}'
    session.get(url=captcha_url, headers=headers, proxies={"http": "http://{}".format(proxy)})
    return session
```

# 数据预处理

## 数据提取

使用 BeautifulSoup 解析 html 并根据 html 相关属性提取信息，例如通过 id 为 detailBullets_feature_div 的 div 提取电影的详细信息，通过 class 为 a-link-normal a-color-tertiary 的 a 标签提取电影的 label等。

## 数据清洗

### TV 过滤

通过提取出的电影 label 进行过滤，当 label 中含有 TV 字段时，判断为 TV 类，将 asin 保存至 txt 持久化。

### 并查集去重

 通过对抓取后的网页数据进行分析，可以发现在：Amazon.com/dp/asin 对应的网页中，位于<div id="MediaMatrix"></div>中的链接地址为同一个电影的不同版本。 因此可以利用 BeautifulSoup 库对爬取的 HTML 网页文件进行解析，筛选出位于上述 div 块中所有的相关 asin。  

 由于指向同一个电影的不同产品网页之间不一定互相全连通。（即：ID 为 01， 02，03 指向同一个电影，其中 01 页面可以获得 02 和 03 的 ID，但 03 的页面不 一定能获取到 02 的 ID）因此不能通过简单去重实现，所以我们选用了并查集这 一数据结构对去重进行维护。 

### 冲突合并

根据并查集的处理结果，对同一种电影按如下规则合并：

- 电影标题选取为并查集处理结果中第一个电影的标题；
- 电影导演信息取并集；
- 电影演员信息取并集；
- 电影评分选取为并查集处理结果中第一个电影的评分；
- 电影发布时间选取为并查集处理结果中第一个电影的发布时间，若为空则选取第二个，以此类推；
- 电影评分选取为并查集处理结果中第一个电影的的评分，若为空则选取第二个，以此类推；
- 电影风格信息取并集。