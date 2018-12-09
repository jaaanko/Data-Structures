import org.junit.Test;
import static org.junit.Assert.*;

public class TestPalindrome {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testWordToDeque() {
        Deque d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    }

    @Test
    public void testIsPalindrome(){
        assertFalse(Palindrome.isPalindrome("flake"));
        assertTrue(Palindrome.isPalindrome("raccar"));
        assertFalse(Palindrome.isPalindrome("raCecar"));
        assertTrue(Palindrome.isPalindrome(""));
        assertTrue(Palindrome.isPalindrome("a"));
    }
    @Test
    public void testIsOffByOnePalindrome(){
        CharacterComparator obo = new OffByOne();
        assertTrue(Palindrome.isPalindrome("flake",obo));
        assertFalse(Palindrome.isPalindrome("racecar",obo));
        assertTrue(Palindrome.isPalindrome("a",obo));
        assertTrue(Palindrome.isPalindrome("",obo));
        assertTrue(Palindrome.isPalindrome("abab",obo));
    }
    @Test
    public void testIsOffByNPalindrome(){
        CharacterComparator obo5 = new OffByN(5);
        CharacterComparator obo2 = new OffByN(2);
        assertTrue(Palindrome.isPalindrome("afaf",obo5));
        assertTrue(Palindrome.isPalindrome("acac",obo2));
        assertFalse(Palindrome.isPalindrome("azaz",obo5));
    }
}
