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
		ladder1 = getWordLadderDFS(words.get(0), words.get(1)); 
		
		for (String item: ladder1){
			System.out.println(item);
		}
		
		
		// 4. BFS Word Ladder
		// 5. Print the best word ladder
		
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
		System.out.println("Enter Commands: "); 
		String read = keyboard.nextLine();
		
		// Parse the input commands
		String[] pieces = read.split(" +"); 
		
		if (pieces[0].equals("/quit")) {
			System.out.println("Quitting"); 
			return  Words; 
		}
		else {
			Words.add(0, pieces[0].toLowerCase()); 
			Words.add(1, pieces[1].toLowerCase()); 
			return Words; 
		}
	}
	
	public static ArrayList<String> getWordLadderDFS(String start, String end) {
		// Returned list should be ordered start to end.  Include start and end.
		// If ladder is empty, return list with just start and end.
		ArrayList<String> wordLadder = new ArrayList<String>(); // Where we will store the word ladder
		ArrayList<String> searched = new ArrayList<String>(); // Already visited
		ArrayList<String> ladder = new ArrayList<String>(); // Word ladder to find our string
		
		findAdjacentNodes(start);
		wordLadder = searchDFS(start, end, ladder, searched); 
		
//		System.out.println("WORD LADDER +++++++++++++++++++++++++++++++++++++++++++++++++++"); 
//		for (String words : wordLadder) {
//			System.out.println("Word Ladder: "+words);
//		}
		return wordLadder;
	}
	
	
	
	
    public static ArrayList<String> getWordLadderBFS(String start, String end) {
		
		// TODO some code
		
		return null; // replace this line later with real return
	}
    
	
	public static void printLadder(ArrayList<String> ladder) {
		
	}

	// PRIVATE HELPER METHODS ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	
	private static ArrayList<String> searchDFS(String current, String end, ArrayList<String> ladder, ArrayList<String> Searched){
		ArrayList<String> check; 
		
		// End Condition: we have found our word
		if(current.toLowerCase().equals(end)) { 
			ladder.add(current); 
			return ladder; 
		}
		
		// If not found, find all adjacent words to current and test
		ArrayList<String> adjacent = findAdjacentNodes(current); // All words one letter off of current
		Searched.add(current); 
		
		// For each word one letter off, search it's adjacent words
		for (String next : adjacent) {
			if (! Searched.contains(next.toUpperCase())) {
				check = searchDFS(next, end, ladder, Searched); 
				if (!check.isEmpty()) {
					
					// Remove unnecessary steps in our word ladder. If present in both adjacent lists, we can safely remove
					if ( check.size() > 2 && (adjacent.contains(check.get(check.size()-1)) && adjacent.contains(check.get(check.size()-2)))){
						check.remove(check.size() -1); 
					}
					ladder.add(current); 
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
		// DEBUGGING ONLY +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//		System.out.println("Attempting: "); 
//		for (String item : oneOff) {
//			System.out.println("DEBUGGING findAdjacentNodes: "+ item); // DEBUGGING ONLYw
//		}
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
