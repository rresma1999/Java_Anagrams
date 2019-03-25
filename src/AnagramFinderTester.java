import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/* CS 314 STUDENTS: FILL IN THIS HEADER AND THEN COPY AND PASTE IT TO YOUR
 * LetterInventory.java AND AnagramSolver.java CLASSES.
 *
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

public class AnagramFinderTester {

    
    private static final String testCaseFileName = "testCaseAnagrams.txt";
    private static final String dictionaryFileName = "d3.txt";

    /**
     * main method that executes tests.
     * @param args Not used.
     */
    public static void main(String[] args) {

//    	cs314StudentTestsForLetterInventory();
        
  
        // tests on the anagram solver itself
        boolean displayAnagrams = getChoiceToDisplayAnagrams();
        AnagramSolver solver = new AnagramSolver(AnagramMain.readWords(dictionaryFileName));
        runAnagramTests(solver, displayAnagrams);
    }
    
    private static void cs314StudentTestsForLetterInventory() {
    		LetterInventory test1 = new LetterInventory("Ryan Resma");
    		LetterInventory test2 = new LetterInventory();
    		
    		// Constructor, isEmpty() test
    		printTests("constructor, isEmpty", "size on empty LetterInventory", 
    				test2.isEmpty());
    		
    		// Constructor, size()
    		printTests("constructor, size", "size on nonempty LetterInventory", 
    				test1.size() == 9);
    		
    		// isEmpty() test
    		printTests("isEmpty", "ability to identify nonempty LetterInventory", 
    				!test1.isEmpty());
    		
    		// get() test
    		printTests("get", "ability to call get() on empty LetterInventory", 
    				test2.get('b') == 0);
    		
    		// get() test
    		printTests("get", "ability to get frequencies of a character in a LetterInventory", 
    				test1.get('r') == 2);
    		
    		// size() test
    		printTests("size", "ability to return size on empty LetterInventory", 
    				test2.size() == 0);
    		
    		// toString() test
    		printTests("toString", "ability to return string version of letter inventory", 
    				test1.toString().equals("aaemnrrsy"));
    		
    		// to String() test
    		printTests("toString", "ability to call toString on empty inventory", 
    				test2.toString().equals(""));
    		
    		// add() test
    		LetterInventory test3 = test1.add(test2);
    		printTests("add()", "ability to add empty inventory to nonempty inventory",
    				test3.size() == 9);
    		
    		// equals() test
    		printTests("equals()", "comparison of two equal LetterInventory objects", 
    				test3.equals(test1));
    		
    		// subtract() test
    		LetterInventory test4 = test1.subtract(test3);
    		printTests("subtract()", "ability to subtract two equal LetterInventory objects",
    				test4.size() == 0);
    		
    		// add() test
    		LetterInventory test5 = test1.add(test3);
    		printTests("add()", "ability to add two nonempty LetterInventory objects",
    				test5.size() == 18);
    		
    		// equals() test
    		printTests("equals()", "ability to identify contrasts in two LetterInventory objects", 
    				!test5.equals(test1));
    		
    		// subtract() test
    		LetterInventory test6 = test2.subtract(test5);
    		printTests("subtract()", "ability to return null LetterInventory when calling "
    				+ "subtract method on empty inventory", test6 == null);
    }
    
    // Helper method to help print LetterInventory tests
    private static void printTests(String testName, String testWhat, boolean check) {
    		StringBuilder sb = new StringBuilder();
    		sb.append(testName);
    		sb.append(" test. Tests the ");
    		sb.append(testWhat);
    		if (check) {
    			sb.append(" Passed Test. pop pop pop!\n");
    		} else {
    			sb.append(" Failed Test. OH NOOOO!!!\n");
    		}
    		System.out.println(sb.toString());
    }

    private static boolean getChoiceToDisplayAnagrams() {
        Scanner console = new Scanner(System.in);
        System.out.print("Enter y or Y to display anagrams during tests: ");
        String response = console.nextLine();
        console.close();
        return response.length() > 0 && response.toLowerCase().charAt(0) == 'y';
    }

    private static boolean showTestResults(Object expected, Object actual, int testNum, String featureTested) {
        System.out.println("Test Number " + testNum + " testing " + featureTested);
        System.out.println("Expected result: " + expected);
        System.out.println("Actual result: " + actual);
        boolean passed = (actual == null && expected == null) || (actual != null && actual.equals(expected));
        if(passed)
            System.out.println("Passed test " + testNum);
        else
            System.out.println("!!! FAILED TEST !!! " + testNum);
        System.out.println();
        return passed;
    }

    /**
     * Method to run tests on Anagram solver itself.
     * pre: the files d3.txt and testCaseAnagrams.txt are in the local directory
     * 
     * assumed format for file is
     * <NUM_TESTS>
     * <TEST_NUM>
     * <MAX_WORDS>
     * <PHRASE>
     * <NUMBER OF ANAGRAMS>
     * <ANAGRAMS>
     */
    private static void runAnagramTests(AnagramSolver solver, boolean displayAnagrams) {
        int solverTestCases = 0;
        int solverTestCasesPassed = 0;
        Stopwatch st = new Stopwatch();
        try {
            Scanner sc = new Scanner(new File(testCaseFileName));
            final int NUM_TEST_CASES = Integer.parseInt(sc.nextLine().trim());
            System.out.println(NUM_TEST_CASES);
            for(int i = 0; i < NUM_TEST_CASES; i++) {
                // expected results
                TestCase currentTest = new TestCase(sc);
                solverTestCases++;
                st.start();
                // actual results
                List<List<String>> actualAnagrams = solver.getAnagrams(currentTest.phrase, currentTest.maxWords);
                st.stop();
                if(displayAnagrams) {
                    displayAnagrams("actual anagrams", actualAnagrams);
                    displayAnagrams("expected anagrams", currentTest.anagrams);
                }


                if(checkPassOrFailTest(currentTest, actualAnagrams))
                    solverTestCasesPassed++;
                System.out.println("Time to find anagrams: " + st.time());
            }
            sc.close();
        }
        catch(IOException e) {
            System.out.println("\nProblem while running test cases on AnagramSolver. Check" +
                    " that file testCaseAnagrams.txt is in the correct location.");
            System.out.println(e);
            System.out.println("AnagramSolver test cases run: " + solverTestCases);
            System.out.println("AnagramSolver test cases failed: " + (solverTestCases - solverTestCasesPassed));
        }
        System.out.println("\nAnagramSolver test cases run: " + solverTestCases);
        System.out.println("AnagramSolver test cases failed: " + (solverTestCases - solverTestCasesPassed));
    }


    // print out all of the anagrams in a list of anagram
    private static void displayAnagrams(String type,
            List<List<String>> anagrams) {

        System.out.println("Results for " + type);
        System.out.println("num anagrams: " + anagrams.size());
        System.out.println("anagrams: ");
        for(List<String> singleAnagram : anagrams)
            System.out.println(singleAnagram);
    }


    // determine if the test passed or failed
    private static boolean checkPassOrFailTest(TestCase currentTest,
            List<List<String>> actualAnagrams) {
        
        boolean passed = true;
        System.out.println();
        System.out.println("Test number: " + currentTest.testCaseNumber);
        System.out.println("Phrase: " + currentTest.phrase);
        System.out.println("Word limit: " + currentTest.maxWords);
        System.out.println("Expected Number of Anagrams: " + currentTest.anagrams.size());
        if(actualAnagrams.equals(currentTest.anagrams)) {
            System.out.println("Passed Test");
        }
        else{
            System.out.println("\n!!! FAILED TEST CASE !!!");
            System.out.println("Recall MAXWORDS = 0 means no limit.");
            System.out.println("Expected number of anagrams: " + currentTest.anagrams.size());
            System.out.println("Actual number of anagrams:   " + actualAnagrams.size());
            if(currentTest.anagrams.size() == actualAnagrams.size()) {
                System.out.println("Sizes the same, but either a difference in anagrams or anagrams not in correct order.");
            }
            System.out.println();
            passed = false;
        }  
        return passed;
    }

    // class to handle the parameters for an anagram test 
    // and the expected result
    private static class TestCase {

        private int testCaseNumber;
        private String phrase;
        private int maxWords;
        private List<List<String>> anagrams;

        // pre: sc is positioned at the start of a test case
        private TestCase(Scanner sc) {
            testCaseNumber = Integer.parseInt(sc.nextLine().trim());
            maxWords = Integer.parseInt(sc.nextLine().trim());
            phrase = sc.nextLine().trim();
            anagrams = new ArrayList<List<String>>();
            readAndStoreAnagrams(sc);
        }

        // pre: sc is positioned at the start of the resulting anagrams
        // read in the number of anagrams and then for each anagram:
        //  - read in the line
        //  - break the line up into words
        //  - create a new list of Strings for the anagram
        //  - add each word to the anagram
        //  - add the anagram to the list of anagrams
        private void readAndStoreAnagrams(Scanner sc) {
            int numAnagrams = Integer.parseInt(sc.nextLine().trim());
            for(int j = 0; j < numAnagrams; j++){
                String[] words = sc.nextLine().split("\\s+");
                ArrayList<String> anagram = new ArrayList<String>();
                for(String st : words)
                    anagram.add(st);
                anagrams.add(anagram);
            }
            assert anagrams.size() == numAnagrams : "Wrong number of angrams read or expected";
        }
    }
}