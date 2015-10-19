import wordfind_site

path_to_save = '../app/src/main/assets/words/'
file_prefix = 'words-length-'
response = wordfind_site.wordFindSite()

for length, words in response.iteritems():
    file = open('{0}{1}{2}'.format(path_to_save, file_prefix, length), 'r')
    wordsByLength = file.readlines()
    file.close()
    for word in words:
        if '{0}\n'.format(word.encode('utf-8')) in wordsByLength:
            print('False')
            pass
        else:
            print('true')
            wordsByLength.append('{0}\n'.format(word.encode('utf-8')))
    
    with open('{0}{1}{2}'.format(path_to_save, file_prefix, length), 'w') as file:
        for word in wordsByLength:
            file.write(word)    
