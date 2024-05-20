import javax.swing.JFrame;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Main extends JFrame
{
    // private static final String BOARD_URL = "./board.png";

	private static final int F_WIDTH = 600;
	private static final int F_HEIGHT = 600;
	private static final int BOARD_SIZE = 3;
    
    // Variable to track which player's turn it is
    // - 0: player 1, 1: player 2
    private static int turn;
    private int movesCount;
    
    private Box[][] boxes;
    private JPanel board;
    private JLabel conclusion;
    private Border defaultBorder;
    
    public Main()
    {
    	setTitle("Tic-Tac-Toe");
    	setSize(F_WIDTH, F_HEIGHT);
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	setLocationRelativeTo(null);
    	setLayout(new BorderLayout());
    	
    	defaultBorder = BorderFactory.createEmptyBorder(20, 20, 20, 20);

    	setVisible(true);
    }
    
    public void start()
    {
    	board = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE, 20, 20));
    	board.setBorder(defaultBorder);
    	conclusion = new JLabel("Click on the board above to start playing");
    	conclusion.setHorizontalAlignment(JLabel.CENTER);
    	conclusion.setFont(new Font("Consolas", Font.PLAIN, 14));
    	conclusion.setBorder(defaultBorder);
    	
    	add(board, BorderLayout.CENTER);
    	add(conclusion, BorderLayout.SOUTH);
    	
    	turn = 0;
    	movesCount = 0;
    	boxes = new Box[BOARD_SIZE][BOARD_SIZE];
    	
    	for (int x = 0; x < BOARD_SIZE; x++)
    	{
    		for (int y = 0; y < BOARD_SIZE; y++)
    		{
    			boxes[x][y] = new Box(x, y);
    			Box box = boxes[x][y];
    			
    			box.addMouseListener(new MouseAdapter() {
    				public void mouseClicked(MouseEvent me)
    				{
    					if (box.getState() != Box.State.BLANK)
    					{
    						return;
    					}
    					
    					movesCount++;
    					box.setState(turn);
    					
    					checkWinner(box.getPos_x(), box.getPos_y(), box.getState());
    					turn = turn == 0 ? 1 : 0;
    				}
    			});
    			board.add(box);
    		}
    	}
    }
    
    private void checkWinner(int x, int y, Box.State state)
    {
    	// Check diagonal
    	if (x == y)
    	{
    		for (int i = 0; i < BOARD_SIZE; i++)
    		{
    			if (boxes[i][i].getState() != state)
    			{
    				break;
    			}
    			
    			if (i == BOARD_SIZE - 1)
    			{
    				declareResult(state);
    			}
    		}
    	}
    	
    	// Check Anti-diagonal
    	if (x + y == BOARD_SIZE - 1)
    	{
    		for (int i = 0; i < BOARD_SIZE; i++)
    		{
    			if (boxes[i][BOARD_SIZE - 1 - i].getState() != state)
    			{
    				break;
    			}
    			
    			if (i == BOARD_SIZE - 1)
    			{
    				declareResult(state);
    			}
    		}
    	}
    	
    	// Check rows
    	for (int i = 0; i < BOARD_SIZE; i++)
    	{
    		if (boxes[x][i].getState() != state)
    		{
    			break;
    		}
    		
    		if (i == BOARD_SIZE - 1)
    		{
    			declareResult(state);
    		}
    	}
    	
    	// Check columns
    	for (int i = 0; i < BOARD_SIZE; i++)
    	{
    		if (boxes[i][y].getState() != state)
    		{
    			break;
    		}
    		
    		if (i == BOARD_SIZE - 1)
    		{
    			declareResult(state);
    		}
    	}
    	
    	if (movesCount == BOARD_SIZE * BOARD_SIZE)
    	{
    		declareResult(Box.State.BLANK);
    	}
    }
    
    private void declareResult(Box.State state)
    {
    	String text;
    	switch (state)
    	{
    	case X:
    		text = "Player 1 is the winner!";
    		break;
    	case O:
    		text = "Player 2 is the winner!";
    		break;
    	case BLANK:
    		text = "It's a tie!";
    		break;
    	default:
    		text = "An error has occurred.";
    			
    	}
    	
    	for (Box[] row : boxes)
    	{
    		for (Box box : row)
    		{
    			if (box.getState() == Box.State.BLANK)
    			{    				
    				box.setState(-1);
    			}
    		}
    	}
    	
    	conclusion.setText(text);
    	conclusion.setFont(new Font("Consolas", Font.BOLD, 20));
    }
    
    public static void main(String[] args)
    {
    	Main app = new Main();
    	app.start();
    }
}

class Box extends JLabel
{
	public static enum State{BLANK, X, O, DISABLED};
	private static final String[] URLS = {"./cross.png", "./circle.png", "./grey-square.png", "./aegean.jpeg"};
	
	private State state;
	
	// Position on board
	private int pos_x, pos_y;
	
	public Box(int pos_x, int pos_y)
	{
		super("", JLabel.CENTER);
		this.state = State.BLANK;
		this.pos_x = pos_x;
		this.pos_y = pos_y;
		this.setImage(URLS[3]);
	}
	
	private final void setImage(String url)
	{
		setIcon(new ImageIcon(url));
	}
	
	public final void setState(int player)
	{
		if (player == 0)
		{
			this.state = State.X;
		}
		else if (player == 1)
		{
			this.state = State.O;
		}
		else
		{
			this.state = State.DISABLED;
			
			return;
		}
		this.setImage(URLS[player]);
	}
	
	public final State getState()
	{
		return this.state;
	}

	public int getPos_x()
	{
		return pos_x;
	}

	public void setPos_x(int pos_x)
	{
		this.pos_x = pos_x;
	}

	public int getPos_y()
	{
		return pos_y;
	}

	public void setPos_y(int pos_y)
	{
		this.pos_y = pos_y;
	}
}