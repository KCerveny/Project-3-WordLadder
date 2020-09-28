/* WORD LADDER Main.java
 * EE422C Project 3 submission by
 * Replace <...> with your actual data.
 * <Student1 Name>
 * <Student1 EID>
 * <Student1 5-digit Unique No.>
 * <Student2 Name>
 * <Student2 EID>
 * <Student2 5-digit Unique No.>
 * Slip days used: <0>
 * Git URL:
 * Fall 2020
 */


package assignment3;
import java.util.*;
import java.io.*;

public class Main {
	
	// static variables and constants only here.
	static ArrayList<String> words;
	public static Set<String> dictionary; // Storage of hashed dict from infile
	private static char[] alphabet = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
	
	public static void main(String[] args) throws Exception {
		
		Scanner kb;	// input Scanner for commands
		PrintStream ps;	// output file, for student testing and grading only
		// If arguments are specified, read/write from/to files instead of Std IO.
		if (args.length != 0) {
			kb = new Scanner(new File(args[0]));
			ps = new PrintStream(new File(args[1]));
			System.setOut(ps);			// redirect output to ps
		} else {
			kb = new Scanner(System.in);// default input from Stdin
			ps = System.out;			// default output to Stdout
		}
		
		// 1. Initialize static Variables
		initialize();
		
		// 2. Receive and format input from the keyboard
		words = parse(kb); 
		if (words.isEmpty()) {
			return; // given command was "/quit"
		}
		
		// 3. DFS Word Ladder
		ArrayList<String> ladder1 = new ArrayList<String>();
//		ladder1 = getWordLadderDFS(words.get(0), words.get(1)); 
				
		// 4. BFS Word Ladder
		ArrayList<String> ladder2 = new ArrayList<String>(); 
		ladder2 = getWordLadderBFS(words.get(0), words.get(1)); 		
		
		// 5. Print the best word ladder
//		printLadder(ladder2); 
		
	}
	
	public static void initialize() {
		// initialize your static variables or constants here.
		// We will call this method before running our JUNIT tests.  So call it 
		// only once at the start of main.
		
		words = new ArrayList<String>();
		dictionary = makeDictionary(); 

	}
	
	/**
	 * @param keyboard Scanner connected to System.in
	 * @return ArrayList of Strings containing start word and end word. 
	 * If command is /quit, return empty ArrayList. 
	 */
	public static ArrayList<String> parse(Scanner keyboard) {
		
		ArrayList<String> Words = new ArrayList<String>();		
		String read = keyboard.nextLine();
		
		// Parse the input commands
		String[] pieces = read.split(" +"); 
		
		if (pieces[0].equals("/quit")) { 
			return  Words; 
		}
		else {
			Words.add(0, pieces[0].toLowerCase()); 
			Words.add(1, pieces[1].toLowerCase()); 
			return Words; 
		}
	}
	
	public static ArrayList<String> getWordLadderDFS(String start, String end) {
		ArrayList<String> wordLadder = new ArrayList<String>(); // Where we will store the word ladder
		ArrayList<String> searched = new ArrayList<String>(); // Already visited
		ArrayList<String> ladder = new ArrayList<String>(); // Word ladder to find our string
		
//		findAdjacentNodes(start);
		wordLadder = searchDFS(end, start, ladder, searched); 
		
		// Case where we could not find a word ladder
		if (wordLadder.isEmpty()) {
			wordLadder.add(start); 
			wordLadder.add(end);
		}
		
		return wordLadder;
	}
			
    public static ArrayList<String> getWordLadderBFS(String start, String end) {
    	ArrayList<String> wordLadder = new ArrayList<String>(); // Ladder to eval and return
    	ArrayList<String> searchNext = new ArrayList<String>(); // Queue of nodes to search
    	ArrayList<String> ladder = new ArrayList<String>(); // Argument, to be appended to 
    	ArrayList<String> discovered = new ArrayList<String>(); 
    	
    	searchBFS(start, end, ladder, discovered, searchNext);  
    	wordLadder = discovered; 
		
		return wordLadder; // replace this line later with real return
	}
    
	
	public static void printLadder(ArrayList<String> ladder) {
		// If word ladder only contains start and end words, could not find
		if (ladder.size() == 2) {
			System.out.println("no word ladder can be found between " + ladder.get(0) + " and " + ladder.get(1) + ".");
		}
		else {
			System.out.print("a " + ladder.size() + "-rung word ladder exists between " );
			System.out.println(ladder.get(0) + " and " + ladder.get(ladder.size()-1) + ".");
			
			for (String item: ladder){
				System.out.println(item);
			}
		}
		
	}

