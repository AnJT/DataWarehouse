import time
import requests

base_url = 'https://www.amazon.com/dp/'
review_base_url = 'https://www.amazon.com/product-reviews/'


def get_proxy():
    return requests.get("http://81.69.225.235:5010/get/").json()


def delete_proxy(proxy):
    requests.get("http://81.69.225.235:5010/delete/?proxy={}".format(proxy))


def get_html(asin: str, headers):
    retry_count = 5
    proxy = get_proxy().get("proxy")
    while retry_count > 0:
        try:
            html = requests.get(url=base_url + asin, headers=headers, proxies={"http": "http://{}".format(proxy)})
            review_html = requests.get(url=review_base_url + asin, headers=headers, proxies={"http": "http://{}".format(proxy)})
            return html, review_html
        except Exception:
            retry_count -= 1
            time.sleep(0.5)
    delete_proxy(proxy)
    return None, None
