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
				
		// 4. BFS Word Ladder
//		ArrayList<String> ladder2 = new ArrayList<String>(); 
//		ladder2 = getWordLadderBFS(words.get(0), words.get(1)); 		
		
		// 5. Print the best word ladder
		printLadder(ladder1); 
		
//		ladder1 = findAdjacentNodes("pines"); 
//		for(String word : ladder1) System.out.println(word);	
	}
	
	public static void initialize() {
		// initialize your static variables or constants here.
		// We will call this method before running our JUNIT tests.  So call it 
		// only once at the start of main.
		
		words = new ArrayList<String>();
		//dictionary = makeDictionary();

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
		Set<String> dictionary = makeDictionary();
		
//		findAdjacentNodes(start);
		wordLadder = searchDFS(end, start, ladder, searched, dictionary);
		
		// Case where we could not find a word ladder
		if (wordLadder.isEmpty()) {
			wordLadder.add(start); 
			wordLadder.add(end);
		}
		
		return wordLadder;
	}
			
    public static ArrayList<String> getWordLadderBFS(String start, String end) {
    	ArrayList<String> wordLadder = new ArrayList<String>(); // Ladder to eval and return
    	Node begin = new Node(start, null); // Create starting node in word tree
		Set<String> dictionary = makeDictionary();

    	wordLadder.add(end.toUpperCase()); 
    	searchBFS(start, end, begin, wordLadder, dictionary);
    	wordLadder.add(start.toUpperCase());
    	
    	ArrayList<String> finalLadder = new ArrayList<String>(); 
    	for (String word : wordLadder) {
    		finalLadder.add(0, word);
    	}
    	
		return finalLadder;
	}
    
	
	public static void printLadder(ArrayList<String> ladder) {
		// If word ladder only contains start and end words, could not find
		if (ladder.size() == 2) {
			System.out.println("no word ladder can be found between " + ladder.get(0).toLowerCase() + " and " + ladder.get(1).toLowerCase() + ".");
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
	
	private static void searchBFS(String start, String end, Node tree, ArrayList<String> ladder, Set<String> dictionary){
		HashSet<String> visited = new HashSet<>(); // Words that have been seen already
		LinkedList<Node> queue = new LinkedList<>(); // Upcoming word nodes to explore
		ArrayList<String> upNext; // Holds all adjacent words

		queue.addFirst(tree);
		while(! queue.isEmpty()) {
			Node hold = queue.removeFirst(); // Current node item
			upNext = findAdjacentNodes(hold.word, dictionary); // find all adjacent words
			
			// add all adjacent words to queue
			for(String item : upNext) {
				if ( end.equals(item.toLowerCase()) ){
					while(! hold.word.equals(start)) {
						ladder.add(hold.word); 
						hold = hold.prev; // Go up one level
					}
					return;
				}
				
				else if (! visited.contains(item)) {
					Node touch = new Node(item, hold); // Create a new Node for our word
					queue.addLast(touch); // Add our new Node to the queue
					visited.add(item); // Add to discovered words 
				}
			}	
		}
	}
	
	
	
	private static ArrayList<String> searchDFS(String current, String end, ArrayList<String> ladder, ArrayList<String> Searched, Set<String> dictionary){
		ArrayList<String> check; 
		
		// End Condition: we have found our word
		if(current.toLowerCase().equals(end)) { 
			ladder.add(current.toLowerCase()); 
			return ladder; 
		}
		
		// If not found, find all adjacent words to current and test
		Searched.add(current); 
		ArrayList<String> adjacent = findAdjacentNodes(current, dictionary); // All words one letter off of current
		
		
		// For each word one letter off, search it's adjacent words
		for (String next : adjacent) {
			if (! Searched.contains(next.toUpperCase())) {
				check = searchDFS(next, end, ladder, Searched, dictionary);
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
	private static ArrayList<String> findAdjacentNodes(String givenWord, Set<String> dictionary){
			ArrayList<String> adjacent = new ArrayList<>();
			String testWord = givenWord.toUpperCase();
			for(String compare : dictionary){
				if(!compare.equals(givenWord)){
					if(differbyOne(testWord, compare)){
						adjacent.add(compare);
					}
				}
			}
			return adjacent;
	}

	//Returns: True if given words only differ by one letter, false otherwise
	private static boolean differbyOne(String first, String second) {
		int differsBy = 0;

		for (int i = 0; i < first.length(); i++) {
			if (first.charAt(i) != second.charAt(i)) {
				differsBy++;
				if (differsBy > 1) {
					return false;
				}
			}
		}

		return true;

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
