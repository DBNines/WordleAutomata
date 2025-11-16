package wordleautomata;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import wordleautomata.Checker.LetterColor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Solver {
    private static final String guessListFileName = "/guess_list.txt";
    private ArrayList<String> openGuessList;
    private ArrayList<String> guessHistory = new ArrayList<>();
    private Checker SolutionChecker; 
    // Critera data structures
    private ArrayList<Character> notInSolution = new ArrayList<>(); // Equivalent to gray letters
    private char[] knownInSolution =  {'#', '#', '#', '#', '#'}; // Equivalent to green letters
    private ArrayList<Character> includes = new ArrayList<>(); //tracks what the yellows are
    private ArrayList<ArrayList<Character>> notInSolutionHere = new ArrayList<ArrayList<Character>>(5); //tracks where yelllows were 

    public Solver(String solution) {
        this.openGuessList = loadGuessList();
        SolutionChecker = new Checker(solution);
        for(int i = 0; i < 5; i++){
            notInSolutionHere.add(new ArrayList<>()); //Prefill the list with lists
        }
        boolean foundSolution = false;
        //String guess = getRandomGuessNoDupes();
        Eval guessEval = new Eval();
        
        while(!foundSolution){
            String guess = guessEval.findBestWordFromList(openGuessList);
            guessHistory.add(guess);
            foundSolution = checkGuess(guess);
            //guess = guessEval.findBestWordFromList(openGuessList);//getRandomGuess();
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
        }else{ //Invalid guess, remove it from guess list
            openGuessList.remove(guess);
            System.out.println("SOLVER: Removed " + guess + " from guess list");
        }
        mapGuessToCritera(guess); //Find letters not in guess, then prunes words that contain these letters
        filterWordsByGrays();
        filterWordsByGreens();
        filterWordsByBasicYellow(); //Only gets of words missing the yellow
        filterWordsByAdvancedYellow(); //Gets rid of words that have yellow in same spot, or fail process of elimination. TODO: Figure out process

        for(int i = 0; i < notInSolutionHere.size(); i++){
            for (int j = 0; j < notInSolutionHere.get(i).size(); j++){
                System.out.print(notInSolutionHere.get(i).get(j));
                System.out.print(", ");
            }
            System.out.print('\n');
        }

        return false;
    }

    private void mapGuessToCritera(String guess) {
        LetterColor[] guessResult = SolutionChecker.getResult(guess); 
        for (int i = 0; i < guessResult.length; i++){ //For every letter result
            char guessLetter = guess.charAt(i);
            switch(guessResult[i]) {
                case Gray:
                    if (!notInSolution.contains(guessLetter)) { // Only add if unique
                        System.out.println("SOLVER(" + guess +")" + ": Our solution DOES NOT contain: " + guessLetter);
                        notInSolution.add(guessLetter);
                    }
                    break;
                case Green:
                    System.out.println("SOLVER(" + guess +")" + ": Our solution DOES contain: " + guessLetter);
                    knownInSolution[i] =  guessLetter; //Set spot to our green letter
                    break;
                case Yellow:
                    if (!includes.contains(guessLetter)) { //Only add if unique
                        System.out.println("SOLVER(" + guess +")" + ": Our solution SOMEWHERE contain: " + guessLetter);
                        includes.add(guessLetter);
                    }
                    // Advanced Storing
                    if (!notInSolutionHere.get(i).contains(guessLetter)) { //Only add if unique
                        notInSolutionHere.get(i).add(guessLetter);
                    }
                    
                    break;
                default:
                    System.out.println("SOLVER: INVALID GUESS RESULT CASE");
                    break;

            }
        }
    }
    private void filterWordsByAdvancedYellow(){
        int counter = 0;
        Iterator<String> itr = openGuessList.iterator();
        while(itr.hasNext()){ //For every word in the open list
            String currentWord = itr.next();
            boolean removed = false;
            for (int i = 0; i < currentWord.length(); i++){ //for every letter in the word
                char currentWordLetter = currentWord.charAt(i);
                if(notInSolutionHere.get(i).contains(currentWordLetter) && !removed){
                    itr.remove();
                    removed = true;
                    counter++;
                }
            }
        }
        System.out.println("SOLVER: Removed " + counter + " words (Yellow, ADVANCED)| Solution space is now " + openGuessList.size() + " words");
    }

    private void filterWordsByBasicYellow(){ //Remove words that don't have the yellow
        int counter = 0;
        Iterator<String> itr = openGuessList.iterator();
        while(itr.hasNext()){ //For every word in the open list
            String currentWord = itr.next();
            boolean removed = false;
            for(int i = 0; i < includes.size(); i++){ //For every e
                char currentLetter = includes.get(i); //Get a known yellow
                    if(!currentWord.contains(String.valueOf(currentLetter)) && !removed){ //Remove if doesn't have yellow
                        itr.remove();
                        removed = true;
                        counter++;
                    }
            }
        }
        System.out.println("SOLVER: Removed " + counter + " words (Yellow)| Solution space is now " + openGuessList.size() + " words");

    }
    private void filterWordsByGreens(){
        int counter = 0;
        Iterator<String> itr = openGuessList.iterator();
        while(itr.hasNext()){ //For every word in the open list
            String currentWord = itr.next();
            boolean removed = false;
            for(int i = 0; i < knownInSolution.length; i++){
                char currentLetter = knownInSolution[i]; //Get currently known green at index
                if (currentLetter != '#'){ //Make sure the green isn't blank
                    if(currentWord.charAt(i) != currentLetter && !removed){ //Check if the current green letter is the same as the letter in the word
                        itr.remove();
                        removed = true;
                        counter++;
                    }
                }
            }
        }
        System.out.println("SOLVER: Removed " + counter + " words (Greens)| Solution space is now " + openGuessList.size() + " words");
    }

    private void filterWordsByGrays(){
        int counter = 0;
        Iterator<String> itr = openGuessList.iterator();
        while(itr.hasNext()){
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
        System.out.println("SOLVER: Removed " + counter + " words (Grays)| Solution space is now " + openGuessList.size() + " words");
    }

    // Picks a random guess from the open list
    private String getRandomGuess() {
        Random rand = new Random();
        int randomNum = rand.nextInt(openGuessList.size());
        return openGuessList.get(randomNum);
    }
    // Picks a random guess without duplicates
    private String getRandomGuessNoDupes(){
        boolean validGuess = false;
        String guess = "";
        while(!validGuess){
            Random rand = new Random();
            int randomNum = rand.nextInt(openGuessList.size());
            guess = openGuessList.get(randomNum);
            int duplicateCount = 0;
            for (int i = 0; i < guess.length(); i++){//Grab the first letter
                for (int j = 0; j < guess.length(); j++){//Check against the other letters
                    if(i != j){ //Don't check against self
                        if(guess.charAt(i) == guess.charAt(j)){
                            duplicateCount++;
                        }
                    }
                }
                if(duplicateCount == 0){
                    validGuess = true;
                }else{
                    validGuess = false;
                    System.out.println("SOLVER: Discard " + guess + " as a starting guess. (" + duplicateCount + " duplicates)");
                }
            }
        }
        return guess;
    }

    // Load the list of gusses into a new array list
    private ArrayList<String> loadGuessList() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(guessListFileName)))) {
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
