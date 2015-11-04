import threading
import time
import enchant
from profanity_filter import arrBad

path_to_save = '../app/src/main/assets/words/'
file_prefix = 'words-length-'
auto_correct = enchant.Dict('en_US')   # create dictionary for US English
target = {
    3: 5000,
    4: 5000,
    5: 5000,
    6: 3000,
    7: 3000,
    8: 1000,
    9: 800,
    10: 600,
    11: 300
    }
is_satisfied = {x: False for x in range(3, 12)}
words_by_length = {x: [] for x in range(3, 12)}
new_words = {x: [] for x in range(3, 12)}
lock = threading.RLock()

def word_merger(dict_of_words):
    for word_length in range(3, 12):
        with open('{0}{1}{2}'.format(path_to_save, file_prefix, word_length), 'r') as file:
            words_by_length[word_length]=[line.rstrip() for line in file]
            target[word_length] = target[word_length] - len(words_by_length[word_length])
   
    class filterThread (threading.Thread):
        
        def __init__(self, length, words):
            threading.Thread.__init__(self)
            self.length = length
            self.words = set(words)
            
        def run(self):
            for word in self.words:
                if len(new_words[self.length]) < target[self.length]:
                    check_if_word(word, self.length)
                else:              
                    new_words[self.length] = new_words[self.length][:target[self.length]]
                    break                
            is_satisfied[self.length] = True            
    
    # Static Methods
    def check_if_word(word, length):
        if not(auto_correct.check(word)):
            suggestions = filter(lambda x: len(x) == length, auto_correct.suggest(word))
            lock.acquire()
            suggested_words = set([x.lower() for x in suggestions if x.isalpha()])
            new_words[length].extend(list(suggested_words - set(new_words[length]) - set(arrBad) - set(words_by_length[length])))
            lock.release()
        else:
            new_words[length].extend(list(set([word]) - set(new_words[length]) - set(arrBad) - set(words_by_length[length])))
    
    def check_is_satisfied():
        if all(is_satisfied.values()):
            return True
        return False   
    
    def print_summary():
        print ('Summary')
        for k, v in new_words.iteritems():
            print ('{0} : {1}'.format(k, len(v)))    
                
    # Create new threads
    thread1 = filterThread(3, dict_of_words[3])
    thread2 = filterThread(4, dict_of_words[4])
    thread3 = filterThread(5, dict_of_words[5])
    thread4 = filterThread(6, dict_of_words[6])    
    thread5 = filterThread(7, dict_of_words[7])
    thread6 = filterThread(8, dict_of_words[8]) 
    thread7 = filterThread(9, dict_of_words[9])
    thread8 = filterThread(10, dict_of_words[10])
    thread9 = filterThread(11, dict_of_words[11])
    
    # Start new Threads
    thread1.start()
    thread2.start()
    thread3.start()
    thread4.start()
    thread5.start()
    thread6.start()    
    thread7.start()
    thread8.start()
    thread9.start()
        
    while True:
        time.sleep(5)
        if check_is_satisfied():
            break
        print_summary()
        
    for length, words in new_words.iteritems():
        string_of_words = ''
        with open('{0}{1}{2}'.format(path_to_save, file_prefix, length), 'a') as file:
            for word in words:
                string_of_words += '{0}\n'.format(word)
            file.write(string_of_words)