import pandas as pd
import csv
from tqdm import tqdm
import numpy as np
import re
from textblob import TextBlob

def nlp(asin, text):
    blob = TextBlob(text)
    res = blob.sentiment.polarity
    return 1 if res >= 0 else 0

def run():
    df = pd.read_csv('./data/processed_data/deduplicated_review.csv', encoding='utf-8')
    df = df.where(df.notnull(), None)
    with open('./data/processed_data/deduplicated_review_nlp.csv', 'w', newline='', encoding='utf-8') as f:
        writer = csv.writer(f)
        writer.writerow(['asin', 'review_id', 'user_id', 'user_name', 'helpfulness', 'text', 
                        'score', 'time', 'summary', 'nlp'])
        for review in tqdm(df.values):
            res = re.match('(\d+?) (\d+?), (\d+)', review[7])
            review[7] = f'{res.group(3)}-{res.group(1)}-{res.group(2)}'
            if review[2] is None or review[5] is None:
                continue
            text = review[5]
            writer.writerow(np.append(review, nlp(review[0], text)))
            
if __name__ =='__main__':
    run()