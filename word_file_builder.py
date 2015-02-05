import urllib2
import json
import requests

initial_words = ['hello', 'bob', 'how','are','you','there','twitter','static','check','cracker','apple','pear']
wordsByLength = {}
min_size = 1000
max_size = 1000
max_word_length = 11
min_word_length = 3
print_frequency = 50
path_to_save = 'app/src/main/assets/words/'
file_prefix = 'words-length-'

def add_to_dict(word):
    if word.isalpha() and len(word) >= min_word_length and len(word) <= max_word_length:
       length = len(word)
       if word not in wordsByLength[length] and len(wordsByLength[length]) < max_size:
            wordsByLength[length].add(word.lower())

def set_up_initial_words():
    for i in xrange(min_word_length, max_word_length + 1):
        wordsByLength[i] = set()
    for word in initial_words:
        add_to_dict(word)

def get_random_words():
    req = requests.post('http://watchout4snakes.com/wo4snakes/Random/RandomWord')
    add_to_dict(req.text)
    '''response = urllib2.urlopen("http://api.wordnik.com:80/v4/words.json/randomWords?hasDictionaryDef=false&minCorpusCount=0&maxCorpusCount=-1&minDictionaryCount=1&maxDictionaryCount=-1&minLength=3&maxLength=11&limit=10000&api_key=a2a73e7b926c924fad7001ca3111acd55af2ffabf50eb4ae5")
    response = json.load(response)
    for obj in response:
        add_to_dict(obj['word'])
        '''

def is_satisfied():
    for k, v in wordsByLength.iteritems():
        if len(v) < min_size:
            return False
    return True

def print_summary():
    print "Summary"
    for k, v in wordsByLength.iteritems():
        print '{0} : {1}'.format(k, len(v))

word_count = 0
set_up_initial_words()
get_random_words()
while not is_satisfied():
    get_random_words()
    if word_count % print_frequency == 0:
        print_summary()
    word_count = word_count + 1

for length, words in wordsByLength.iteritems():
    with open('{0}{1}{2}'.format(path_to_save, file_prefix, length), 'w') as file:
        for word in words:
            file.write('{0}\n'.format(word.encode('utf-8')))