	// PRIVATE HELPER METHODS ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	
	private static void searchBFS(String current, String end, ArrayList<String> ladder, ArrayList<String> discovered, ArrayList<String> nodeQueue){
		ArrayList<String> toAdd;
		nodeQueue.add(current); 
		discovered.add(current); 
		
		while(! nodeQueue.isEmpty()) {
			String check =  nodeQueue.remove(0); 
			System.out.println(check); 
			toAdd = findAdjacentNodes(check); 
			
			for (String item : toAdd) {
				if (!end.equals(item.toLowerCase()) &&  !discovered.contains(item)) {
					nodeQueue.add(item); 
					discovered.add(item); 
				}
			}
		}
		
//		ArrayList<String> toAdd; 
//		String test; // String which we will be comparing to end and evaluating partners of
//		
//		discovered.add(current); // We have visited our current node 
//		
//		// Base Case: we have found our end node
//		if(current.toLowerCase().equals(end)) {
//			ladder.add(current); // Add the target to the word ladder
//		}
//		
//		// If word is not found, add all UNVISITED nodes at the same level
//		toAdd = findAdjacentNodes(current); 
//		for (String item : toAdd) {
//			if (! discovered.contains(item)) {
//				nodeQueue.add(item); 
//			}
//		}
//
//		// Add all next nodes to the queue
//		while (! nodeQueue.isEmpty()) {
//			test = nodeQueue.remove(0); // Pop a String off of the Queue
//			discovered.add(test); 
//		}
//		
//		// Add current node if we have found the end node through this branch
//		if (! ladder.isEmpty()) {
//			ladder.add(current); 
//		}
	}
	
	
	
	
	private static ArrayList<String> searchDFS(String current, String end, ArrayList<String> ladder, ArrayList<String> Searched){
		ArrayList<String> check; 
		
		// End Condition: we have found our word
		if(current.toLowerCase().equals(end)) { 
			ladder.add(current.toLowerCase()); 
			return ladder; 
		}
		
		// If not found, find all adjacent words to current and test
		Searched.add(current); 
		ArrayList<String> adjacent = findAdjacentNodes(current); // All words one letter off of current
		
		
		// For each word one letter off, search it's adjacent words
		for (String next : adjacent) {
			if (! Searched.contains(next.toUpperCase())) {
				check = searchDFS(next, end, ladder, Searched); 
				if (!check.isEmpty()) {
					
					// Remove unnecessary steps in our word ladder. If present in both adjacent lists, we can safely remove
					if ( check.size() > 2 && (adjacent.contains(check.get(check.size()-1).toUpperCase()) && adjacent.contains(check.get(check.size()-2).toUpperCase()))){
						check.remove(check.size() -1); 
					}
					ladder.add(current.toLowerCase()); 
					return ladder; 
				}
			}
		}
		return ladder; 
	}

	// Returns: ArrayList of Strings which are one character different from argument givenWord
	private static ArrayList<String> findAdjacentNodes(String givenWord){
		ArrayList<String> oneOff = new ArrayList<String>(); // Store all words in dictionary one letter off given
		String testWord; 
		
		// Search each word one letter off of givenWord
		// For each letter in givenWord
		for (int i = 0; i<givenWord.length(); i++) { 
			
			// Replace with each letter, check if new word is in dictionary
			for (char letter : alphabet) {
				
				// Make sure this is not the same word as givenWord
				if ( letter != givenWord.charAt(i) ) {
					testWord = givenWord.replace(givenWord.charAt(i), letter).toUpperCase(); 
					if (dictionary.contains(testWord)) {
						oneOff.add(testWord); // If valid word, testWord is a new node to explore
					}
				}
			}
		}
		return oneOff; 
	}

	/* Do not modify makeDictionary */
	public static Set<String>  makeDictionary () {
		Set<String> words = new HashSet<String>();
		Scanner infile = null;
		try {
			infile = new Scanner (new File("five_letter_words.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Dictionary File not Found!");
			e.printStackTrace();
			System.exit(1);
		}
		while (infile.hasNext()) {
			words.add(infile.next().toUpperCase());
		}
		return words;
	}
}
