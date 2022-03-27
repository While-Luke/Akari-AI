/**
 * Akari represents a single puzzle of the game Akari.
 *
 * @author Luke Ellis (23099644) and Tom Ellis (23069575)
 * @version 2021
 */
import java.util.ArrayList; 

public class Akari
{
    private String filename; // the name of the puzzle file
    private int size;        // the board is size x size
    private Space[][] board; // the board is a square grid of spaces of various types

    /**
     * Constructor for objects of class Akari. 
     * Creates and initialises the fields with the contents of filename. 
     * The layout of a puzzle file is described on the LMS page; 
     * you may assume the layout is always correct. 
     */
    public Akari(String filename)
    {
        this.filename = filename;
        FileIO file = new FileIO(filename);
        ArrayList<String> lines = file.getLines();
        size = Integer.parseInt(lines.get(0));
        board = new Space[size][size];
        for(int y = 0; y < size; y++){
            for(int x = 0; x < size; x++){
                board[y][x] = Space.EMPTY;
            }
        }
        
        setBlack(lines.get(1));
        
        for(int i = 2; i < lines.size(); i++){
           setNumbers(lines.get(i), i-2); 
        }
    }
    
    /**
     * Interprets the second line of the file
     */
    private void setBlack(String sBlack)
    {
        for (String coOrd : sBlack.split(" ")){
            int y = parseIndex(coOrd.charAt(0));
            for (int i = 1; i<coOrd.length(); i++){
                board[y][parseIndex(coOrd.charAt(i))] = Space.BLACK;
            }
        }
    }
    
    /**
     * Interprets the third to seventh line of the file
     */
    private void setNumbers(String line, int number){
        if(!line.isEmpty()){
            for(String pair : line.split(" ")){
                int y_coord = parseIndex(pair.charAt(0));
                pair = pair.substring(1);
                for(String x_coords : pair.split("")){
                    board[y_coord][parseIndex(x_coords.charAt(0))] = Space.values()[number+1];
                }
            }
        }
    }
    
    /**
     * Uses the example file from the LMS page.
     */
    public Akari()
    {
        this("Puzzles/p7-e7.txt");
    }
    
    /**
     * Returns the name of the puzzle file.
     */
    public String getFilename()
    {
        return filename;
    }
    
    /**
     * Returns the size of the puzzle.
     */
    public int getSize()
    {
        return size;
    }
    
    /**
     * Returns true iff k is a legal index into the board. 
     */
    public boolean isLegal(int k)
    {
        return k < size && k >= 0; 
    }
    
    /**
     * Returns true iff r and c are both legal indices into the board. 
     */
    public boolean isLegal(int r, int c)
    {
        return isLegal(r) && isLegal(c); 
    }
    
    /**
     * Returns the contents of the square at r,c if the indices are legal, 
     * o/w throws an illegal argument exception. 
     */
    public Space getBoard(int r, int c)
    {
        if(isLegal(r,c)) return board[r][c];
        else throw new IllegalArgumentException("Parameter is out of range");
    }
    
    /**
     * Returns the int equivalent of x. 
     * If x is a digit, returns 0 -> 0, 1 -> 1, etc; 
     * if x is an upper-case letter, returns A -> 10, B -> 11, etc; 
     * o/w throws an illegal argument exception. 
     */
    public static int parseIndex(char x)
    {
        if((x >= 48 &&  x <= 57) || (x >= 65 && x <= 90)){
            if(Character.isDigit(x)) return Character.getNumericValue(x);
            return x - 55;
        }
        else throw new IllegalArgumentException("Parameter is out of range");
    }
    
    /**
     * Performs a left click on the square at r,c if the indices are legal, o/w does nothing. 
     * If r,c is empty, a bulb is placed; if it has a bulb, that bulb is removed.
     */
    public void leftClick(int r, int c)
    {
        if(isLegal(r,c) && Space.isMutable(board[r][c])){
            if(board[r][c] == Space.EMPTY){
                board[r][c] = Space.BULB;
            }
            else{
                board[r][c] = Space.EMPTY;
            }
        }
    }
    
    /**
     * Sets all mutable squares on the board to empty.
     */
    public void clear()
    {
        for(int y = 0; y < size; y++){
            for(int x = 0; x < size; x++){
                if(Space.isMutable(board[y][x])) board[y][x] = Space.EMPTY;
            }
        }
    }
    
