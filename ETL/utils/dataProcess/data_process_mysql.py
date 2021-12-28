import pandas as pd
import numpy as np
from pandas.core.frame import DataFrame

detail_path = "./data/processed_data/deduplicated_detail_date.csv"
review_path = "./data/processed_data/deduplicated_review_nlp.csv"

def generate_score_schema():
    df = pd.read_csv(detail_path)
    data = df.to_dict()
    score_schema = {
        'score': [],
        'score_count': []
    }
    for i in range(0, 51):
        score_schema['score'].append(i / 10)
        score_schema['score_count'].append(0)
    for i in data['score']:
        val = 0
        if data['score'][i] != data['score'][i]:
            val = 0
        else:
            val = int(data['score'][i] * 10)
        score_schema['score_count'][val] += 1
    # print(score_schema)
    ddf = pd.DataFrame(score_schema)
    # print(ddf)
    ddf.to_csv('./data/processed_data/score_schema.csv')

def generate_user_schema():
    df = pd.read_csv(review_path)
    data = df.to_numpy()
    vis = set()
    user_schema = {
        'user_id': [],
        'profile_name': []
    }
    for i in data:
        if not i[2] in vis:
            user_schema['user_id'].append(i[2])
            user_schema['profile_name'].append(i[3])
            vis.add(i[2])
    for i in range(0, len(user_schema['profile_name'])):
        if user_schema['profile_name'][i] != user_schema['profile_name'][i]:
            user_schema['profile_name'][i] = ""
        if len(user_schema['profile_name'][i]) > 20:
            user_schema['profile_name'][i] = user_schema['profile_name'][i][0:20]
    ddf = pd.DataFrame(user_schema)
    ddf.to_csv('./data/processed_data/user_schema.csv')


def generate_movie_schema():
    df = pd.read_csv(detail_path)
    data = df.to_numpy()
    movie_schema = {
        'product_id': data[:, 0],
        'title': data[:, 1],
        'score_score': data[:, 2],
        'time_time_id': data[:, 5]
    }
    for i in range(0, len(movie_schema['title'])):
        if len(movie_schema['title'][i]) > 20:
            movie_schema['title'][i] = movie_schema['title'][i][0:20]
    ddf = pd.DataFrame(movie_schema)
    ddf.to_csv('./data/processed_data/movie.csv')


def generate_review_schema():
    df = pd.read_csv(review_path)
    data = df.to_numpy()
    review_schema = {
        'id': data[:, 1],
        'emotion_score': data[:, 9],
        'score': data[:, 6],
        'summary': data[:, 8],
        'text': data[:, 5],
        'time_stamp': data[:, 7],
        'helpfulness': data[:, 4],
        'user_user_id': data[:, 2],
        'movie_product_id': data[:, 0]
    }
    # print(type(review_schema['time_stamp'][0]))
    # print(review_schema['time_stamp'][0])
    ddf = pd.DataFrame(review_schema)
    ddf.to_csv('./data/processed_data/review.csv')

def generate_director_schema():
    df = pd.read_csv(detail_path)
    data = df.to_numpy()
    director_schema = {
        'director_name': [],
        'product_id': [],
        'movie_count': []
    }
    movie_count = {}
    for ds in data:
        if str(ds[3]) == 'nan':
            continue
        else:
            dirs = str(ds[3]).split(',')
            for d in dirs:
                if d in movie_count.keys():
                    movie_count[d] += 1
                    director_schema['director_name'].append(d)
                    director_schema['product_id'].append(str(ds[0]))
                else:
                    movie_count.update({d: 1})
                    director_schema['director_name'].append(d)
                    director_schema['product_id'].append(str(ds[0]))
    for i in director_schema['director_name']:
        director_schema['movie_count'].append(movie_count[i])
    ddf = pd.DataFrame(director_schema)
    ddf.to_csv('./data/processed_data/director.csv')

def generate_actor_schema():
    df = pd.read_csv(detail_path)
    data = df.to_numpy()
    actor_schema = {
        'actor_name': [],
        'product_id': [],
        'movie_count': []
    }
    movie_count = {}
    for ds in data:
        if str(ds[4]) == 'nan':
            continue
        else:
            dirs = str(ds[4]).split(',')
            for d in dirs:
                if d in movie_count.keys():
                    movie_count[d] += 1
                    actor_schema['actor_name'].append(d)
                    actor_schema['product_id'].append(str(ds[0]))
                else:
                    movie_count.update({d: 1})
                    actor_schema['actor_name'].append(d)
                    actor_schema['product_id'].append(str(ds[0]))
    for i in actor_schema['actor_name']:
        actor_schema['movie_count'].append(movie_count[i])
    ddf = pd.DataFrame(actor_schema)
    ddf.to_csv('./data/processed_data/actor.csv')

def generate_label_schema():
    df = pd.read_csv(detail_path)
    data = df.to_numpy()
    label_schema = {
        'label_name': [],
        'product_id': [],
        'movie_count': []
    }
    movie_count = {}
    for ds in data:
        if str(ds[6]) == 'nan':
            continue
        else:
            dirs = str(ds[6]).split(',')
            for d in dirs:
                if d in movie_count.keys():
                    movie_count[d] += 1
                    label_schema['label_name'].append(d)
                    label_schema['product_id'].append(str(ds[0]))
                else:
                    movie_count.update({d: 1})
                    label_schema['label_name'].append(d)
                    label_schema['product_id'].append(str(ds[0]))
    for i in label_schema['label_name']:
        label_schema['movie_count'].append(movie_count[i])
    # print(len(label_schema['product_id']))
    ddf = pd.DataFrame(label_schema)
    ddf.to_csv('./data/processed_data/label.csv')

def generate_schema():
    generate_review_schema()
    generate_actor_schema()
    generate_director_schema()
    generate_movie_schema()
    generate_score_schema()
    generate_user_schema()
    generate_label_schema()

if __name__ == "__main__":
    generate_review_schema()
    generate_director_schema()
    generate_actor_schema()