import pandas as pd
import json
from tqdm import tqdm


# 去重
class Dedulicate:

    def __init__(self, detail_csv='./data/processed_data/asin_detail.csv',
                    review_csv='./data/processed_data/asin_review.csv',
                    series_json = './data/processed_data/series.json',
                    deduplicated_detail_csv ='./data/processed_data/deduplicated_detail.csv',
                    deduplicated_review_csv ='./data/processed_data/deduplicated_review.csv') -> None:
        self.detail_csv = detail_csv
        self.review_csv = review_csv
        self.series_json = series_json
        self.dedulicated_detail_csv = deduplicated_detail_csv
        self.dedulicated_review_csv = deduplicated_review_csv

    def dedulicate_detail(self):
        df = pd.read_csv(self.detail_csv, encoding='utf-8')
        df = df.where(df.notnull(), None)
        detail_dict = {}
        for detail in df.values:
            detail_dict[detail[0]] = detail[1:]
        res = []
        with open(self.series_json) as f:
            series = json.load(f)
            for group in tqdm(series):
                asin = None
                for movie_asin in group:
                    if movie_asin in detail_dict.keys():
                        asin = movie_asin
                        break
                if asin is None:
                    continue
                title = detail_dict[asin][0]
                score = detail_dict[asin][1]
                director = []
                actor = []
                release_data = None
                label = []
                for movie_asin in group:
                    if movie_asin not in detail_dict.keys():
                        continue
                    if detail_dict[movie_asin][2] is not None:
                        director.extend(detail_dict[movie_asin][2].split(','))
                    if detail_dict[movie_asin][3] is not None:
                        actor.extend(detail_dict[movie_asin][3].split(','))
                    if detail_dict[movie_asin][4] is not None:
                        release_data = detail_dict[movie_asin][4]
                    if detail_dict[movie_asin][5] is not None:
                        label.extend(detail_dict[movie_asin][5].split(','))
                label_ = []
                for l in label:
                    label_.extend([x.strip() for x in l.split('&')])
                label_ = list(set(label_) - set(['TV', 'Movies']))
                director, actor = list(set(director)), list(set(actor))
                director = None if len(director) == 0 else ','.join(director)
                actor = None if len(actor) == 0 else ','.join(actor)
                label_ = None if len(label_) == 0 else ','.join(label_)
                res.append([asin, title, score, director, actor, release_data, label_])
            df = pd.DataFrame(res, columns=['asin', 'title', 'score', 'director', 'actor', 'release_data', 'label'], dtype='str')
            df.to_csv(self.dedulicated_detail_csv, index=False)
    
    def dedulicate_review(self):
        df = pd.read_csv(self.dedulicated_detail_csv, encoding='utf-8')
        df = df.where(df.notnull(), None)
        asin_set = set()
        for detail in df.values:
            asin_set.add(detail[0])
        res = []
        df = pd.read_csv(self.review_csv, encoding='utf-8')
        df = df.where(df.notnull(), None)
        for review in tqdm(df.values):
            if review[0] in asin_set:
                res.append(review)
        df = pd.DataFrame(res, columns=['asin', 'review_id', 'user_id', 'user_name', 'helpfulness', 'text', 
                                                    'score', 'time', 'summary'], dtype='str')
        df.to_csv(self.dedulicated_review_csv, index=False)

    def dedulicate_moviesource(self):
        df = pd.read_csv(self.detail_csv, encoding='utf-8')
        df = df.where(df.notnull(), None)
        detail_dict = {}
        for detail in df.values:
            detail_dict[detail[0]] = detail[1:]
        res = {}
        with open(self.series_json) as f:
            series = json.load(f)
            for group in tqdm(series):
                asin = None
                for movie_asin in group:
                    if movie_asin in detail_dict.keys():
                        asin = movie_asin
                        break
                if asin is None:
                    continue
                res[asin] = group
            with open('./data/processed_data/moviesource.json', 'w+', encoding='utf-8') as f:
                json.dump(res, f, ensure_ascii=False, indent=4, separators=(',', ':'))
        

if __name__ == '__main__':
    de = Dedulicate()
    # de.dedulicate_detail()
    # de.dedulicate_review()
    de.dedulicate_moviesource()