import requests
from word_file_merger import word_merger
from bs4 import BeautifulSoup

def extract(htmlList):
    finding_words = filter(lambda x: x.get('class') == ['defLink'], htmlList)
    words = map(lambda x: x.find('a').get('href')[6:-1], finding_words)        
    return words

def extract_from_word_find_site():
    wordfind_dict = {}
    for word_length in range(3,12):
        try:
            req = requests.get("http://www.wordfind.com/{0}-letter-words/".format(word_length))
            unextracted = BeautifulSoup(req.text, 'html.parser').find_all('li') 
            wordfind_dict[word_length] = extract(unextracted)  
        except requests.exceptions.ConnectionError as e:
            print ("A connection error occurred.") 
        
    return wordfind_dict  

word_merger(extract_from_word_find_site())