    /**
     * Returns the number of bulbs adjacent to the square at r,c. 
     * Throws an illegal argument exception if r,c are illegal coordinates.
     */
    public int numberOfBulbs(int r, int c)
    {
        if(isLegal(r,c)){
            int iBulbs = 0;
            if (isLegal(r, c+1) && board[r][c+1] == Space.BULB) iBulbs ++;
            if (isLegal(r, c-1) && board[r][c-1] == Space.BULB) iBulbs ++;
            if (isLegal(r+1, c) && board[r+1][c] == Space.BULB) iBulbs ++;
            if (isLegal(r-1, c) && board[r-1][c] == Space.BULB) iBulbs ++;
            return iBulbs;
        }
        else throw new IllegalArgumentException("Parameter is out of range");
    }
    
    /**
     * Returns true iff the square at r,c is lit by a bulb elsewhere. 
     * Throws an illegal argument exception if r,c are illegal coordinates.
     */
    public boolean canSeeBulb(int r, int c)
    {
        if(isLegal(r,c)){
            for(int i = r - 1; i >=0; i--){ //check up
               if(board[i][c] == Space.BULB) return true;
               if(board[i][c].ordinal() <= 5) break;
            }
            for(int i = r + 1; i < size; i++){ //check down
               if(board[i][c] == Space.BULB) return true;
               if(board[i][c].ordinal() <= 5) break;
            }
            for(int i = c - 1; i >=0; i--){ //check left
               if(board[r][i] == Space.BULB) return true;
               if(board[r][i].ordinal() <= 5) break;
            }
            for(int i = c + 1; i < size; i++){ //check right
               if(board[r][i] == Space.BULB) return true;
               if(board[r][i].ordinal() <= 5) break;
            }
            return false;
        }
        else throw new IllegalArgumentException("Parameter is out of range");
    }
    
    /**
     * Returns an assessment of the state of the puzzle, either 
     * "Clashing bulb at r,c", 
     * "Unlit square at r,c", 
     * "Broken number at r,c", or
     * three ticks, as on the LMS page. 
     * r,c must be the coordinates of a square that has that kind of error. 
     * If there are multiple errors on the board, you may return any one of them. 
     */
    public String isSolution()
    {
       int square_number;
       for(int y = 0; y < size; y++){
           for(int x = 0; x < size; x++){
               if(board[y][x] == Space.BULB && canSeeBulb(y, x)) return String.format("Clashing bulb at %d,%d", y, x);
               if(board[y][x] == Space.EMPTY && !canSeeBulb(y, x)) return String.format("Unlit square at %d,%d", y, x);
               square_number = board[y][x].ordinal();
               if(square_number > 0 && square_number <= 5){
                    if(square_number-1 != numberOfBulbs(y, x)) return String.format("Broken number at %d,%d", y, x);
               }
           }
       }
       return "✓✓✓";
    }
    
    
    //EXTENSION------------------------------------------------------------------------------------------------------------------------
    
    /**
     * Returns just the coordinates of the square at the error found in isSolution().
     */
    public String getCoordinates()
    {
       int square_number;
       for(int y = 0; y < size; y++){
           for(int x = 0; x < size; x++){
               if(board[y][x] == Space.BULB && canSeeBulb(y, x)) return String.format("%d,%d", y, x);
               if(board[y][x] == Space.EMPTY && !canSeeBulb(y, x)) return String.format("%d,%d", y, x);
               square_number = board[y][x].ordinal();
               if(square_number > 0 && square_number <= 5){
                    if(square_number-1 != numberOfBulbs(y, x)) return String.format("%d,%d", y, x);
               }
           }
       }
       return "✓✓✓";
    }
    
