package src;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import src.Checker.LetterColor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Solver {
    private static final String guessListFileName = "guess_list.txt";
    private ArrayList<String> openGuessList;
    private ArrayList<String> guessHistory = new ArrayList<>();
    private Checker SolutionChecker; 
    // Critera data structures
    private ArrayList<Character> notInSolution = new ArrayList<>(); // Equivalent to gray letters

    public Solver(String solution) {
        this.openGuessList = loadGuessList();
        SolutionChecker = new Checker(solution);
        boolean foundSolution = false;
        while(!foundSolution){
            String guess = getRandomGuess();
            guessHistory.add(guess);
            foundSolution = checkGuess(guess); //TODO: Remove guess we pull from guess list
        }

        //Prints gusses taken
        Iterator<String> itr = guessHistory.iterator();
        int counter = 0;
        while(itr.hasNext()){
            counter++;
            System.out.println(counter + " | " + itr.next());
        }
    }

    // Check guess, return true if solution
    // If not, add what we learned from guess to Critera
    private boolean checkGuess(String guess) {
        if (SolutionChecker.checkGuess(guess)) {
            System.out.println("Found solution");
            return true;
        }
        mapGuessToCritera(guess); //Find letters not in guess, then prunes words that contain these letters
        filterWordsByGrays();

        return false;
    }

    private void mapGuessToCritera(String guess) {
        LetterColor[] guessResult = SolutionChecker.getResult(guess); 
        for (int i = 0; i < guessResult.length; i++){ //For every letter result
            char guessLetter = guess.charAt(i);
            switch(guessResult[i]) {
                case Gray:
                    if (!notInSolution.contains(guessLetter)) { // Only add if unique
                        System.out.println("SOLVER: Our solution does not contain: " + guessLetter);
                        notInSolution.add(guessLetter);
                    }
                    break;
                case Green:
                    break;
                case Yellow:
                    break;
                default:
                    break;

            }
        }
    }

    private void filterWordsByGrays(){
        int counter = 0;
        Iterator<String> itr = openGuessList.iterator();
        //for(int i = 0; i < openGuessList.size(); i++){ //For every word in the open guess list
        while(itr.hasNext()){
            //String currentWord = openGuessList.get(i); //Grab current word to check and see if contains gray letters
            String currentWord = itr.next();
            boolean removed = false;
            for(int j = 0; j < notInSolution.size(); j++){ //For every letter not in the solution
                char currentLetter = notInSolution.get(j);
                if(currentWord.contains(String.valueOf(currentLetter)) && !removed){ //If the word has not been already removed and it contains gray
                    itr.remove();
                    removed = true;
                    counter++;
                }
            }
        }
        System.out.println("SOLVER: Removed " + counter + " words | Solution space is now " + openGuessList.size() + " words");
    }

    // Picks a random guess from the open list
    private String getRandomGuess() {
        Random rand = new Random();
        int randomNum = rand.nextInt(openGuessList.size());
        return openGuessList.get(randomNum);
    }

    // Load the list of gusses into a new array list
    private ArrayList<String> loadGuessList() {
        try (BufferedReader reader = new BufferedReader(new FileReader(guessListFileName))) {
            String line;
            ArrayList<String> list = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                //System.out.println(line);
                list.add(line);
            }
            return list;
        } catch (IOException e) {
            System.out.println("Error reading: " + guessListFileName);
            return null;
        }
    }
}
