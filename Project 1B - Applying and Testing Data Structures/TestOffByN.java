import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByN {

    CharacterComparator offBy5 = new OffByN(5);
    CharacterComparator offBy2 = new OffByN(2);
    @Test
    public void testEqualChars(){
        assertTrue(offBy5.equalChars('a','f'));
        assertTrue(offBy5.equalChars('f','a'));
        assertFalse(offBy5.equalChars('b','d'));
        assertTrue(offBy2.equalChars('b','d'));
        assertFalse(offBy2.equalChars('a','d'));
    }

}
