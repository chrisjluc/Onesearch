import threading
import time
import enchant

path_to_save = '../app/src/main/assets/words/'
file_prefix = 'words-length-'
d = enchant.Dict('en_US')   # create dictionary for US English
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
is_satisfied = {x: False for x in range(3,12)}
words_by_length = {x: [] for x in range(3,12)}
new_words = {x: [] for x in range(3,12)}
lock = threading.RLock()

arrBad = [
'2g1c',
'2 girls 1 cup',
'acrotomophilia',
'anal',
'anilingus',
'anus',
'arsehole',
'ass',
'asses',
'asshole',
'assmunch',
'auto erotic',
'autoerotic',
'babeland',
'baby batter',
'ball gag',
'ball gravy',
'ball kicking',
'ball licking',
'ball sack',
'ball sucking',
'bangbros',
'bareback',
'barely legal',
'barenaked',
'bastardo',
'bastinado',
'bbw',
'bdsm',
'beaver cleaver',
'beaver lips',
'bestiality',
'bi curious',
'big black',
'big breasts',
'big knockers',
'big tits',
'bimbos',
'birdlock',
'bitch',
'black cock',
'blonde action',
'blonde on blonde action',
'blow j',
'blow your l',
'blue waffle',
'blumpkin',
'bollocks',
'bondage',
'boner',
'boob',
'boobs',
'booty call',
'brown showers',
'brunette action',
'bukkake',
'bulldyke',
'bullet vibe',
'bung hole',
'bunghole',
'busty',
'butt',
'buttcheeks',
'butthole',
'camel toe',
'camgirl',
'camslut',
'camwhore',
'carpet muncher',
'carpetmuncher',
'chocolate rosebuds',
'circlejerk',
'cleveland steamer',
'clit',
'clitoris',
'clover clamps',
'clusterfuck',
'cock',
'cocks',
'coprolagnia',
'coprophilia',
'cornhole',
'cum',
'cumming',
'cunnilingus',
'cunt',
'darkie',
'date rape',
'daterape',
'deep throat',
'deepthroat',
'dick',
'dildo',
'dirty pillows',
'dirty sanchez',
'dog style',
'doggie style',
'doggiestyle',
'doggy style',
'doggystyle',
'dolcett',
'domination',
'dominatrix',
'dommes',
'donkey punch',
'double dong',
'double penetration',
'dp action',
'eat my ass',
'ecchi',
'ejaculation',
'erotic',
'erotism',
'escort',
'ethical slut',
'eunuch',
'faggot',
'fecal',
'felch',
'fellatio',
'feltch',
'female squirting',
'femdom',
'figging',
'fingering',
'fisting',
'foot fetish',
'footjob',
'frotting',
'fuck',
'fucking',
'fuck buttons',
'fudge packer',
'fudgepacker',
'futanari',
'g-spot',
'gag',
'gang bang',
'gay sex',
'genitals',
'giant cock',
'girl on',
'girl on top',
'girls gone wild',
'goatcx',
'goatse',
'gokkun',
'golden shower',
'goo girl',
'goodpoop',
'goregasm',
'grope',
'group sex',
'guro',
'hand job',
'handjob',
'hard core',
'hardcore',
'hentai',
'homoerotic',
'honkey',
'hooker',
'hot chick',
'how to kill',
'how to murder',
'huge fat',
'humping',
'incest',
'intercourse',
'jack off',
'jail bait',
'jailbait',
'jerk off',
'jigaboo',
'jiggaboo',
'jiggerboo',
'jizz',
'juggs',
'kike',
'kinbaku',
'kinkster',
'kinky',
'knobbing',
'leather restraint',
'leather straight jacket',
'lemon party',
'lolita',
'lovemaking',
'make me come',
'male squirting',
'masturbate',
'menage a trois',
'milf',
'missionary position',
'motherfucker',
'mound of venus',
'mr hands',
'muff diver',
'muffdiving',
'nambla',
'nawashi',
'negro',
'neonazi',
'nig nog',
'nigga',
'nigger',
'nimphomania',
'nipple',
'nipples',
'nsfw images',
'nude',
'nuder',
'nudity',
'nympho',
'nymphomania',
'octopussy',
'omorashi',
'one cup two girls',
'one guy one jar',
'orgasm',
'orgy',
'paedophile',
'panties',
'panty',
'pedobear',
'pedophile',
'pegging',
'penis',
'phone sex',
'piece of shit',
'piss pig',
'pissing',
'pisspig',
'playboy',
'pleasure chest',
'pole smoker',
'ponyplay',
'poof',
'poop chute',
'poopchute',
'porn',
'porno',
'pornography',
'prince albert piercing',
'pthc',
'pubes',
'pussy',
'queaf',
'raghead',
'raging boner',
'rape',
'raping',
'rapist',
'rectum',
'reverse cowgirl',
'rimjob',
'rimming',
'rosy palm',
'rosy palm and her 5 sisters',
'rusty trombone',
's&m',
'sadism',
'scat',
'schlong',
'scissoring',
'semen',
'sex',
'sexo',
'sexy',
'shaved beaver',
'shaved pussy',
'shemale',
'shibari',
'shit',
'shota',
'shrimping',
'slanteye',
'slut',
'smut',
'snatch',
'snowballing',
'sodomize',
'sodomy',
'spic',
'spooge',
'spread legs',
'strap on',
'strapon',
'strappado',
'strip club',
'style doggy',
'suck',
'sucks',
'suicide girls',
'sultry women',
'swastika',
'swinger',
'tainted love',
'taste my',
'tea bagging',
'threesome',
'throating',
'tied up',
'tight white',
'tit',
'tits',
'titties',
'titty',
'tongue in a',
'topless',
'tosser',
'towelhead',
'tranny',
'tribadism',
'tub girl',
'tubgirl',
'tushy',
'twat',
'twink',
'twinkie',
'two girls one cup',
'undressing',
'upskirt',
'urethra play',
'urophilia',
'vagina',
'venus mound',
'vibrator',
'violet blue',
'violet wand',
'vorarephilia',
'voyeur',
'vulva',
'wank',
'wet dream',
'wetback',
'white power',
'whore',
'women rapping',
'wrapping men',
'wrinkled starfish',
'xx',
'xxx',
'yaoi',
'yellow showers',
'yiffy',
'zoophilia']

def word_merger(dict_of_words):
    for word_length in range(3,12):
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
        if not(d.check(word)):
            suggestions = filter(lambda x: len(x) == length, d.suggest(word))
            lock.acquire()
            suggested_words = set([x.lower() for x in suggestions if x.isalpha()])
            new_words[length].extend(list(((suggested_words - set(new_words[length])) - set(arrBad)) - set(words_by_length[length])))
            lock.release()
        else:
            new_words[length].extend(list(((set([word]) - set(new_words[length])) - set(arrBad)) - set(words_by_length[length])))
    
    def check_is_satisfied():
        true = 0
        for key in is_satisfied:            
            if is_satisfied[key]:
                true += 1
        if true == 9:
            return True
        return False   
    
    def print_summary():
        print ('Summary')
        for k, v in new_words.items():
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
        
    for length, words in new_words.items():
        string_of_words = ''
        with open('{0}{1}{2}'.format(path_to_save, file_prefix, length), 'a') as file:
            for word in words:
                string_of_words += '{0}\n'.format(word)
            file.write(string_of_words)