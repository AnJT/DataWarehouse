from time import daylight
from request import *
from multiprocessing import Pool
import random
import json
import os
from tqdm import tqdm
import concurrent.futures
from parser_html import parse_home, parse_review

ua_file = open('../../lib/fake_useragent_0.1.11.json')
# asin_file = open('../../data/asin_no_duplicate.txt')
filed_asin_file = open('../../data/faild_home_asin2.txt', 'r+')
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
    html, asin = get_home(asin.strip(), headers)
    # html = get_review(asin.strip(), headers)
    # TODO: add parser operation
    # pass
    try:
        detail = parse_home(html, asin)
        # reviews = parse_review(html)
        if detail == None:
            return
        with open(f'../../data/detail/{asin.strip()}.json','w+', encoding='utf-8') as f:
            json.dump(detail,f,ensure_ascii=False, indent=4, separators=(',', ':'))
        print('access ', asin)
    except Exception as e:
        print("-----------------------------------------------------------")
        print(e)
        print(asin)
        with open('hh.html', 'w+') as f:
            f.write(html.text.encode('gbk', 'ignore').decode('gbk'))
        raise e

if __name__ == '__main__':
    asin_list = filed_asin_file.readlines()
    asin_list = [x.strip() for x in asin_list]
    done_list = open('../../data/invalid_home_asin.txt')
    done_list = [x.strip() for x in done_list]
    asin_list = list(set(asin_list) - set(done_list))
    done_list = open('../../data/tv.txt')
    done_list = [x.strip() for x in done_list]
    asin_list = list(set(asin_list) - set(done_list))
    print(len(asin_list))
    # exit()
    with concurrent.futures.ProcessPoolExecutor() as executor:
        for res in executor.map(process, asin_list):
            pass
    ua_file.close()
    filed_asin_file.close()
    # i=0
    # for done in tqdm(os.listdir('../../data/review')):
    #     flag = False
    #     path = os.path.join('../../data/review', done)
    #     with open(path, encoding='utf-8') as f:
    #         data=json.load(f)
    #         if data==None:
    #             i+=1
    #             flag = True
    #             print(i)
    #     if flag:
    #         os.remove(path)
    # print(i)