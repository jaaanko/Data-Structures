public class Palindrome {

    public static Deque<Character> wordToDeque(String word){
        Deque<Character> d = new ArrayDeque<Character>();
        for(int i = 0;i<word.length();i++){
            d.addLast(word.charAt(i));
        }
        return d;
    }

    public static boolean isPalindrome(String word){
        Deque<Character> d = wordToDeque(word);
        if(d.isEmpty() || d.size() == 1){
            return true;
        }
        return isPalindrome(d,d.removeFirst(),d.removeLast());
    }

    private static boolean isPalindrome(Deque<Character> word, char first, char last ){
        if(first != last){
            return false;
        }
        else if(word.size() <= 1){
            return true;
        }
        return isPalindrome(word,word.removeFirst(),word.removeLast());
    }

    public static boolean isPalindrome(String word, CharacterComparator cc){
        Deque<Character> d = wordToDeque(word);
        if(d.isEmpty() || d.size() == 1){
            return true;
        }
        return isPalindrome(d,cc);
    }

    private static boolean isPalindrome(Deque<Character> word, CharacterComparator cc){
        if(!cc.equalChars(word.removeFirst(),word.removeLast())){
            return false;
        }
        else if(word.size() <= 1){
            return true;
        }
        return isPalindrome(word,cc);
    }
}
