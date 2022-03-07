import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class xkcdpwgen {

  public static void main(String[] args) throws FileNotFoundException{
  	//Default password
    int numWords = 4;
    int capitals = 0;
    int numbers = 0;
    int symbols = 0;

	ArrayList<String> wordList = new ArrayList<String>(0);
    Readable in = new InputStreamReader(new FileInputStream("words.txt"));
	Scanner reader = new Scanner(in);
    while (reader.hasNext()) {
    	String word = reader.nextLine().toLowerCase();
      	wordList.add(word);
    }

    for (int i = 0; i < args.length; i++) {
      try {
        switch (args[i]) {
          case "-h":
          case "--help":
            System.out
                .println("usage: xkcdpwgen [-h] [-w WORDS] [-c CAPS] [-n NUMBERS] [-s SYMBOLS]\n"
                    + "                \n"
                    + "Generate a secure, memorable password using the XKCD method\n"
                    + "                \n"
                    + "optional arguments:\n"
                    + "    -h, --help            show this help message and exit\n"
                    + "    -w WORDS, --words WORDS\n"
                    + "                          include WORDS words in the password (default=4)\n"
                    + "    -c CAPS, --caps CAPS  capitalize the first letter of CAPS random words\n"
                    + "                          (default=0)\n"
                    + "    -n NUMBERS, --numbers NUMBERS\n"
                    + "                          insert NUMBERS random numbers in the password\n"
                    + "                          (default=0)\n"
                    + "    -s SYMBOLS, --symbols SYMBOLS\n"
                    + "                          insert SYMBOLS random symbols in the password\n"
                    + "                          (default=0)\n");
			return;
          case "-w":
					case "--words":
            i++;
            numWords = Integer.parseInt(args[i]);
            break;
          case "-c":
					case "--caps":
            i++;
            capitals = Integer.parseInt(args[i]);
            break;
          case "-n":
					case "--numbers":
            i++;
            numbers = Integer.parseInt(args[i]);
            break;
          case "-s":
					case "--symbols":
            i++;
            symbols = Integer.parseInt(args[i]);
            break;

          default:
            System.out.println("Invalid command, look at -h for more info");
            return;
        }
      } catch (NumberFormatException e) {
        System.out.println(e.getLocalizedMessage() + "\n Must enter a positive integer less than 4");
      } 
    }

    String specialChars = "~!@#$%^&*.:;";

    //Create new random num generator
    Random rand = new Random();
    String password = "";

    int j = 0;
    while (j < numWords) {
		boolean addSymbolOrNumber = true;
        while (addSymbolOrNumber && (numbers > 0 || symbols > 0)) {
        	//Add a number or add a symbol
          boolean addNumber = rand.nextBoolean();
          if (addNumber && numbers > 0) {
            int number = rand.nextInt(10);
            password = password + number;
            numbers--;
          } else if (symbols > 0){
            int randomIndex = rand.nextInt(specialChars.length());
            String randomSymbol = specialChars.substring(randomIndex, randomIndex+1);
            password = password + randomSymbol;
            symbols--;
          }
          addSymbolOrNumber = rand.nextBoolean();
        }

      String word = wordList.get(rand.nextInt(wordList.size()));
      if (!password.contains(word)) {
        if (capitals > 0) {
          boolean capitalize = rand.nextBoolean();
          //If capitalize is true or if the num capitals is greater than or equal
          //to numWords remaining
          if (capitalize || capitals >= numWords - j) {
            word = word.substring(0, 1).toUpperCase() + word.substring(1);
            capitals--;
          }
        }
        password = password + word;
        j++;
      }
    }
    while (numbers > 0 || symbols > 0) {
      boolean addNumber = rand.nextBoolean();
      if (addNumber && numbers > 0) {
        int number = rand.nextInt(10);
        password += number;
        numbers--;
      } else if (symbols > 0){
        int randomIndex = rand.nextInt(specialChars.length());
        String symbol = specialChars.substring(randomIndex, randomIndex+1);
        password = password + symbol;
        symbols--;
      }
    }
    System.out.println(password);
  }

}
