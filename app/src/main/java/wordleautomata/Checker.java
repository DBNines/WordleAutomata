package wordleautomata;

public class Checker {
    private String solution;

    public enum LetterColor {
        Gray, Green, Yellow
    }
    public Checker(){
        this.solution = null;
    }

    public Checker(String solution) {
        this.solution = solution;
    }

    public boolean checkGuess(String guess) {
        return solution.equals(guess);
    }
    public void setSolution(String solution){
        this.solution = solution;
    }

    public LetterColor[] getResult(String guess) {
        LetterColor[] resultColors = new LetterColor[5];
        for (int i = 0; i < guess.length(); i++) { // For every letter in the guess
            char guessLetter = guess.charAt(i);
            if (!solution.contains(String.valueOf(guessLetter))) { // If solution not contain guess letter, it is a gray
                resultColors[i] = LetterColor.Gray;
            } else { // So our solution does have the letter somewhere
                if (guessLetter == solution.charAt(i)) { // If the guess letter equals the solution letter at the same
                    resultColors[i] = LetterColor.Green;
                } else {
                    resultColors[i] = LetterColor.Yellow;
                }
            }
        }
        //Print result
        /*System.out.print("CHECKER: Results For: " + guess + ": ");
        for(int j = 0; j < resultColors.length; j++){
            System.out.print(resultColors[j]+", ");
        }
        System.out.print('\n');*/

        return resultColors;
    }

}
