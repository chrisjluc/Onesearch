path_to_save = '../app/src/main/assets/words/'
file_prefix = 'words-length-'
target = {}
target.update({
    3: 5000,
    4: 5000,
    5: 5000,
    6: 3000,
    7: 3000,
    8: 1000,
    9: 800,
    10: 600,
    11: 300
    })

def word_merger(dict_of_words):
    for length, words in dict_of_words.items():
        with open('{0}{1}{2}'.format(path_to_save, file_prefix, length), 'r') as file:
            wordsByLength = [line.rstrip() for line in file] 
        words_left_to_add = len(wordsByLength) 
        
        new_words = list(set(words)-set(wordsByLength))    

        with open('{0}{1}{2}'.format(path_to_save, file_prefix, length), 'a') as file:
            for word in new_words:
                if words_left_to_add < target[length]:
                    file.write('{0}\n'.format(word))
                    words_left_to_add += 1
                else:
                    break

                            