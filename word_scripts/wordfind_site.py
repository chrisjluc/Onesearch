import requests
from bs4 import BeautifulSoup

def wordFindSite ():
    wordFind_dict = {}
    for word_length in range(3,12):
        try:
            req = requests.get("http://www.wordfind.com/{0}-letter-words/".format(word_length))
        except:
            pass
        
        unextracted = BeautifulSoup(req.text, 'html.parser').find_all('li')
        
        def extract (htmlList):
            words = []
            
            finding_words = list(filter(lambda x: True if x.get('class') == ['defLink'] else False, htmlList))
            
            for link in finding_words:
                words.append(link.find('a').get('href')[6:-1])     
                
            return words
        
        wordFind_dict[word_length] = extract(unextracted)
        
    return wordFind_dict  
