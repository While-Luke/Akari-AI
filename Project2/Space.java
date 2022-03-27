/**
 * Space enumerates the possible states of a square on an Akari board. 
 * enum classes are discussed in Lecture 14, Week 10. 
 *
 * @author Lyndon While
 * @version 2021
 */
public enum Space
{
    BLACK,                       // black no number 
    ZERO, ONE, TWO, THREE, FOUR, // black with number (will always be in this order) 
    EMPTY,                       // empty 
    BULB;                        // bulb 
    
    /**
     * Returns true iff x represents a space on the board 
     * that can be changed during play. 
     */
    public static boolean isMutable(Space x)
    {
        return x == EMPTY || x == BULB;
    }
}
