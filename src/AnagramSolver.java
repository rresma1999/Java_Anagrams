
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;

public class AnagramSolver {

	public Map<String, LetterInventory> inventories;

	/**
	 * a public constructor that has one parameter, a List<String>. (A list of
	 * strings.) The list contains the dictionary this AnagramSolver is to use when
	 * forming anagrams. This constructor creates and stores letter inventories for
	 * each element of the list. Store the original words in the dictionary and
	 * their corresponding letter inventories inside the AnagramSolver class. pre:
	 * dictionary != null post: none
	 * 
	 * @param dictionary
	 */
	public AnagramSolver(List<String> dictionary) {
		// check preconditions
		if (dictionary == null)
			throw new IllegalArgumentException("dictionary != null");

		// initialize inventories as a HashMap
		inventories = new HashMap<String, LetterInventory>();
		// parse through the dictionary and map each word to a new LetterInventory
		Iterator<String> wordItr = dictionary.iterator();
		while (wordItr.hasNext()) {
			// instantiate temporary string to map in inventories
			String temp = wordItr.next();
			inventories.put(temp, new LetterInventory(temp));
		}
	}

	// WordEntry Comparator class that helps with the sorting of a list with strings
	private class WordEntryComparator implements Comparator<String> {
		// compare method for two strings, returns integer value based on length of
		// strings
		public int compare(String s1, String s2) {
			int dist = s1.length() - s2.length();
			// check if the strings have the same length
			if (dist == 0) {
				// if so, return value based on alphabetical value
				if (s1.charAt(0) < s2.charAt(0)) {
					return -1;
				} else {
					return 1;
				}
			} else {
				// return the integer dist between to strings
				return dist;
			}
		}
	}

	// Anagram Comparator class that helps with the sorting of the anagrams
	private class AnagramComparator implements Comparator<List<String>> {
		// compare method for two lists of string, returns integer value based on length
		// of the ArrayLists
		public int compare(List<String> list1, List<String> list2) {
			int dist = list1.size() - list2.size();
			// check if the ArrayLists have the same length
			if (dist == 0) {
				// if so, parse through the Lists and compare the individual Strings
				int index = 0;
				while (index < list1.size()) {
					String string1 = list1.get(index);
					String string2 = list2.get(index);
					// check if the strings are lexographically the same
					if (string1.compareTo(string2) == 0) {
						// if so increment to the next string
						index++;
					} else {
						// return the integer value of s1.compareTo(s2)
						return string1.compareTo(string2);
					}
				}
			}
			// return the compare value between the two lists
			return dist;
		}
	}

	/**
	 * a public method named getAnagrams that has two parameters: a String, and an
	 * int. The String is the target word or phrase and your method will form
	 * anagrams out of that word or phrase. The String sent as a parameter does not
	 * have to be in the AnagramSolver's dictionary. It may contain characters which
	 * must be ignored. (Recall this is handled by the LetterInventory class
	 * already.) The int parameter indicates the maximum number of words allowed in
	 * the anagrams of the given String. If the int is 0 there is no maximum number
	 * of words for the anagrams being formed. The precondition requires the String
	 * not be null and the String contain at least one English letter. The int
	 * parameter must be greater than or equal to 0. pre: phrase != null, phrase
	 * must contain at least one english letter, maxWords >= 0 post: none
	 * 
	 * @param phrase
	 * @param maxWords
	 */
	public List<List<String>> getAnagrams(String phrase, int maxNumWords) {
		// check preconditions
		if (phrase == null || !hasEnglishLetter(phrase) || maxNumWords < 0) {
			throw new IllegalArgumentException("Failed preconditions: getAnagrams");
		}
		// initialize new ArrayList<List<String>> to store anagrams, return at end
		List<List<String>> result = new ArrayList<List<String>>();
		if (maxNumWords == 0)
			maxNumWords = Integer.MAX_VALUE;
		// instantiate a modified dictionary that will keep track of the feasible
		// anagram partials
		ArrayList<String> modDict = new ArrayList<String>();
		// instantiate temporary LetterInventory for phrase to compare to each dict word
		// inventory
		LetterInventory phraseInv = new LetterInventory(phrase);
		// filter the feasible partial words from the dictionary into the modDict
		filterPossibleAnagrams(modDict, phraseInv);
		// sort the modified dictionary based on length
		Collections.sort(modDict, new WordEntryComparator());
		// get all the anagrams recursively
		getAnagramsHelper(phraseInv, maxNumWords, new ArrayList<String>(), modDict, 
				new LetterInventory(), modDict.size() - 1, result);
		// sort the final ArrayList of ArrayLists
		Collections.sort(result, new AnagramComparator());
		return result;
	}

