import static org.junit.Assert.*;
import org.junit.Test;

/**
 * TestAkariViewer tests the class AkariViewer.
 *
 * @author  Lyndon While
 * @version 2021 v1
 */
public class TestAkariViewer
{
    String f7 = "Puzzles/p7-e7.txt";
    AkariViewer av7 = new AkariViewer();
    
    @Test
    public void testAkariViewer()
    {
        assertTrue  ("", av7 != null);
        assertTrue  ("", av7.getCanvas() != null);
        Akari a7 = av7.getPuzzle();
        assertTrue("",      a7 != null);
        assertTrue("",      a7.getFilename().equals(f7));
        assertEquals("", 7, a7.getSize());
        for (int c : new int[] {0,1,2,4,5,6})
            assertEquals(c + "", Space.EMPTY, a7.getBoard(0,c));
        assertEquals("", Space.BLACK, a7.getBoard(0,3));
        for (int c : new int[] {0,1,2,3,5,6})
            assertEquals(c + "", Space.EMPTY, a7.getBoard(1,c));
        assertEquals("", Space.FOUR, a7.getBoard(1,4));
        for (int c : new int[] {0,2,4,5,6})
            assertEquals(c + "", Space.EMPTY, a7.getBoard(2,c));
        for (int c : new int[] {1,3})
            assertEquals(c + "", Space.BLACK, a7.getBoard(2,c));
        assertEquals("", Space.ZERO,  a7.getBoard(3,0));
        assertEquals("", Space.EMPTY, a7.getBoard(3,1));
        assertEquals("", Space.ONE,   a7.getBoard(3,2));
        assertEquals("", Space.BLACK, a7.getBoard(3,3));
        assertEquals("", Space.TWO,   a7.getBoard(3,4));
        assertEquals("", Space.EMPTY, a7.getBoard(3,5));
        assertEquals("", Space.ONE,   a7.getBoard(3,6));
        for (int c : new int[] {0,1,2,4,6})
            assertEquals(c + "", Space.EMPTY, a7.getBoard(4,c));
        for (int c : new int[] {3,5})
            assertEquals(c + "", Space.BLACK, a7.getBoard(4,c));
        for (int c : new int[] {0,1,3,4,5,6})
            assertEquals(c + "", Space.EMPTY, a7.getBoard(5,c));
        assertEquals("", Space.TWO, a7.getBoard(5,2));
        for (int c : new int[] {0,1,2,4,5,6})
            assertEquals(c + "", Space.EMPTY, a7.getBoard(6,c));
        assertEquals("", Space.BLACK, a7.getBoard(6,3));
    }
    
    @Test
    public void testDisplayAndMouseMethods()
    {
        assert true : "display and mouse methods are not tested";
    }
    
    @Test
    public void testleftClick()
    {
        testAkariViewer();
        av7.leftClick(0,1);
        assertEquals("", Space.BULB,  av7.getPuzzle().getBoard(0,1));
        av7.leftClick(0,1);
        testAkariViewer();
        av7.leftClick(2,1);
        testAkariViewer();
        av7.leftClick(7,1);
        testAkariViewer();
    }
}
