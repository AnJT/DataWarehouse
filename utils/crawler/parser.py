from bs4 import BeautifulSoup
import re

def parse_home(html):
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
    soup = BeautifulSoup(html.text.encode('gbk', 'ignore').decode('gbk'), 'lxml')
    res = []
    reviews = soup.findAll(name="div", attrs={"data-hook" : "review"})
    for idx, review in enumerate(reviews):
        customer_review_div = review.div.div
        divs = list(customer_review_div.children)

        user_id = re.search('account.(.*)/ref', divs[0].a['href']).group(1)
        user_name = divs[0].a.div.next_sibling.span.text
        helpfulness = str(divs[5].span.div.span.text)
        helpfulness = int(helpfulness.split()[0]) if str(helpfulness[0]).isdigit() else 0
        text = str(divs[4].span.span.text).strip()
        score = divs[1].a.i.span.text   
        review_time = divs[2].text
        summary = divs[1].a.next_sibling.next_sibling.span.text
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



if __name__ =='__main__':
    from request import *
    from batch import *
    asin = 'B00005JLI6'
    headers = get_random_headers()
    html, review_html = get_html(asin.strip(), headers)
    # res = parse_review(review_html)
    res = parse_home(html)
    print(res)