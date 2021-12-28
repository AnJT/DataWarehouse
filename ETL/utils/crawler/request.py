import time
import requests
from bs4 import BeautifulSoup
from amazoncaptcha import AmazonCaptcha
from batch import get_random_headers

base_url = 'https://www.amazon.com/dp/'
review_base_url = 'https://www.amazon.com/product-reviews/'


def get_proxy():
    return requests.get("http://81.69.225.235:5010/get/").json()


def delete_proxy(proxy):
    requests.get("http://81.69.225.235:5010/delete/?proxy={}".format(proxy))


# 爬虫-电影主页
def get_home(asin: str, headers):
    retry_proxy_count = 6
    while retry_proxy_count > 0:
        proxy = get_proxy().get("proxy")
        headers = get_random_headers()
        retry_count = 2
        session = requests.session()
        while retry_count > 0:
            try:
                print(retry_count)
                html = session.get(url=base_url + asin, headers=headers, proxies={"http": "http://{}".format(proxy)})
                result = html.text.encode('gbk', 'ignore').decode('gbk')
                soup = BeautifulSoup(result, 'lxml')
                movie_title = str(soup.select('title')[0].getText())
                if movie_title.strip() == 'Page Not Found':
                    with open('./data/invalid_home_asin.txt', 'a') as f:
                        f.write(asin.strip() + '\n')
                    session.close()
                    return None, asin
                if (movie_title != 'Robot Check') and (movie_title != 'Sorry! Something went wrong!') and (movie_title != 'Amazon.com'):
                    for a in soup.findAll(name='a', attrs={"class" : "a-link-normal a-color-tertiary"}):
                        if str(a.text).strip() == 'TV':
                            with open('./data/tv.txt', 'a') as f:
                                f.write(asin.strip() + '\n')
                            return None, asin
                    detail = soup.find('div', id='detailBullets_feature_div')
                    if detail == None:
                        print("detail")
                        retry_count -= 2
                        continue
                    session.close()
                    return html, asin
                else:
                    print(movie_title)
                    try:
                        if movie_title == 'Sorry! Something went wrong!':
                            exit()
                        else:
                            session = verify(soup, session, proxy, headers)
                    except Exception as e:
                        print(e)
                    retry_count -= 1
            except Exception as e:
                print(e)
                retry_count -= 1
        retry_proxy_count -= 1
    with open('./data/faild_home_asin.txt', 'a') as f:
        f.write(asin.strip() + '\n')
    return None, asin


# 爬虫-电影评论
def get_review(asin: str, headers):
    retry_proxy_count = 3
    while retry_proxy_count > 0:
        proxy = get_proxy().get("proxy")
        retry_count = 5
        session = requests.session()
        while retry_count > 0:
            try:
                html = session.get(url=review_base_url + asin, headers=headers, proxies={"http": "http://{}".format(proxy)})
                result = html.text.encode('gbk', 'ignore').decode('gbk')
                soup = BeautifulSoup(result, 'lxml')
                movie_title = str(soup.select('title')[0].getText())
                if movie_title.strip() == 'Page Not Found':
                    with open('./data/invalid_review_asin.txt', 'a') as f:
                        f.write(asin.strip() + '\n')
                    session.close()
                    return None
                if (movie_title != 'Robot Check') and (movie_title != 'Sorry! Something went wrong!') and (movie_title != 'Amazon.com'):
                    session.close()
                    return html
                else:
                    try:
                        if movie_title == 'Sorry! Something went wrong!':
                            time.sleep(0.5)
                        else:
                            session = verify(soup, session, proxy, headers)
                    except Exception as e:
                        print(e)
                retry_count -= 1
            except Exception as e:
                print(e)
                retry_count -= 1
        retry_proxy_count -= 1
        session.close()
    with open('./data/failed_review_asin.txt', 'a') as f:
        f.write(asin.strip() + '\n')
    return None

# 识别验证码
def verify(soup, session, proxy, headers):
    captcha = soup.find(name="div", attrs={"class" : "a-row a-text-center"})
    captcha_link = captcha.img['src']
    captcha =  AmazonCaptcha.fromlink(captcha_link)
    solution = captcha.solve()
    amaz_code = soup.find(name="input", attrs={"name" : "amzn"})
    amaz_code = amaz_code['value']
    amaz_r_code = soup.find(name="input", attrs={"name" : "amzn-r"})
    amaz_r_code = amaz_r_code['value']
    captcha_url = f'https://www.amazon.com/errors/validateCaptcha?amzn={amaz_code}&amzn-r={amaz_r_code}&field-keywords={solution}'
    print(captcha_url)
    html = session.get(url=captcha_url, headers=headers, proxies={"http": "http://{}".format(proxy)})
    return session