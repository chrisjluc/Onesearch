import requests
from bs4 import BeautifulSoup

def extract(htmlList):
    words = []
    
    finding_words = list(filter(lambda x: x.get('class') == ['defLink'], htmlList))
    
    for link in finding_words:
        words.append(link.find('a').get('href')[6:-1])     
        
    return words

def extract_from_word_find_site():
    wordFind_dict = {}
    for word_length in range(3,12):
        try:
            req = requests.get("http://www.wordfind.com/{0}-letter-words/".format(word_length))
            unextracted = BeautifulSoup(req.text, 'html.parser').find_all('li') 
            wordFind_dict[word_length] = extract(unextracted)  
        except requests.exceptions.ConnectionError as e:
            print ("A connection error occurred.") 
        
    return wordFind_dict  


