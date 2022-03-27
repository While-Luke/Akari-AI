/**
 * AkariViewer represents an interface for a player of Akari.
 *
 * @author Luke Ellis (23099644) and Tom Ellis (23069575)
 * @version 2021
 */
import java.awt.*;
import java.awt.event.*; 

public class AkariViewer implements MouseListener
{    
    private Akari puzzle;    // the internal representation of the puzzle
    private SimpleCanvas sc; // the display window
    final int CANVAS_SIZE = 1000;
    
    boolean[][] crosses; //EXTENSION
    
    /**
     * Constructor for objects of class AkariViewer.
     * Sets all fields and displays the initial puzzle.
     */
    public AkariViewer(Akari puzzle)
    {
        this.puzzle = puzzle;
        sc = new SimpleCanvas("Best_Akari", CANVAS_SIZE*14/10, CANVAS_SIZE, Color.WHITE);
        sc.addMouseListener(this);
        crosses = new boolean[puzzle.getSize()][puzzle.getSize()];
        displayPuzzle();
    }
    
    /**
     * Selects from among the provided files in folder Puzzles. 
     * The number xyz selects pxy-ez.txt. 
     */
    public AkariViewer(int n)
    {
        this(new Akari("Puzzles/p" + n / 10 + "-e" + n % 10 + ".txt"));
    }
    
    /**
     * Uses the provided example file on the LMS page.
     */
    public AkariViewer()
    {
        this(77);
    }
    
    /**
     * Returns the internal state of the puzzle.
     */
    public Akari getPuzzle()
    {
        return puzzle;
    }
    
    /**
     * Returns the canvas.
     */
    public SimpleCanvas getCanvas()
    {
        return sc;
    }
    
    /**
     * Displays the initial puzzle; see the LMS page for a suggested layout. 
     */
    private void displayPuzzle()
    {
        int size = puzzle.getSize();
        for(int y = 0; y < size; y++){
            for(int x = 0; x < size; x++){
                drawSquare(puzzle.getBoard(y, x), x, y); //draws squares from board
            }
        }
        
        for(int i = 0; i <= size; i++){
            sc.drawLine((i*CANVAS_SIZE)/size, 0, (i*CANVAS_SIZE)/size, CANVAS_SIZE, Color.BLACK); //draw grid
            sc.drawLine(0, (i*CANVAS_SIZE)/size, CANVAS_SIZE, (i*CANVAS_SIZE)/size, Color.BLACK);
        }
        
        for(int y = 0; y < size; y++){
            for(int x = 0; x < size; x++){
                if(crosses[y][x]){
                    sc.drawLine((x*CANVAS_SIZE)/size, (y*CANVAS_SIZE)/size, ((x+1)*CANVAS_SIZE)/size, ((y+1)*CANVAS_SIZE)/size, Color.RED); //draw crosses
                    sc.drawLine(((x+1)*CANVAS_SIZE)/size, (y*CANVAS_SIZE)/size, (x*CANVAS_SIZE)/size, ((y+1)*CANVAS_SIZE)/size, Color.RED);
                }
            }
        }
        
        sc.setFont(new Font("TimesRoman", Font.PLAIN, 100)); //draw buttons
        sc.drawRectangle(1050,40,1350,240, Color.BLACK);
        sc.drawString("Clear", 1070, 170, Color.WHITE);
        
        sc.drawRectangle(1050,280,1350,480, Color.BLACK);
        sc.drawString("Solve", 1070, 410, Color.WHITE);
        
        sc.drawRectangle(1050,520,1350,720, Color.BLACK);
        sc.drawString("Check", 1060, 645, Color.WHITE);
        
        sc.drawRectangle(1050,760,1350,960, Color.BLACK);
    }
    
