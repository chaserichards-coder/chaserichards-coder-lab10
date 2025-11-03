import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.*;

public class WordCounter {
public static int processText(StringBuffer text, String stopword) throws TooSmallText, InvalidStopwordException {
        //need count here which is what we will be returning
        int count = 0;
        //need a boolean to see if stopword is found in the text
        boolean stopwordFound = (stopword == null);

        //need this to say what our word will loo like
        Matcher matcher = Pattern.compile("\\b[a-zA-Z0-9']+\\b").matcher(text.toString());
        while (matcher.find()) {
            String word = matcher.group();
            count++;
            //if stopword is not null and we match our word with our stopword, then we have found the stopword
            if (stopword != null && word.equals(stopword)) {
                stopwordFound = true;
                // stop counting here if we have enough words
                if (count >= 5) {
                break;
                }
            }
        }
        // checks if count is less than 5, if it is it will throw a toosmalltext exception
        if (count < 5) {
            throw new TooSmallText(count);
        }
        //checks if stopword found is false, or stopword is null, then throw invalid stopword exception.
        if (stopword != null && !stopwordFound) {
            throw new InvalidStopwordException(stopword);
        }
        //will finally return count
        return count;
    }



    public static StringBuffer processFile(String path) throws EmptyFileException {
        //creates a file that represents the file located at the path.
        File file = new File(path);
        //Scanner that will be used to read all contents of the file line by line
        Scanner scanner;

        while (true) {
            try {
                //tries to create a scanner that reads from file
                scanner = new Scanner(file);
                //if it works then we will break out the loop, which means the file exists and can be read.
                break; // success
            } catch (FileNotFoundException e) {
                // Prompt user to re-enter file path
                //using flush to make sure that the prompt will correctly appear.
                System.out.flush();
                //creates a scanner input that will input from the user.
                Scanner input = new Scanner(System.in);
                //reads the next line typed by user and then updates path.
                path = input.nextLine();
                //creates a new file with path we read from the previous line. 
                //loop will try again based on what file equals this time around.
                file = new File(path);
            }
        }
        //Stringbuffer sb will store the contents of the file as we read through it.
        StringBuffer sb = new StringBuffer();
        //loops through every line in the file, hasnextline is true as long as there are more lines in the file.
        while (scanner.hasNextLine()) {
            //if sb has text in it, it will append space before adding the next line.
            if (sb.length() > 0) sb.append(" ");
            //  adds the next line that we see in the scanner.
            sb.append(scanner.nextLine());
        }
        //closes when there are no more lines.
        scanner.close();
        // if sb has nothing in it, that means the scanner isnt reading anything from the file, which means the file is empty,
        //which is why we throw the EmptyFileException.
        if (sb.length() == 0) throw new EmptyFileException(path);
    
        return sb;
    }
    
    //Need this to pass test case
    private static String getFileFromUser() {
        Scanner input = new Scanner(System.in);
        return input.nextLine();
    }
    
    public static void main(String[] args) {
        //creates a scanner that reads input from the user.
        Scanner input = new Scanner(System.in);
        //default stopword is null
        String stopword = null;
        //text is created as a stringbuffer that will store the text
        StringBuffer text = new StringBuffer();
    
        //checks if program was run with more than 1 argument
        if (args.length > 1) {
            //If so then the stopword is equal to the second argument.
            stopword = args[1];
        }
    
        try {
            // Determine if first argument is a file or direct text
            if (args.length > 0 && new File(args[0]).exists()) {
                text = processFile(args[0]);
            } else if (args.length > 0) {
                text = new StringBuffer(args[0]);
            } else {
                // No arguments: ask user to enter text
                System.out.println("Enter text to process:");
                text = new StringBuffer(input.nextLine());
            }
    
            int count = 0;
            boolean done = false;
            //loops until text is processed correctly or a stopword exception is finally resolved.
            while (!done) {
                try {
                    // processtext is called to count words up to stopword
                    count = processText(text, stopword);
                    done = true;
                } catch (TooSmallText e) {
                    //if text has too few words it will  throw toosmalltext
                    System.out.println("TooSmallText: Only found " + e.getCount() + " words.");
                    done = true; // Stop after reporting

                } catch (InvalidStopwordException e) {
                    // Give user one chance to re-enter stopword
                    //and if stopword is not found it will throw stopword not found exception.
                    System.out.println("Stopword not found. Please enter a new stopword:");
                    stopword = input.nextLine();
                }
            }
    
            // Only print count if enough words were found
            if (count >= 5) {
                System.out.println("Found " + count + " words.");
            }
    
        } catch (EmptyFileException e) {
            // If file is empty, report but continue with empty text
            System.out.println("EmptyFileException: " + e.getMessage());
            text = new StringBuffer("");
            try {
                processText(text, stopword);
            } catch (TooSmallText ex) {
                System.out.println("TooSmallText: Only found " + ex.getCount() + " words.");
            } catch (InvalidStopwordException ex) {
                System.out.println("InvalidStopwordException: " + ex.getMessage());
            }
        }
    }
    
}    