    /**
     * Attempts to solve the puzzle by placing crosses on squares that cannot contain a bulb
     * and placing bulbs where they definitely must go.
     * Warning: The Solver will loop infinitely if it cannot determine a solution.
     */
    public void solve(){
        clear();
        int[][] crossed = new int[size][size]; //0 = possible bulb location, 1 = cross, 2 = wall/number
        for(int y = 0; y < size; y++){
           for(int x = 0; x < size; x++){
               if(board[y][x].ordinal() <= 5){
                   crossed[y][x] = 2;
               }
           }
        }
        int count;
        boolean one, two, cont;
        int r = 0;
        int c = 0;
        int[][] test_crosses = new int[size][size];
        
        while(isSolution() != "✓✓✓"){
            for(int y = 0; y < size; y++){
                for(int x = 0; x < size; x++){
                    if(board[y][x].ordinal() > 0 && board[y][x].ordinal() <= 5){
                       if(board[y][x].ordinal() == 3){ 
                            if(isLegal(y-1,x-1) && board[y-1][x-1].ordinal() == 2){ //2s next to a wall and adjacent to a 1 can place a bulb
                                if(isLegal(x+1) && crossed[y][x+1] != 0) board[y+1][x] = Space.BULB; 
                                if(isLegal(y+1) && crossed[y+1][x] != 0) board[y][x+1] = Space.BULB;
                            }
                            if(isLegal(y-1,x+1) && board[y-1][x+1].ordinal() == 2){
                                if(isLegal(x-1) && crossed[y][x-1] != 0) board[y+1][x] = Space.BULB;
                                if(isLegal(y+1) && crossed[y+1][x] != 0) board[y][x-1] = Space.BULB;
                            }
                            if(isLegal(y+1,x-1) && board[y+1][x-1].ordinal() == 2){
                                if(isLegal(x+1) && crossed[y][x+1] != 0) board[y-1][x] = Space.BULB;
                                if(isLegal(y-1) && crossed[y-1][x] != 0) board[y][x+1] = Space.BULB;
                            }
                            if(isLegal(y+1,x+1) && board[y+1][x+1].ordinal() == 2){
                                if(isLegal(x-1) && crossed[y][x-1] != 0) board[y-1][x] = Space.BULB;
                                if(isLegal(y-1) && crossed[y-1][x] != 0) board[y][x-1] = Space.BULB;
                            }
                       }
                        
                       if(board[y][x].ordinal() == 4){ //3 diagonal to 1 has 2 known bulbs
                            if(isLegal(y-1,x-1) && board[y-1][x-1].ordinal() == 2){
                                board[y+1][x] = Space.BULB;
                                board[y][x+1] = Space.BULB;
                            }
                            if(isLegal(y-1,x+1) && board[y-1][x+1].ordinal() == 2){
                                board[y+1][x] = Space.BULB;
                                board[y][x-1] = Space.BULB;
                            }
                            if(isLegal(y+1,x-1) && board[y+1][x-1].ordinal() == 2){
                                board[y-1][x] = Space.BULB;
                                board[y][x+1] = Space.BULB;
                            }
                            if(isLegal(y+1,x+1) && board[y+1][x+1].ordinal() == 2){
                                board[y-1][x] = Space.BULB;
                                board[y][x-1] = Space.BULB;
                            }
                       }
                        
                       count = 0; //place bulbs around numbers with only one possible orientation of bulbs
                       if(isLegal(x-1) && crossed[y][x-1] == 0) count++;
                       if(isLegal(x+1) && crossed[y][x+1] == 0) count++;
                       if(isLegal(y-1) && crossed[y-1][x] == 0) count++;
                       if(isLegal(y+1) && crossed[y+1][x] == 0) count++;
                       if(board[y][x].ordinal() - 1 == count){
                            if(isLegal(x-1) && crossed[y][x-1] == 0) board[y][x-1] = Space.BULB;
                            if(isLegal(x+1) && crossed[y][x+1] == 0) board[y][x+1] = Space.BULB;
                            if(isLegal(y-1) && crossed[y-1][x] == 0) board[y-1][x] = Space.BULB;
                            if(isLegal(y+1) && crossed[y+1][x] == 0) board[y+1][x] = Space.BULB;
                       }
                        
                       if(board[y][x].ordinal()-1 == numberOfBulbs(y, x)){ //place crosses around numbers with correct number of bulbs
                            if(isLegal(x-1) && board[y][x-1] != Space.BULB && crossed[y][x-1] == 0) crossed[y][x-1] = 1;
                            if(isLegal(x+1) && board[y][x+1] != Space.BULB && crossed[y][x+1] == 0) crossed[y][x+1] = 1;
                            if(isLegal(y-1) && board[y-1][x] != Space.BULB && crossed[y-1][x] == 0) crossed[y-1][x] = 1;
                            if(isLegal(y+1) && board[y+1][x] != Space.BULB && crossed[y+1][x] == 0) crossed[y+1][x] = 1;
                       }
                    }
                    else{
                        if(canSeeBulb(y,x) && crossed[y][x] < 2) crossed[y][x] = 1; //put crosses on squares that can see bulbs
                        if(crossed[y][x] != 2 && board[y][x] != Space.BULB && !canSeeBulb(y,x)){ //if a square can only be lit from exactly one location, place a bulb there
                            one = false;
                            two = false;
                            if(!two){
                                for(int i = x; i >= 0; i--){
                                    if(crossed[y][i] == 0) {
                                        if(one) two = true; 
                                        else {
                                            one = true;
                                            r = y;
                                            c = i;
                                        }
                                    }
                                    if(crossed[y][i] == 2) break;
                                }
                            }
                            if(!two){
                                for(int i = x+1; i < size; i++){
                                    if(crossed[y][i] == 0) {
                                        if(one) two = true; 
                                        else {
                                            one = true;
                                            r = y;
                                            c = i;
                                        }
                                    }
                                    if(crossed[y][i] == 2) break;
                                }
                            }
                            if(!two){
                                for(int i = y-1; i >= 0; i--){
                                    if(crossed[i][x] == 0) {
                                        if(one) two = true; 
                                        else {
                                            one = true;
                                            r = i;
                                            c = x;
                                        }
                                    }
                                    if(crossed[i][x] == 2) break;
                                }
                            }
                            if(!two){
                                for(int i = y+1; i < size; i++){
                                    if(crossed[i][x] == 0) {
                                        if(one) two = true; 
                                        else {
                                            one = true;
                                            r = i;
                                            c = x;
                                        }
                                    }
                                    if(crossed[i][x] == 2) break;
                                }
                            }
                            if(one && !two){ 
                               board[r][c] = Space.BULB;
                            }
                        }
                        
                        if(board[y][x] == Space.EMPTY && crossed[y][x] == 0 && !canSeeBulb(y,x)){ //if placeing a bulb breaks the puzzle, place a cross there instead
                            for(int j = 0; j < size; j++){
                                for(int i = 0; i < size; i++){
                                    test_crosses[j][i] = crossed[j][i]; 
                                }
                            }
                            
                            board[y][x] = Space.BULB;
                            
                            for(int j = 0; j < size; j++){
                                for(int i = 0; i < size; i++){
                                   if(test_crosses[j][i] == 0 && canSeeBulb(j,i)) test_crosses[j][i] = 1; //place new temporary crosses
                                   if(board[j][i].ordinal()-1 == numberOfBulbs(j, i)){
                                        if(isLegal(i-1) && board[j][i-1] != Space.BULB && test_crosses[j][i-1] == 0) test_crosses[j][i-1] = 1;
                                        if(isLegal(i+1) && board[j][i+1] != Space.BULB && test_crosses[j][i+1] == 0) test_crosses[j][i+1] = 1;
                                        if(isLegal(j-1) && board[j-1][i] != Space.BULB && test_crosses[j-1][i] == 0) test_crosses[j-1][i] = 1;
                                        if(isLegal(j+1) && board[j+1][i] != Space.BULB && test_crosses[j+1][i] == 0) test_crosses[j+1][i] = 1;
                                   }
                                }
                            }
                            
                            cont = true;
                            
                            for(int j = 0; j < size; j++){ //if a square cannot be lit then puzzle is broken
                                for(int i = 0; i < size; i++){
                                    if(board[j][i] == Space.EMPTY && !canSeeBulb(j,i)){
                                        one = false;
                                        if(!one){
                                            for(int k = i; k >= 0; k--){
                                                if(test_crosses[j][k] == 0) {one = true; break;}
                                                if(test_crosses[j][k] == 2) break;
                                            }
                                        }
                                        if(!one){
                                            for(int k = i+1; k < size; k++){
                                                if(test_crosses[j][k] == 0) {one = true; break;}
                                                if(test_crosses[j][k] == 2) break;
                                            }
                                        }
                                        if(!one){
                                            for(int k = j-1; k >= 0; k--){
                                                if(test_crosses[k][i] == 0) {one = true; break;}
                                                if(test_crosses[k][i] == 2) break;
                                            }
                                        }
                                        if(!one){
                                            for(int k = j+1; k < size; k++){
                                                if(test_crosses[k][i] == 0) {one = true; break;}
                                                if(test_crosses[k][i] == 2) break;
                                            }
                                        }
                                        if(!one){
                                            crossed[y][x] = 1;
                                            cont = false;
                                        }
                                    }
                                }
                                if(!cont) break;
                            }
                            
                            if(cont){ //if a number cannot be completed then the puzzle is broken
                                for(int j = 0; j < size; j++){
                                    for(int i = 0; i < size; i++){
                                        if(board[j][i].ordinal() > 1 && board[j][i].ordinal() <= 5){
                                            count = 0;
                                            if(isLegal(i-1) && test_crosses[j][i-1] == 0) count++;
                                            if(isLegal(i+1) && test_crosses[j][i+1] == 0) count++;
                                            if(isLegal(j-1) && test_crosses[j-1][i] == 0) count++;
                                            if(isLegal(j+1) && test_crosses[j+1][i] == 0) count++;
                                            if(count < board[j][i].ordinal() - 1){
                                                crossed[y][x] = 1;
                                                cont = false;
                                            }
                                        }
                                    }
                                    if(!cont) break;
                                }
                            }
                            
                            board[y][x] =  Space.EMPTY;
                        }
                    }
                }
            }
        }
    }
}