    /**
     * Displays a square based on the contents of the board at the inputted coordinates.
     */
    private void drawSquare(Space space, int x, int y){
        int size = puzzle.getSize();
        int x1 = (x*CANVAS_SIZE)/size;
        int y1 = (y*CANVAS_SIZE)/size;
        int x2 = ((x+1)*CANVAS_SIZE)/size;
        int y2 = ((y+1)*CANVAS_SIZE)/size;
        sc.setFont(new Font("TimesRoman", Font.PLAIN, CANVAS_SIZE/(2*size)));
        
        if(space == Space.EMPTY && !puzzle.canSeeBulb(y,x))sc.drawRectangle(x1,y1,x2,y2, Color.WHITE);
        if((space == Space.EMPTY && puzzle.canSeeBulb(y,x)) || space == Space.BULB)sc.drawRectangle(x1,y1,x2,y2, Color.YELLOW);
        
        if(space == Space.BULB && !puzzle.canSeeBulb(y,x))sc.drawString("💡", x1 + (CANVAS_SIZE/(4*size)),y1 + ((9*CANVAS_SIZE)/(14*size)), Color.GREEN);
        if(space == Space.BULB && puzzle.canSeeBulb(y,x))sc.drawString("💡", x1 + (CANVAS_SIZE/(4*size)),y1 + ((9*CANVAS_SIZE)/(14*size)), Color.RED);
        
        if(space.ordinal() <= 5){
            sc.drawRectangle(x1,y1,x2,y2, Color.BLACK);
            if(space.ordinal() >= 1){
                sc.drawString(space.ordinal()-1, x1 + ((3*CANVAS_SIZE)/(8*size)),y1 + ((9*CANVAS_SIZE)/(14*size)), Color.WHITE);
            }
        }
    }
    
    /**
     * Performs a left click on the square at r,c if the indices are legal, o/w does nothing. 
     * Updates both puzzle and the display. 
     */
    public void leftClick(int r, int c){
        if(puzzle.isLegal(r,c)){
            puzzle.leftClick(r,c);
            displayPuzzle();
        }
    }
    
    public void mousePressed (MouseEvent e) {
        if(e.getX() > CANVAS_SIZE){
            if(e.getX() > 1050 && e.getX() < 1350 && e.getY() > 40 && e.getY() < 240) puzzle.clear();
            if(e.getX() > 1050 && e.getX() < 1350 && e.getY() > 280 && e.getY() < 480) puzzle.solve();
            displayPuzzle();
            if(e.getX() > 1050 && e.getX() < 1350 && e.getY() > 520 && e.getY() < 720) {
                sc.setFont(new Font("TimesRoman", Font.PLAIN, 26));
                sc.drawString(puzzle.isSolution(), 1070, 870, Color.WHITE);
                
                if(puzzle.isSolution() != "✓✓✓"){ //highlight error square
                    int r = Integer.parseInt(puzzle.getCoordinates().split(",")[0]);
                    int c = Integer.parseInt(puzzle.getCoordinates().split(",")[1]);
                    int size = puzzle.getSize();
                    int c1 = (c*CANVAS_SIZE)/size;
                    int r1 = (r*CANVAS_SIZE)/size;
                    int c2 = ((c+1)*CANVAS_SIZE)/size;
                    int r2 = ((r+1)*CANVAS_SIZE)/size;
                    sc.drawRectangle(c1,r1,c2,r2, Color.CYAN);
                    sc.setFont(new Font("TimesRoman", Font.PLAIN, CANVAS_SIZE/(2*size)));
                    if(puzzle.getBoard(r,c) == Space.BULB)sc.drawString("💡", c1 + (CANVAS_SIZE/(4*size)), r1 + ((9*CANVAS_SIZE)/(14*size)), Color.RED);
                    if(puzzle.getBoard(r,c).ordinal() <= 5){
                        sc.drawString(puzzle.getBoard(r,c).ordinal()-1, c1 + ((3*CANVAS_SIZE)/(8*size)), r1 + ((9*CANVAS_SIZE)/(14*size)), Color.WHITE);
                    }
                }
            }
        }
        else{
            int r = (e.getY()*puzzle.getSize())/CANVAS_SIZE;
            int c = (e.getX()*puzzle.getSize())/CANVAS_SIZE;
            if(e.getButton() == MouseEvent.BUTTON1){
                leftClick(r,c);
            }
            else{
                if(puzzle.getBoard(r,c).ordinal() > 5){ //right-click places a cross
                    crosses[r][c] = !crosses[r][c];
                    displayPuzzle();
                }
            }
        }
    }
    public void mouseClicked (MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered (MouseEvent e) {}
    public void mouseExited  (MouseEvent e) {}
}

