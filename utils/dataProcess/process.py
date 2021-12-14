from ntpath import join
import pandas as pd
import numpy as np
import os
import json
from tqdm import tqdm
import calendar
import re

class Process:
    def __init__(self,
        detail_json_path = '../../data/detail',
        review_json_path = '../../data/review',
        processed_detail_save_path = '../../data/processed_data/asin_detail.csv',
        processed_review_save_path = '../../data/processed_data/asin_review.csv',
    ) -> None:
        self.detail_json_path = detail_json_path
        self.review_json_path = review_json_path
        self.processed_detail_save_path = processed_detail_save_path
        self.processed_review_save_path = processed_review_save_path

    def process_detail(self):
        '''处理电影属性'''
        detail_arr = os.listdir(self.detail_json_path)
        asin_detail = []
        for detail in tqdm(detail_arr):
            detail_path = os.path.join(self.detail_json_path, detail)
            with open(detail_path, encoding='utf-8') as f:
                detail_data = json.load(f)
                asin = detail.split('.')[0]
                asin_title = self.get_title(detail_data)
                asin_score = self.get_score(detail_data)
                asin_director = self.get_director(detail_data)
                asin_actor = self.get_actor(detail_data)
                asin_release_data = self.get_release_data(detail_data)
                asin_label = self.get_label(asin)
            asin_detail.append([asin, asin_title, asin_score, asin_director, asin_actor, asin_release_data, asin_label])
        df = pd.DataFrame(asin_detail, columns=['asin', 'title', 'score', 'director', 'actor', 'release_data', 'label'], dtype='str')
        df.to_csv(self.processed_detail_save_path, index=False)
    
    def get_label(self, asin):
        with open(os.path.join('../../data/label', asin + '.json'), encoding='utf-8') as f:
            labels = json.load(f)
            labels = labels['label']
        if len(labels) == 0:
            return None
        return ','.join(labels)

    def get_actor(self, detail_data):
        actor = []
        for key, value in detail_data['bylineInfo'].items():
            if 'actor' in key.lower():
                actor.extend(value.split(','))
        for key, value in detail_data.items():
            if 'actor' in key.lower():
                actor.extend(value.split(','))
        actor = [x.strip() for x in actor]
        actor = list(set(actor))
        if len(actor) == 0:
            return None
        return ','.join(actor)

    def get_release_data(self, detail_data):
        release_data = None
        if 'Date First Available' in detail_data:
            release_data = detail_data['Date First Available']
        if 'Release date' in detail_data:
            release_data = detail_data['Release date']
        if release_data != None:
            res = re.match('(\w+) (\d+), (\d+)', release_data)
            month = list(calendar.month_name).index(res.group(1))
            day = res.group(2)
            year = res.group(3)
            release_data = year + '-' + str(month) + '-' + day
        return release_data

    def get_title(self, detail_data):
        return detail_data['productTitle']

    def get_score(self, detail_data):
        score = None
        if "averageCustomerReviews" in detail_data:
            score = detail_data["averageCustomerReviews"]
        return score

    def get_director(self, detail_data):
        director = []
        for key, value in detail_data['bylineInfo'].items():
            if 'director' in key.lower():
                director.extend(value.split(','))
        for key, value in detail_data.items():
            if 'director' in key.lower():
                director.extend(value.split(','))
        director = [x.strip() for x in director]
        director = list(set(director))
        if len(director) == 0:
            return None
        return  ','.join(director)

    def process_review(self):
        '''处理电影评论'''
        review_arr = os.listdir(self.review_json_path)
        asin_review = []
        for reviews in tqdm(review_arr):
            asin = reviews.split('.')[0]
            reviews_path = os.path.join(self.review_json_path, reviews)
            with open(reviews_path, encoding='utf-8') as f:
                reviews_data = json.load(f)
                for review in reviews_data:
                    asin_review.append([asin, review["review_id"], review["user_id"], review["user_name"],
                    review["helpfulness"], review["text"], review["score"], review["time"], review["summary"]])
        df = pd.DataFrame(asin_review, columns=['asin', 'review_id', 'user_id', 'user_name', 'helpfulness', 'text', 
                                                    'score', 'time', 'summary'], dtype='str')
        df.to_csv(self.processed_review_save_path, index=False)

def check():
    reviews = pd.read_csv('../../data/processed_data/asin_review.csv')
    print(reviews.values[0])

if __name__ == '__main__':
    process = Process()
    # process.process_detail()
    process.process_review()
    # check()