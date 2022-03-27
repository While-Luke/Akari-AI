import static org.junit.Assert.*;
import org.junit.Test;

/**
 * TestAkari tests the class Akari.
 *
 * @author  Lyndon While
 * @version 2021 v1
 */
public class TestAkari
{
    String f7 = "Puzzles/p7-e7.txt";
    Akari  a7 = new Akari(f7);
    
    @Test
    public void testAkari()
    {
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
    public void testisLegal1()
    {
        assertTrue ("", a7.isLegal(0));
        assertTrue ("", a7.isLegal(6));
        assertFalse("", a7.isLegal(7));
    }
    
    @Test
    public void testisLegal2()
    {
        assertTrue ("", a7.isLegal(0,0));
        assertTrue ("", a7.isLegal(0,6));
        assertTrue ("", a7.isLegal(6,0));
        assertTrue ("", a7.isLegal(6,6));
        assertFalse("", a7.isLegal(0,7));
        assertFalse("", a7.isLegal(7,0));
    }
    
    @Test
    public void testgetBoard()
    {
        assertEquals("", Space.EMPTY, a7.getBoard(0,0));
        assertEquals("", Space.ONE,   a7.getBoard(3,6));
        assertEquals("", Space.BLACK, a7.getBoard(6,3));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testgetBoardException() 
    {
        a7.getBoard(7,0);
    }
    
    @Test
    public void testparseIndex()
    {
        assertEquals("",  0, Akari.parseIndex('0'));
        assertEquals("",  9, Akari.parseIndex('9'));
        assertEquals("", 10, Akari.parseIndex('A'));
        assertEquals("", 19, Akari.parseIndex('J'));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testparseIndexException() 
    {
        Akari.parseIndex('a');
    }
    
    @Test
    public void testleftClick()
    {
        assertEquals("", Space.EMPTY, a7.getBoard(4,2));
        assertEquals("", Space.EMPTY, a7.getBoard(2,4));
        a7.leftClick(4,2);
        assertEquals("", Space.BULB,  a7.getBoard(4,2));
        assertEquals("", Space.EMPTY, a7.getBoard(2,4));
        a7.leftClick(4,2);
        assertEquals("", Space.EMPTY, a7.getBoard(4,2));
        assertEquals("", Space.EMPTY, a7.getBoard(2,4));
        a7.leftClick(4,3);
        assertEquals("", Space.EMPTY, a7.getBoard(4,2));
        assertEquals("", Space.EMPTY, a7.getBoard(2,4));
        a7.leftClick(4,7);
        assertEquals("", Space.EMPTY, a7.getBoard(4,2));
        assertEquals("", Space.EMPTY, a7.getBoard(2,4));
    }
    
    @Test
    public void testclear()
    {
        testAkari();
        for (int i = 0; i < 7; i++)
            for (int j = 0; j < 7; j++)
                a7.leftClick(i,j); // click everywhere once
        a7.clear();                // then clear the board
        testAkari();
    }
    
    @Test
    public void testnumberOfBulbs()
    {
        assertEquals("", 0, a7.numberOfBulbs(2,5));
        a7.leftClick(2,4);
        assertEquals("", 1, a7.numberOfBulbs(2,5));
        a7.leftClick(1,5);
        assertEquals("", 2, a7.numberOfBulbs(2,5));
        a7.leftClick(2,6);
        assertEquals("", 3, a7.numberOfBulbs(2,5));
        a7.leftClick(3,5);
        assertEquals("", 4, a7.numberOfBulbs(2,5));
        a7.leftClick(2,5);
        assertEquals("", 4, a7.numberOfBulbs(2,5));
        
        assertEquals("", 0, a7.numberOfBulbs(0,6));
        a7.leftClick(1,6);
        assertEquals("", 1, a7.numberOfBulbs(0,6));
        a7.leftClick(0,5);
        assertEquals("", 2, a7.numberOfBulbs(0,6));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testnumberOfBulbsException() 
    {
        a7.numberOfBulbs(7,0);
    }
    
    @Test
    public void testcanSeeBulb()
    {
        a7.leftClick(1,2);
        for (int k = 0; k < 7; k++)
            assertTrue(k + "", a7.canSeeBulb(1,k) == (k != 2 && k <= 4));
        for (int k = 0; k < 7; k++)
            assertTrue(k + "", a7.canSeeBulb(k,2) == (k != 1 && k <= 3));
        for (int r = 0; r < 7; r++)
            for (int c = 0; c < 7; c++)
                assertTrue(r + "," + c + "", r == 1 || c == 2 || !a7.canSeeBulb(r,c));
        a7.leftClick(6,6);
        for (int k = 0; k < 7; k++)
        {
            assertTrue(k + "", a7.canSeeBulb(6,k) == (k != 6 && k >= 3));
            assertTrue(k + "", a7.canSeeBulb(k,6) == (k != 6 && k >= 3));
            assertTrue(k + "", a7.canSeeBulb(5,k) == (k == 6));
            assertTrue(k + "", a7.canSeeBulb(k,5) == (k == 6));
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testcanSeeBulbException() 
    {
        a7.canSeeBulb(7,0);
    }
    
    @Test
    public void testisSolution()
    {
        for (int[] x : new int[][] {{0,0},{0,4},{1,3},{1,5},{2,2},{2,4}
                                   ,{4,1},{4,4},{4,6},{5,0},{5,3},{6,2},{6,5}})
            a7.leftClick(x[0],x[1]);
        // solution set up
        assertTrue("", a7.isSolution().equals("\u2713\u2713\u2713"));
        
        a7.leftClick(5,0);
        assertTrue("", a7.isSolution().equals("Unlit square at 5,0"));
        a7.leftClick(5,0);
        // solution restored
        assertTrue("", a7.isSolution().equals("\u2713\u2713\u2713"));
        
        a7.leftClick(6,2);
        a7.leftClick(6,0);
        a7.leftClick(5,0);
        assertTrue("", a7.isSolution().equals("Broken number at 5,2"));
        a7.leftClick(6,2);
        a7.leftClick(6,0);
        a7.leftClick(5,0);
        // solution restored
        assertTrue("", a7.isSolution().equals("\u2713\u2713\u2713"));
        
        a7.leftClick(5,0);
        a7.leftClick(6,0);
        // the clashing bulbs are at 6,0 and 6,2
        assertTrue("", a7.isSolution().substring(0,19).equals("Clashing bulb at 6,"));
        assertTrue("", "02".contains(a7.isSolution().charAt(19) + ""));
        a7.leftClick(5,0);
        a7.leftClick(6,0);
        // solution restored
        assertTrue("", a7.isSolution().equals("\u2713\u2713\u2713"));
    }
}
