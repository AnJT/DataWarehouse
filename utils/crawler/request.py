import time
import requests

base_url = 'https://www.amazon.com/dp/'

headers = {
    "User-Agent": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) "
                  "Chrome/83.0.4103.116 Safari/537.36",
    "Connection": "closer",
    "Upgrade-Insecure-Requests": "1",
    "Cache-Control": "max-age=0"
}


def get_proxy():
    return requests.get("http://81.69.225.235:5010/get/").json()


def delete_proxy(proxy):
    requests.get("http://81.69.225.235:5010/delete/?proxy={}".format(proxy))


def get_html(asin: str):
    retry_count = 5
    proxy = get_proxy().get("proxy")
    while retry_count > 0:
        try:
            url = base_url + asin
            html = requests.get(url=url, headers=headers, proxies={"http": "http://{}".format(proxy)})
            return html
        except Exception:
            retry_count -= 1
            time.sleep(0.5)
    delete_proxy(proxy)
    return None
