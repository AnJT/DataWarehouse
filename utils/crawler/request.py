import time
import requests
from bs4 import BeautifulSoup

base_url = 'https://www.amazon.com/dp/'
review_base_url = 'https://www.amazon.com/product-reviews/'


def get_proxy():
    return requests.get("http://81.69.225.235:5010/get/").json()


def delete_proxy(proxy):
    requests.get("http://81.69.225.235:5010/delete/?proxy={}".format(proxy))


def get_html(asin: str, headers):
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
                if (movie_title != 'Robot Check') and (movie_title != 'Sorry! Something went wrong!') and (movie_title != 'Amazon.com'):
                    review_html = requests.get(url=review_base_url + asin, headers=headers, proxies={"http": "http://{}".format(proxy)})
                    return html, review_html
            except Exception:
                retry_count -= 1
                time.sleep(0.5)
        delete_proxy(proxy)
        retry_proxy_count -= 1
    return None, None
