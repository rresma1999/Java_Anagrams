/*
 * Student information for assignment:
 *
 *  On my honor, Ryan Resma, this programming assignment is my own work
 *  and I have not provided this code to any other student.
 *
 *  UTEID: rmr3429
 *  email address: ryan.resma@utexas.edu
 *  Grader name: Joseph Manahan
 *  Number of slip days I am using: 0
 */

public class LetterInventory {
	
	private char[] letters;
	private int[] frequencies;
	private int numLetters;
	
	/**
	 * Default Constructor
	 */
	public LetterInventory() {
		letters = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		frequencies = new int[26];
		numLetters = 0;
	}
	
	/**
	 * Constructor that takes in a String word, ignoring case
	 * pre: theWord != null
	 * @param theWord
	 */
	public LetterInventory(String theWord) {
		// check precondition
		if (theWord == null) 
			throw new IllegalArgumentException("theWord != null");
		// initialize letters char array to the letters of the alphabet
		letters = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		// initialize numLetters int array to the array of same length as letters
		frequencies = new int[26];
		takeInventory(theWord.toLowerCase());
	}
	
	// Utility method for Constructor to help instantiate instance variables
	private void takeInventory(String word) {
		numLetters = 0;
		// tally all the frequencies of the characters in numLetters, parse through word 
		for (int charIndex = 0; charIndex < word.length(); charIndex++) {
			// initialize temporary char at the given charIndex
			char temp = word.charAt(charIndex);
			// find the letter in the char array
			for (int letterIndex = 0; letterIndex < letters.length; letterIndex++) {
				// if the letter has been found in the char array, use letterIndex to
				// access frequencies int[]
				if (temp == letters[letterIndex]) {
					// increment numLetters
					numLetters++;
					// increment the number of appearances for this letter
					frequencies[letterIndex]++;
				}
			}
		}
	}
	
	/**
	 * a method named get that accepts a char and returns the frequency of that
	 * letter in this LetterInventory. The precondition requires that the char be an
	 * English letter. It may be an upper or lower case letter. The answer returned
	 * must be the same given the upper or lower case version of a letter. This
	 * method shall be O(1).
	 * 
	 * pre: 'a' <= c && c <= 'z'
	 * post: none
	 * 
	 * @param c
	 * @return number of frequencies that the char c appears in the
	 */
	public int get(char c) {
		// check precondition
		if ('a' > c && c > 'z')
			throw new IllegalArgumentException("");
		
		// change char c to lower case for case insensitive aspect
		char temp = Character.toLowerCase(c);
		// the index of the character is the difference between temp and 'a'
		return frequencies[temp - 'a'];
	}
	
	/**
	 * a method named size that returns the total number of letters in this
	 * LetterInventory. This method shall be O(1).
	 * 
	 * @return total number of letters in this LetterInventory.
	 */
	public int size() {
		return numLetters;
	}
	
	/**
	 * a method named isEmpty that returns true if the size of this LetterInventory
	 * is 0, false otherwise. This method shall be O(1) with respect to the size of
	 * the alphabet.
	 * 
	 * @return boolean value numLetters == 0
	 */
	public boolean isEmpty() {
		return numLetters == 0;
	}
	
	/**
	 * a method named toString that returns a String representation of this
	 * LetterInventory. All letters in the inventory are listed in alphabetical
	 * order.
	 */
	@Override
	public String toString() {
		// instantiate new StringBuilder to return at end
		StringBuilder sb = new StringBuilder();
		// parse through the parallel arrays
		for (int index = 0; index < letters.length; index++) {
			// if the number of frequencies for a given letter is greater than 0, then append the
			// letter n number of times
			for (int letterCount = 0; letterCount < frequencies[index]; letterCount++) {
				sb.append(letters[index]);
			}
		}
		return sb.toString();
	}
	
	/**
	 * a method named add with one parameter, another LetterInventory object. The
	 * method returns a new LetterInventory created by adding the frequencies from
	 * the calling LetterInventory object to the frequencies of the letters in the
	 * explicit parameter. 
	 * 
	 * pre: other != null
	 * post: neither calling object or the explicit parameter are altered 
	 * @param other
	 * @return new LetterInventory
	 */
	public LetterInventory add(LetterInventory other) {
		// instantiate new LetterInventory to be returned at the end of the method
		// see constructor method for specific instance variable initializations
		LetterInventory result = new LetterInventory();
		// parse through the parallel arrays
		for (int index = 0; index < result.frequencies.length; index++) {
			// count the frequencies of both this calling object and other and add them together
			result.frequencies[index] = this.get(letters[index]) + other.get(letters[index]);
			// adjust numLetters based on the frequencies at index
			result.numLetters += result.frequencies[index];	
		}
		return result;
	}
	
	/**
	 * a method named subtract with one parameter, another LetterInventory object.
	 * The methods returns a new LetterInventory object created by subtracting the
	 * letter frequencies of the explicit parameter from the calling object's (this)
	 * letter frequencies.
	 * 
	 * @param other
	 * @return new LetterInventory
	 */
	public LetterInventory subtract(LetterInventory other) {
		// check preconditions
		if (other == null) 
			throw new IllegalArgumentException("other != null");
	
		// instantiate new LetterInventory to be returned at the end of the method
		// see constructor method for specific instance variable initializations
		LetterInventory result = new LetterInventory();
		// parse through the parallel arrays
		for (int index = 0; index < result.frequencies.length; index++) {
			// count the frequencies of both this calling object and other and add them together
			result.frequencies[index] = this.get(letters[index]) - other.get(letters[index]);
			// check to see if resulting frequency is negative, if so, return null
			if (result.frequencies[index] < 0) {
				return null;
			}
			// adjust numLetters based on the frequencies at index
			result.numLetters += result.frequencies[index];		
		}
		return result;
	}
	
	/**
	 * override the equals method from Object. Two LetterInventorys are equal if the frequency 
	 * for each letter in the alphabet is the same.
	 * 
	 * pre: other != null
	 */
	@Override
	public boolean equals(Object other) {
		// check preconditions
		if (other == null) {
			throw new IllegalArgumentException("other != null, other must be a LetterInventory");
		}
		// quick check to see if the classes of this and other match
		if (!(other instanceof LetterInventory)) {
			return false;
		}
		// parse through the frequencies of both this and other
		for (int freqIndex = 0; freqIndex < frequencies.length; freqIndex++) {
			// check if the frequencies of all the letters in both LetterInventories are the same
			if (this.frequencies[freqIndex] != ((LetterInventory) other).frequencies[freqIndex]) {
				// if the frequencies are different numbers, return false
				return false;
			}
		}
		return true;
	}
}
