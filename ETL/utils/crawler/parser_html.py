from bs4 import BeautifulSoup
import re
import sys
import calendar
from parser_utils import spoken_word_to_number

def parse_home_get_label(html, asin):
    if html == None:
        print('html None')
        return None
    res = {'label': []}
    soup = BeautifulSoup(html.text.encode('gbk', 'ignore').decode('gbk'), 'lxml')
    for a in soup.findAll(name='a', attrs={"class" : "a-link-normal a-color-tertiary"}):
        if str(a.text).strip() == 'TV':
            return None
        res['label'].append(str(a.text).strip())
    return res

def parse_home_get_neighbors(html, asin):
    res = {'neighbors': []}
    res['neighbors'].append(asin)
    soup = BeautifulSoup(html.text.encode('gbk', 'ignore').decode('gbk'), 'lxml')
    
    neighbor_div = soup.find(name="div", attrs={"id": "MediaMatrix"})
    if neighbor_div is not None:
        ul = neighbor_div.find(name='ul')
        if ul is not None:
            for li in ul.find_all('li'):
                a = li.find("a")
                if str(a['href']).strip() != 'javascript:void(0)':
                    asin_neighbor = re.search('.*dp/(.*?)/ref.*', a['href']).group(1)
                    res['neighbors'].append(asin_neighbor)
    neighbor_div = soup.find(name="div", attrs={"id": "twister"})
    if neighbor_div is not None:
        for div in neighbor_div.find_all(name="div", attrs={"class": "top-level unselected-row"}):
            asin_neighbor = re.search('.*dp/(.*?)/ref.*', str(div), re.S).group(1)
            res['neighbors'].append(asin_neighbor)
    res['neighbors'] = list(set(res['neighbors']))
    return res

def parse_home(html, asin):
    if html == None:
        print('html None')
        return None
    soup = BeautifulSoup(html.text.encode('gbk', 'ignore').decode('gbk'), 'lxml')
    for a in soup.findAll(name='a', attrs={"class" : "a-link-normal a-color-tertiary"}):
        if str(a.text).strip() == 'TV':
            with open('./data/tv.txt', 'a') as f:
                f.write(asin.strip() + '\n')
            return None
    res = {'neighbors': [], 'bylineInfo':{}}
    neighbor_div = soup.find(name="div", attrs={"id": "MediaMatrix"})
    res['neighbors'].append(asin)
    if neighbor_div != None:
        ul = neighbor_div.find(name='ul')
        for li in ul.find_all('li'):
            a = li.find("a")
            if str(a['href']).strip() != 'javascript:void(0)':
                asin_neighbor = re.search('.*dp/(.*?)/ref.*', a['href']).group(1)
                res['neighbors'].append(asin_neighbor)
    productTitle = soup.find(name="span", attrs={"id": "productTitle"})
    res['productTitle'] = str(productTitle.text).strip()
    bylineInfo_div = soup.find(name="div", attrs={"id": "bylineInfo"})
    if bylineInfo_div != None:
        values = []
        for span in bylineInfo_div.find_all("span", {"class": "author"}):
            if span.span != None:
                key = re.search('.*?\((.*?)\).*', span.span.span.text).group(1)
                if len(values) == 0:
                    if key in  res['bylineInfo']:
                        res['bylineInfo'][key] += ','+span.a.text
                    else:
                        res['bylineInfo'][key] = span.a.text
                else:
                    value = ','.join(values)
                    values = []
                    if key in  res['bylineInfo']:
                        res['bylineInfo'][key] += ','+ str(span.a.text) + ',' + value
                    else:
                        res['bylineInfo'][key] = str(span.a.text) + ',' + value
            else:
                values.append(str(span.a.text))
    averageCustomerReviews = soup.find('div', id='detailBullets_averageCustomerReviews')
    if averageCustomerReviews != None:
        span = averageCustomerReviews.find('span',attrs={"class": "a-icon-alt"})
        averageCustomerReviews = str(span.text).split()[0]
        res['averageCustomerReviews'] = float(averageCustomerReviews)
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
        try:
            text = customer_review_div.find(name="span", attrs={"data-hook": "review-body"}).span.text
            text = str(text).strip()
        except Exception as e:
            text = None
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
    asin = 'B00D0Z2JT2'
    headers = get_random_headers()
    try:
        html, asin = get_home(asin, headers)
        res = parse_home(html, asin)
        print(res)
    except Exception as e:
        print(e)
        print(asin)
        raise e