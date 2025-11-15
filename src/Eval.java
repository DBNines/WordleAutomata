package src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import src.Checker.LetterColor;

public class Eval {
    private ArrayList<String> answerList = new ArrayList<>();

    public Eval(){
        loadAnswerList();
    }
    

    public String findBestWordFromList(ArrayList<String> guessList){
        Checker check = new Checker();
        Random rand = new Random();
        int randomNum = rand.nextInt(guessList.size());
        String bestGuess = guessList.get(randomNum); //Just start with a random guess, in case we find only Dupes
        double bestGuessScore = 0; //Average of number of greens + yellows
        for(int i = 0; i < guessList.size(); i++){ //For every guess word
            int currentScore = 0;
            if(!isDuplicate(guessList.get(i))){ //Make sure our word doesn't have duplicate letters //TODO: Perhaps this is too punishing for duplicates? Maybe we need them.
                for(int j = 0; j < answerList.size(); j++){ //Check against every answer
                    check.setSolution(answerList.get(j));
                    LetterColor[] result = check.getResult(guessList.get(i));
                    for(int k = 0; k < result.length; k++){
                        switch(result[k]){
                            case Gray:
                                break;
                            case Green:
                                currentScore++;
                                break;
                            case Yellow:
                                currentScore++; //TODO: Perhaps this should be less than 1? Maybe not?
                                break;
                            default:
                                break;
                        }
                    }
                }
                if(currentScore > bestGuessScore){ //This word scored better
                    bestGuessScore = currentScore;
                    bestGuess = guessList.get(i);
                    System.out.println("EVAL: NEW BEST GUESS IS: " + bestGuess + "   (" + bestGuessScore + ")");
                }
            }
        }
        System.out.println("EVAL: BEST GUESS IS: " + bestGuess + "   (" + bestGuessScore + ")");
        return bestGuess;
    }

    private void loadAnswerList(){
        try (BufferedReader reader = new BufferedReader(new FileReader("tools/wordle_answers.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                answerList.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading: " + "tools/wordle_answers.txt");
        }
    }
    private boolean isDuplicate(String guess){
        int duplicateCount = 0;
        for (int i = 0; i < guess.length(); i++){//Grab the first letter
            for (int j = 0; j < guess.length(); j++){//Check against the other letters
                if(i != j){ //Don't check against self
                    if(guess.charAt(i) == guess.charAt(j)){
                        duplicateCount++;
                    }
                }
            }
        }
        /*if(duplicateCount != 0){
            System.out.println("EVAL: DUPLICATE DETECTED: " + guess + "  DUPE COUNT: " + duplicateCount);
        }*/
        
        return duplicateCount != 0;
    }
}
