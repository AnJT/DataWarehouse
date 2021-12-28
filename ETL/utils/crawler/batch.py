from request import *
import random
import json
import concurrent.futures
import os
from parser_html import parse_home, parse_review, parse_home_get_label, parse_home_get_neighbors

ua_file = open('./lib/fake_useragent_0.1.11.json')
filed_asin_file = open('./data/faild_home_asin.txt', 'r+')
ua_json = json.load(ua_file)


def get_random_headers():
    browser_list = list(ua_json['browsers'].keys())
    ua = random.choice(ua_json['browsers'][random.choice(browser_list)])
    
    headers = {
        "User-Agent": ua,
        "Connection": "keep-alive",
        "Accept-Language": "en-US,en;q=0.9,zh-CN;q=0.8,zh;q=0.7,zh-TW;q=0.6",  # necessary
        "Accept-Encoding": "gzip, deflate, br",
        "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
        'cache-control': 'max-age=0'
    }
    return headers


def process(asin: str):
    headers = get_random_headers()
    html_home, asin = get_home(asin.strip(), headers)
    html_review = get_review(asin.strip(), headers)
    try:
        neighbors = parse_home_get_neighbors(html_home, asin)
        labels = parse_home_get_label(html_home, asin)
        detail = parse_home(html_home, asin)
        reviews = parse_review(html_review)
        if detail == None:
            return
        with open(f'./data/neighbors/{asin.strip()}.json','w+', encoding='utf-8') as f:
            json.dump(neighbors, f, ensure_ascii=False, indent=4, separators=(',', ':'))
        with open(f'./data/label/{asin.strip()}.json','w+', encoding='utf-8') as f:
            json.dump(labels, f, ensure_ascii=False, indent=4, separators=(',', ':'))
        with open(f'./data/detail/{asin.strip()}.json','w+', encoding='utf-8') as f:
            json.dump(detail, f, ensure_ascii=False, indent=4, separators=(',', ':'))
        with open(f'./data/review/{asin.strip()}.json','w+', encoding='utf-8') as f:
            json.dump(reviews, f, ensure_ascii=False, indent=4, separators=(',', ':'))
        print('access ', asin)
    except Exception as e:
        print("-----------------------------------------------------------")
        print(e)
        print(asin)
        raise e

def run():
    if not os.path.exists('./data/neighbors'):
        os.mkdir('./data/neighbors')
    if not os.path.exists('./data/label'):
        os.mkdir('./data/label')
    if not os.path.exists('./data/detail'):
        os.mkdir('./data/detail')
    if not os.path.exists('./data/review'):
        os.mkdir('./data/review')
    asin_list = filed_asin_file.readlines()
    print(len(asin_list))
    with concurrent.futures.ProcessPoolExecutor() as executor:
        for res in executor.map(process, asin_list):
            pass
    ua_file.close()
    filed_asin_file.close()

if __name__ == '__main__':
    run()