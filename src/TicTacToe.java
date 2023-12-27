import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import java.util.Random;
import java.util.Scanner;

public class TicTacToe extends JFrame implements ActionListener { //This will create our board setup
	private JSlider slider;
	private JButton oButton, xButton;
	private Board board;
	private int lineThickness=5,ch;
	private Color oColor=Color.BLACK, xColor=Color.ORANGE;
	static char BLANK=' ', O='O', X='X';
	
	private char position[]={  // This signifies the board position
	BLANK, BLANK, BLANK,
	BLANK, BLANK, BLANK,
	BLANK, BLANK, BLANK};

// This will begin the game
public static void main(String args[]) {
	new TicTacToe();
	}

// This will allow the player a choice and will initialize it
public TicTacToe() {
	super("Tic Tac Toe Game");
	System.out.println("Select Your Character");
	System.out.println("Enter 0 for O or 1 for X:");
	ch = new Scanner(System.in).nextInt();
	
	oButton=new JButton("O Color");
	xButton=new JButton("X Color");
	oButton.addActionListener(this);
	xButton.addActionListener(this);
	
	add(board=new Board(), BorderLayout.CENTER);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setSize(500, 500);
	setVisible(true);
	}

// Change color of O or X
public void actionPerformed(ActionEvent e) {
if (e.getSource()==oButton) {
Color newColor = JColorChooser.showDialog(this, "Choose a new color for O", oColor);
if (newColor!=null)
oColor=newColor;
}
else if (e.getSource()==xButton) {
Color newColor = JColorChooser.showDialog(this, "Choose a new color for X", xColor);
if (newColor!=null)
xColor=newColor;
}
board.repaint();
}


private class Board extends JPanel implements MouseListener {// Board is what will play and show the game
	private Random random=new Random();
	private int rows[][]={{0,2},{3,5},{6,8},{0,6},{1,7},{2,8},{0,8},{2,6}};//Endpoints of the rows in position. Across, down, and diagonally)

public Board() {
	addMouseListener(this);
	}

// The board will be drawn
public void paintComponent(Graphics graph) {
	super.paintComponent(graph);
	int wid = getWidth();
	int ht = getHeight();
	Graphics2D grid = (Graphics2D) graph;

	// This will create the grid
	grid.setPaint(Color.WHITE);
	grid.fill(new Rectangle2D.Double(0, 0, wid, ht));
	grid.setPaint(Color.BLACK);
	grid.setStroke(new BasicStroke(lineThickness));
	
	grid.draw(new Line2D.Double(0, ht/3, wid, ht/3));
	grid.draw(new Line2D.Double(0, ht*2/3, wid, ht*2/3));
	grid.draw(new Line2D.Double(wid/3, 0, wid/3, ht));
	grid.draw(new Line2D.Double(wid * 2/3, 0, wid*2/3, ht));

	// This will actually draw the xs and os
	for (int i=0; i<9; ++i) {
		double xon=(i%3+0.5)*wid/3.0;
		double yon=(i/3+0.5)*ht/3.0;
		
		double xle=wid/8.0;
		double yle=ht/8.0;
		if (position[i]==O) {
			grid.setPaint(oColor);
			grid.draw(new Ellipse2D.Double(xon-xle, yon-yle, xle*2, yle*2));
			}
		else if (position[i]==X) {
			grid.setPaint(xColor);
			grid.draw(new Line2D.Double(xon-xle, yon-yle, xon+xle, yon+yle));
			grid.draw(new Line2D.Double(xon-xle, yon+yle, xon+xle, yon-yle));
				}
			}
		}

//Wherever the mouse is clicked is where the o is placed
public void mouseClicked(MouseEvent me) {
	int xon=me.getX()*3/getWidth();
	int yon=me.getY()*3/getHeight();
	
	int pos=xon+3*yon;
	if (pos>=0 && pos<9 && position[pos]==BLANK) {
		if(ch==0)
			position[pos]=O;
	else
		position[pos]=X;
		repaint();
		putX(); // computer plays
		repaint();
		}
	}

//Disregard the unwanted mouse events
public void mousePressed(MouseEvent me) {}
public void mouseReleased(MouseEvent me) {}
public void mouseEntered(MouseEvent me) {}
public void mouseExited(MouseEvent me) {}

// The opponent or computer will play
void putX() {
// This will see if the game is finished
	if (won(O))
		newGame(O);
	else if (isDraw())
		newGame(BLANK);
	else {
		nextMove();
		if (won(X))
			newGame(X);
		else if (isDraw())
			newGame(BLANK);
		}
	}

// True if the player has won the game
	boolean won(char player) {
	for (int i=0; i<8; ++i)
		if (GameRow(player, rows[i][0], rows[i][1]))
			return true;
			return false;
		}

// This will see if the player has won the row
	boolean GameRow(char player, int a, int b) {
	return position[a]==player && position[b]==player && position[(a+b)/2]==player;
	}


void nextMove() {//Finish a row and win or attempt to stop o. Or make random plays
	int t=findRow(X); 
	if (t<0)
		t=findRow(O); 
	if (t<0) { 
		do
			t=random.nextInt(9);
	while (position[t]!=BLANK);
		}
	if(ch==0)
		position[t]=X;
	else
		position[t]=O;
	}

	// return the position of the blank spot
	int findRow(char player) {
	for (int i=0; i<8; ++i) {
		int result=find1Row(player, rows[i][0], rows[i][1]);
	if (result>=0)
		return result;
	}
	return -1;
	}

	//Return the index of the blank spot if a player has 2 of 3 hits in the row otherwise return -1
	int find1Row(char player, int a, int b) {
	int c=(a+b)/2; 
	if (position[a]==player && position[b]==player && position[c]==BLANK)
		return c;
	if (position[a]==player && position[c]==player && position[b]==BLANK)
		return b;
	if (position[b]==player && position[c]==player && position[a]==BLANK)
		return a;
		return -1;
	}

	//This will check if all spots are occupied
	boolean isDraw() {
	for (int i=0; i<9; ++i)
		if (position[i]==BLANK)
			return false;
			return true;
	}

// This will start an entirely new game
void newGame(char winner) {
	repaint();
	// Output the result and prompt the user to play again
	String result;
	if ((ch==0 && winner==O) || (ch==1 && winner==X)) {
	result = "You Have Won!";
	}
	else if ((ch==0 && winner==X) || (ch==1 && winner==O)) {
	result = "You've Been Defeated :( ";
	}
	else {
	result = "Tie";
	}
	if (JOptionPane.showConfirmDialog(null,result+" Play again?", "Game Over",JOptionPane.YES_NO_OPTION)
	!=JOptionPane.YES_OPTION) {
	System.exit(0);
	}

	// This will empty the board for the coming game
	for (int j=0; j<9; ++j)
		position[j]=BLANK;
			}
		} 
	}

