import time
import requests
from bs4 import BeautifulSoup
from amazoncaptcha import AmazonCaptcha

base_url = 'https://www.amazon.com/dp/'
review_base_url = 'https://www.amazon.com/product-reviews/'


def get_proxy():
    return requests.get("http://81.69.225.235:5010/get/").json()


def delete_proxy(proxy):
    requests.get("http://81.69.225.235:5010/delete/?proxy={}".format(proxy))


def get_home(asin: str, headers):
    retry_count = 5
    retry_proxy_count = 3
    while retry_proxy_count > 0:
        proxy = get_proxy().get("proxy")
        while retry_count > 0:
            try:
                html = requests.get(url=base_url + asin, headers=headers, proxies={"http": "http://{}".format(proxy)})
                result = html.text.encode('gbk', 'ignore').decode('gbk')
                soup = BeautifulSoup(result, 'lxml')
                movie_title = str(soup.select('title')[0].getText())
                if movie_title.strip() == 'Page Not Found':
                    with open('../../data/invalid_asin.txt', 'a') as f:
                        f.write(asin.strip() + '\n')
                    return None
                if (movie_title != 'Robot Check') and (movie_title != 'Sorry! Something went wrong!') and (movie_title != 'Amazon.com'):
                    detail = soup.find('div', id='detailBullets_feature_div')
                    if detail == None:
                        continue
                    return html
            except Exception:
                retry_count -= 1
                time.sleep(0.5)
        delete_proxy(proxy)
        retry_proxy_count -= 1
    with open('../../data/faild_home_asin.txt', 'a') as f:
        f.write(asin.strip() + '\n')
    return None


def get_review(asin: str, headers):
    retry_proxy_count = 3
    while retry_proxy_count > 0:
        proxy = get_proxy().get("proxy")
        retry_count = 5
        session = requests.session()
        while retry_count > 0:
            html = session.get(url=review_base_url + asin, headers=headers, proxies={"http": "http://{}".format(proxy)})
            result = html.text.encode('gbk', 'ignore').decode('gbk')
            soup = BeautifulSoup(result, 'lxml')
            movie_title = str(soup.select('title')[0].getText())
            if movie_title.strip() == 'Page Not Found':
                with open('../../data/invalid_asin.txt', 'a') as f:
                    f.write(asin.strip() + '\n')
                return None
            if (movie_title != 'Robot Check') and (movie_title != 'Sorry! Something went wrong!') and (movie_title != 'Amazon.com'):
                return html
            else:
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
                with open('hh.html', 'w+') as f:
                    f.write(html.text.encode('gbk', 'ignore').decode('gbk'))
                time.sleep(0.5)
                pass
            retry_count -= 1
        retry_proxy_count -= 1
        # delete_proxy(proxy)
    with open('../../data/failed_review_asin.txt', 'a') as f:
        f.write(asin.strip() + '\n')
    return None

# def get_html(asin: str, headers):
#     retry_count = 5
#     retry_proxy_count = 3
#     while retry_proxy_count > 0:
#         proxy = get_proxy().get("proxy")
#         while retry_count > 0:
#             try:
#                 html = requests.get(url=base_url + asin, headers=headers, proxies={"http": "http://{}".format(proxy)})
#                 result = html.text.encode('gbk', 'ignore').decode('gbk')
#                 soup = BeautifulSoup(result, 'lxml')
#                 movie_title = str(soup.select('title')[0].getText())
#                 if movie_title.strip() == 'Page Not Found':
#                     with open('../../data/invalid_asin.txt', 'a') as f:
#                         f.write(asin.strip() + '\n')
#                     return None, None
#                 if (movie_title != 'Robot Check') and (movie_title != 'Sorry! Something went wrong!') and (movie_title != 'Amazon.com'):
#                     review_html = requests.get(url=review_base_url + asin, headers=headers, proxies={"http": "http://{}".format(proxy)})
#                     return html, review_html
#             except Exception:
#                 retry_count -= 1
#                 time.sleep(0.5)
#         # delete_proxy(proxy)
#         retry_proxy_count -= 1
#     return None, None