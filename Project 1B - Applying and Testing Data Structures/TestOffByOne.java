import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByOne {

    // You must use this CharacterComparator and not instantiate
    // new ones, or the autograder might be upset.
    static CharacterComparator offByOne = new OffByOne();
    @Test
    public void testEqualChars(){
        assertTrue(offByOne.equalChars('a','b'));
        assertFalse(offByOne.equalChars('a','z'));
        assertFalse(offByOne.equalChars('a','a'));
        assertFalse(offByOne.equalChars('a','B'));
        assertTrue(offByOne.equalChars('&','%'));
    }
}