from request import *
from multiprocessing import Pool
import random
import json


ua_file = open('lib/fake_useragent_0.1.11.json')
asin_file = open('data/asin_no_duplicate.txt')
ua_json = json.load(ua_file)


def get_random_headers():
    browser_list = list(ua_json['browsers'].keys())
    ua = random.choice(ua_json['browsers'][random.choice(browser_list)])
    headers = {
        "User-Agent": ua,
        "Connection": "closer",
        "Upgrade-Insecure-Requests": "1",
        "Cache-Control": "max-age=0"
    }
    return headers


def process(asin: str):
    # headers = get_random_headers()
    # html = get_html(asin.strip(), headers)
    # TODO: add parser operation
    pass


if __name__ == '__main__':
    asin_list = asin_file.readlines()
    with Pool(5) as p:
        p.map(process, asin_list)
    ua_file.close()
    asin_file.close()