	// Helper method for getAnagrams(), works the bulk of the program, recursively
	// finds all
	// anagram possibilities
	private void getAnagramsHelper(LetterInventory phraseInv, int maxNumWords, 
			ArrayList<String> potential, ArrayList<String> modDict, LetterInventory currInv,
			int currIndex, List<List<String>> result) {

		// base case
		if (baseCase(phraseInv, currInv, potential, maxNumWords)) {
			// check if the current inventory is equal to the phraseInv
			if (currInv.equals(phraseInv)) {
				// if equal, sort the potential anagram based on alphabet and add it to result
				Collections.sort(potential);
				result.add(potential);
			}
		} else {
			// parse through the modified dictionary
			for (int wordIndex = currIndex; wordIndex >= 0; wordIndex--) {
				// instantiate temporary
				String word = modDict.get(wordIndex);
				LetterInventory temp = inventories.get(word);
				// make choice, add t
				potential.add(word);
				// recursive call, virtually shrinks modDict by passing in wordIndex
				getAnagramsHelper(phraseInv, maxNumWords, deepCopy(potential), modDict, 
						currInv.add(temp), wordIndex, result);
				// un-make choice
				potential.remove(potential.size() - 1);
			}
		}
	}

	// Utility method that returns a deep copy of a passed in ArrayList
	private ArrayList<String> deepCopy(ArrayList<String> temp) {
		// instantiate a new ArrayList<String> to be returned at the end
		ArrayList<String> copy = new ArrayList<String>();
		// parse through temp's elements, copying over the elements into copy
		for (int wordIndex = 0; wordIndex < temp.size(); wordIndex++) {
			copy.add(temp.get(wordIndex));
		}
		// return the deep copied ArrayList
		return copy;
	}

	// Utility boolean method for the base case
	private boolean baseCase(LetterInventory phraseInv, LetterInventory currInv, 
			ArrayList<String> potential, int maxNumWords) {
		// instantiate temporary LetterInventory that is the subtraction of phraseInv and currInv
		LetterInventory temp = phraseInv.subtract(currInv);
		// if temp is null then the currInv is over the limit
		if (temp == null) {
			return true;
		} else if (temp.isEmpty()) {
			// if the temp isEmpty() then the inventories match
			return true;
		} else if (potential.size() == maxNumWords) {
			return true;
		}
		return false;
	}

	// Helper method to help filter out the words from the dictionary that would not
	// be
	// possible anagrams
	// pre: possibleAnagrams != null, phrase != null
	// post: none
	private void filterPossibleAnagrams(ArrayList<String> modDict, LetterInventory phraseInv) {
		// check preconditions
		if (modDict == null || phraseInv == null) {
			throw new IllegalArgumentException("possibleAnagrams != null, phrase != null");
		}

		// create a set view of the keys in inventories to parse through
		Set<String> dictionarySet = inventories.keySet();
		// parse through the dictionarySet of words and check if the values that map to
		// the words
		// are comparable to phraseInv, if they are not feasible, they should not
		// be added
		// to the small set of words
		Iterator<String> dictItr = dictionarySet.iterator();
		while (dictItr.hasNext()) {
			String word = dictItr.next();
			// if temp is null then this word is not a possible anagram for phrase
			LetterInventory temp = phraseInv.subtract(inventories.get(word));
			if (temp != null) {
				// add word to the possibleAnagrams list if temp is not null
				modDict.add(word);
			}
		}
	}

	// Helper method to check if phrase in getAnagrams has at least one english
	// letter
	// pre: phrase != 0, post: none
	private boolean hasEnglishLetter(String phrase) {
		// check precondition
		if (phrase == null) {
			throw new IllegalArgumentException("phrase != 0");
		}
		// replace all non letter characters with ""
		phrase = phrase.replaceAll("[^a-zA-Z]+", "");
		// return boolean value of phrase's length not being 0
		return phrase.length() != 0;
	}
}
