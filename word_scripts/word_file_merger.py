
path_to_save = '../app/src/main/assets/words/'
file_prefix = 'words-length-'

def word_merger(dict_of_words):
    for length, words in dict_of_words.items():
        with open('{0}{1}{2}'.format(path_to_save, file_prefix, length), 'r') as file:
            wordsByLength = file.readlines()
        
        for word in words:
            if '{0}\n'.format(word) in wordsByLength:
                print(word + " already in list")
            else:
                wordsByLength.append('{0}\n'.format(word))        

        with open('{0}{1}{2}'.format(path_to_save, file_prefix, length), 'w') as file:
            for word in wordsByLength:
                file.write(word)
                            