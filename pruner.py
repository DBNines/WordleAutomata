# Quick little script to get a word list of only 5 letter words
def process(inputFile, outputName):
    output = open(outputName, "w")
    with open(inputFile) as input:
        for line in input:
            cleanLine = line.strip()
            if(len(cleanLine) == 5):
                output.write(cleanLine)
                output.write("\n")
    output.close()

process("words_alpha.txt", "guess_list.txt")