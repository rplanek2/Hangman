import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import java.awt.Color;
import java.awt.Font;




public class Hangman {
	//Instance Variables
	private final String[] secretWords = {"beef", "bratwurst", "six", "hello"};//These will be the secret words 
	private String secretWord;
	private String[] holder = new String[30];
	private final String[] hangman = {"0","/","|","\\","/","\\"};//hangman characters
	private final int[] hangmanPos = {31,51,52,53,74,75};//position of characters
	private int nooseFlag = 0;//one way flag for noose drawing
	private int loser;
	
	//Swing objects
	private JFrame frame;
	private JTextField letterField;
	private JTextField secretHolder;
	private JTextArea gameField;
	private JTextArea guessedField;
	private JButton startGame;
	//Launch the application.
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Hangman window = new Hangman();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	//Create the application
	public Hangman() {
		initialize();
	}

	
	//Initialize the contents of the frame.
	private void initialize() {
		//JFrame 
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.BLACK);
		frame.setBounds(100, 100, 609, 423);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		//Title displaying hangman
		JLabel title = new JLabel("Hangman");
		title.setFont(new Font("Comic Sans MS", Font.PLAIN, 42));
		title.setForeground(Color.WHITE);
		title.setBounds(161, -12, 257, 67);
		frame.getContentPane().add(title);
		
		//where the noose and hangman are
		gameField = new JTextArea();
		gameField.setBounds(43, 84, 424, 130);
		frame.getContentPane().add(gameField);
		
		//where the guessed letters are
		guessedField = new JTextArea();
		guessedField.setBounds(24, 341, 543, 22);
		frame.getContentPane().add(guessedField);
		
		//where you enter the letter to be guessed
		letterField = new JTextField();
		letterField.setBounds(24, 287, 116, 38);
		frame.getContentPane().add(letterField);
		letterField.setColumns(10);
		
		//where the secret word is held
		secretHolder = new JTextField();
		secretHolder.setForeground(Color.BLACK);
		secretHolder.setBounds(44, 236, 423, 38);
		frame.getContentPane().add(secretHolder);
		secretHolder.setColumns(10);
		
		//Button that starts game
		startGame = new JButton("Start Game");
		startGame.setBounds(43, 55, 423, 25);
		frame.getContentPane().add(startGame);
		
		//listener/action that starts the game by choosing a random word and drawing the noose 
		startGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try
				{
					//sets losing counter
					loser = 0;
					
					//chooses secret word and sets up a string of "_" in secretHolder
					secretWord = secretWords[(int)(Math.random()*4)];//The number is the range of values(CHANGE TO 20 when you add more words)
					secretHolder.setText(createSecretHolder(secretWord.length()));
					
					//Makes sure noose is only drawn once
					if(nooseFlag==0){
						gameField.append(drawNoose());
						nooseFlag++;
					}
					//Clears guesses
					guessedField.setText(null);
				}
				//catches errors on startup
				catch(Exception startGameError)
				{
					JOptionPane.showMessageDialog(null,startGameError);

				}
			}
		});
		
		//button that listens for letter submissions
		JButton submit = new JButton("Submit Letter");
		submit.setBounds(161, 294, 125, 25);
		frame.getContentPane().add(submit);
		
	
		
		//Performs the actions when a letter is submitted
		submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try
				{
					String guess = letterField.getText();

					if(guess.length()>1)
						JOptionPane.showMessageDialog(null, "One Letter Only!");
					
					else if(guessedField.getText().contains(guess))
						JOptionPane.showMessageDialog(null, "You already guessed that");

					else if(secretWord.indexOf(guess)<0){
						guessedField.append(guess);
						gameField.insert(hangman[loser],hangmanPos[loser]);//draws hangman
						loser++;
						if(loser == 6){
							JOptionPane.showMessageDialog(null, "You Lose");
							startGame.doClick();//restarts game after loss
						}
					}
					
					else{
						guessedField.append(guess);
						secretHolder.setText(modifyHolder(guess,secretWord,secretWord.length()));
						//checks for winner
						for(int i=0;i<secretWord.length();i++){
							if(holder[i]=="_"){
								break;
							}
							else if(holder[i] != "_" && i== secretWord.length()-1){
								JOptionPane.showMessageDialog(null, "You Won!");
								startGame.doClick();
							}
						}
					}
				}
				//catches errors on letter submissions
				catch(Exception inGameError)
				{
					JOptionPane.showMessageDialog(null,inGameError);

				}
			}
		});	
	}
	
	//Various methods used in the program are below
	
	
	// draws hangman noose
	public String drawNoose(){
		String noose = "___________\n" +
				        " |---------------- \n" +
				        " |---------------- \n" +
				        " |----------------\n" +
				        "___";	
		return noose;
	}	
	
	
	//creates a placeholder string of "_"
	public String createSecretHolder(int len){
		String display = "";//display string 
		for(int i=0;i<len;i++){
			holder[i] = "_";
			
			display += "_ ";
		}
		return display;
	}
	
	//modifies holder to include letters
	public String modifyHolder(String c, String word, int len){
		char letter = c.charAt(0);
		//repeat array stores a one if the character exists at that position in the string
		int[] repeat = new int[30];
		for(int i=0;i<len;i++){
			if(letter == word.charAt(i))
				repeat[i] = 1;
		}
		
		String display = ""; //display string 
		
		//changes the holder array to the char and modifies the display string
		for(int i = 0;i<len;i++){
			if(repeat[i]>0)
				holder[i] = c;
			
			display += holder[i] + " ";
			
		}
		return display;
	}	
}