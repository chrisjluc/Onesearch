import json
import requests
import re
import threading
import time
import urllib2

initial_words = ['hello', 'bob', 'how','are','you','there','twitter','static','check','cracker','apple','pear', 'after', 'finish', 'his', 'just', 'word', 'yet', 'random', 'destroy', 'monster', 'killer', 'gun', 'ravage', 'ramp', 'original', 'wheat', 'happy', 'strive', 'friday', 'monday', 'tuesday', 'help', 'shell', 'edit', 'view', 'window', 'july', 'june', 'august', 'september', 'january', 'february', 'may', 'march', 'april']
wordsByLength = {}
max_word_length = 11
min_word_length = 3
path_to_save = '../app/src/main/assets/words/'
file_prefix = 'words-length-'
lock = threading.Lock()

# Determines if we need to keep adding words to the dict
is_satisfied = False

# Target number of words at each length
target = {}
target.update({
    3: 250,
    4: 700,
    5: 1000,
    6: 1000,
    7: 1000,
    8: 1000,
    9: 800,
    10: 600,
    11: 300
    })


class WorkerThread(threading.Thread):

    def iterate_through_words(self, s):
        s = re.sub('[.!,?:;]', '', s)
        s = s.split()
        for word in s:
            add_to_dict(word.strip())


class WordWorkerThread(threading.Thread):

    def run(self):
        while not is_satisfied:
            try:
                req = requests.post('http://watchout4snakes.com/wo4snakes/Random/RandomWord')
                add_to_dict(req.text)
            except:
                pass


class SentenceWorkerThread(WorkerThread):

    def run(self):
        while not is_satisfied:
            try:
                req = requests.post('http://watchout4snakes.com/wo4snakes/Random/NewRandomSentence')
                self.iterate_through_words(req.text)
            except:
                pass


class ParagraphWorkerThread(WorkerThread):

    def run(self):
        while not is_satisfied:
            try:
                req = requests.post('http://watchout4snakes.com/wo4snakes/Random/RandomParagraph', data={'subject1':'', 'subject2':''})
                self.iterate_through_words(req.text)
            except:
                pass


# Static methods
def add_to_dict(word):
    if word.isalpha() and len(word) >= min_word_length and len(word) <= max_word_length:
        length = len(word)
        lock.acquire()
        if word not in wordsByLength[length] and len(wordsByLength[length]) < target[length]:
            wordsByLength[length].add(word.lower())
        lock.release()

def check_is_satisfied():
    for k, v in wordsByLength.iteritems():
        if len(v) < target[k]:
            return False
    return True

def set_up_initial_words():
    for i in xrange(min_word_length, max_word_length + 1):
        wordsByLength[i] = set()
    for word in initial_words:
        add_to_dict(word)

def print_summary():
    print "Summary"
    for k, v in wordsByLength.iteritems():
        print '{0} : {1}'.format(k, len(v))


set_up_initial_words()

# Start 8 threads to constantly make url requests and add words to the dict
# The wait time for responses was very high, multithread to make use of wasted time
WordWorkerThread().start()
WordWorkerThread().start()
SentenceWorkerThread().start()
SentenceWorkerThread().start()
SentenceWorkerThread().start()
ParagraphWorkerThread().start()
ParagraphWorkerThread().start()
ParagraphWorkerThread().start()

while not is_satisfied:
    time.sleep(5)
    if check_is_satisfied():
        break
    print_summary()
is_satisfied = True

for length, words in wordsByLength.iteritems():
    with open('{0}{1}{2}'.format(path_to_save, file_prefix, length), 'w') as file:
        for word in words:
            file.write('{0}\n'.format(word.encode('utf-8')))
