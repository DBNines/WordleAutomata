# Sources
- Credit to https://github.com/dwyl/english-words for words used in guessing

# TODO
- Starting Guess IDEA
    - Create a function that grabs all Wordles, puts them into a file
        - Use this to compare from our guess list, find the best possible starting word (Most greens? Most yellows? Most green+yellows?)

# ALGORITHM OUTLINE
1. Start with previously computed best starting word
2. Check if word is solution, if so END
3. Filter words in list by critera in data structures
4. Randomly pick one from list? (Is there a better way?)
5. If not last guess, repeat to step 2

# DATA STRUCTURES
- Not In Solution: List of letters (grays) that are not in the solution
- Current Known: Known (green) letters
- Includes: List of (yellow) letters where we do not know where they go
- Not In Solution Here: List(2D Array?) for every character, stores what yellow values were there

# Notes
- How to access current wordle solutions .json 
    - https://www.nytimes.com/svc/wordle/v2/2025-11-13.json

- Java Discord Resources
    - https://discord4j.com/
    - https://github.com/discord-jda/JDA
    - https://discord.com/developers/docs/quick-start/overview-of-apps

- Alt world list
    - https://github.com/tabatkins/wordle-list/blob/main/words
