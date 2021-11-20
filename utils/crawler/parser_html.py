from bs4 import BeautifulSoup
import re
import sys
import calendar
from parser_utils import spoken_word_to_number

def parse_home(html):
    if html == None:
        print('html None')
        return None
    soup = BeautifulSoup(html.text.encode('gbk', 'ignore').decode('gbk'), 'lxml')
    res = {}
    detail = soup.find('div', id='detailBullets_feature_div').ul
    for li in detail.find_all('li'):
        spans = li.span.find_all('span')
        key = spans[0].text.split('\n')[0]
        value = spans[1].text
        res[key] = value
    return res

def parse_review(html):
    if html == None:
        print('html None')
        return None
    html.encoding = 'utf-8'
    soup = BeautifulSoup(html.text.encode('gbk', 'ignore').decode('gbk'), 'lxml')
    res = []
    reviews = soup.findAll(name="div", attrs={"data-hook" : "review"})
    for idx, review in enumerate(reviews):
        customer_review_div = review.div.div
        divs = list(customer_review_div.children)
        try:
            user_id = re.search(r'account.(.*)/ref', divs[0].a['href'])
            if user_id == None:
                user_id = re.search(r'account.(.*)', divs[0].a['href'])
            user_id = user_id.group(1)
        except:
            user_id = None
        user_name = divs[0].find(name="span", attrs={"class": "a-profile-name"}).text
        try:
            helpfulness = customer_review_div.find(name="span", attrs={"data-hook" : "helpful-vote-statement"}).text
            helpfulness = parse_helpfulness(str(helpfulness).strip())
        except:
            helpfulness = 0
        text = customer_review_div.find(name="span", attrs={"data-hook": "review-body"}).span.text
        text = str(text).strip()
        score = customer_review_div.find(name="i", attrs={"data-hook": "review-star-rating"})
        if score == None:
            score = customer_review_div.find(name="i", attrs={"data-hook": "cmps-review-star-rating"})
        score = str(score.span.text).split()[0]   
        review_time = customer_review_div.find(name="span", attrs={"data-hook": "review-date"}).text
        review_time = parse_time(review_time)
        summary = customer_review_div.find(name="a", attrs={"data-hook": "review-title"})
        if summary == None:
            summary = customer_review_div.find(name="span", attrs={"data-hook": "review-title"})
        summary = summary.span.text
        res.append(
            {
                'review_id': review['id'],
                'user_id': user_id,
                'user_name': user_name,
                'helpfulness': helpfulness,
                'text': text,
                'score': score,
                'time': review_time,
                'summary': summary
            }
        )

        # try: # other countries
        #     if 'a-divider' in review.next_sibling['class'] or 'a-divider-section' in review.next_sibling['class']:
        #         print(review.next_sibling['class'])
        #         break
        # except:
        #     pass
    return res

def parse_time(time):
    res = re.search('on (\w+) (\d+), (\d+)', time)
    month = list(calendar.month_name).index(res.group(1))
    day = res.group(2)
    year = res.group(3)
    return f'{month} {day}, {year}'

def parse_helpfulness(helpfulness):
    if helpfulness == 'Helpful':
        helpfulness = 0
    elif str(helpfulness[0]).isdigit():
        helpfulness = int(str(helpfulness).split()[0])
    else:
        word = re.search('(.*) person', helpfulness).group(1)
        helpfulness = spoken_word_to_number(word)
    return helpfulness

if __name__ =='__main__':
    from request import *
    from batch import *
    asin = '0783115539'
    headers = get_random_headers()
    try:
        review_html = get_review(asin, headers)
        res = parse_review(review_html)
        print(res[0])
    except Exception as e:
        print(e)
        print(asin)
        with open('hh.html', 'w+') as f:
            f.write(review_html.text.encode('gbk', 'ignore').decode('gbk'))
        raise e
    # res = parse_home(html)
    with open('hh.json', 'w+') as f:
        json.dump(res, f, ensure_ascii=False, indent=4, separators=(',', ':'))