from request import *
from multiprocessing import Pool
import random
import json
import concurrent.futures
from parser_html import parse_home, parse_review

ua_file = open('../../lib/fake_useragent_0.1.11.json')
asin_file = open('../../data/asin_no_duplicate.txt')
# ua_file = open('lib/fake_useragent_0.1.11.json')
# asin_file = open('data/asin_no_duplicate.txt')
ua_json = json.load(ua_file)


def get_random_headers():
    browser_list = list(ua_json['browsers'].keys())
    ua = random.choice(ua_json['browsers'][random.choice(browser_list)])
    
    headers = {
        "User-Agent": ua,
        # "User-Agent": 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.45 Safari/537.36',
        "Connection": "keep-alive",
        "Accept-Language": "en-US,en;q=0.9,zh-CN;q=0.8,zh;q=0.7,zh-TW;q=0.6",  # necessary
        "Accept-Encoding": "gzip, deflate, br",
        "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
        'cache-control': 'max-age=0'
    }

    return headers


def process(asin: str):
    headers = get_random_headers()
    # html = get_home(asin.strip(), headers)
    html = get_review(asin.strip(), headers)
    # TODO: add parser operation
    # pass
    try:
        # detail = parse_home(html)
        reviews = parse_review(html)
        with open(f'../../data/review/{asin.strip()}.json','w+', encoding='utf-8') as f:
            json.dump(reviews,f,ensure_ascii=False, indent=4, separators=(',', ':'))
        print('access ', asin)
    except Exception as e:
        print(e)
        print(asin)
        with open('hh.html', 'w+') as f:
            f.write(html.text.encode('gbk', 'ignore').decode('gbk'))
        raise e
   
    # with open(f'../../data/detail/{asin.strip()}.json', 'w+') as f:
    #     res = {'detail': None, 'reviews': reviews}
    #     json.dump(res, f, ensure_ascii=False, indent=4, separators=(',', ':'))



if __name__ == '__main__':
    asin_list = asin_file.readlines()
    # with Pool() as p:
    #     p.map(process, asin_list)
    with concurrent.futures.ProcessPoolExecutor() as executor:
        for res in executor.map(process, asin_list):
            pass
    ua_file.close()
    asin_file.close()
