import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.*;

public class WordCounter {
public static int processText(StringBuffer text, String stopword) throws TooSmallText, InvalidStopwordException {
        int count = 0;
        boolean stopwordFound = (stopword == null);
    
        Matcher matcher = Pattern.compile("\\b[a-zA-Z0-9']+\\b").matcher(text.toString());
        while (matcher.find()) {
            String word = matcher.group();
            count++;
    
            if (stopword != null && word.equals(stopword)) {
                stopwordFound = true;
                // stop counting here if we have enough words
                if (count >= 5) {
                break;
                }
            }
        }
        if (count < 5) {
            throw new TooSmallText(count);
        }
        if (stopword != null && !stopwordFound) {
            throw new InvalidStopwordException(stopword);
        }
    
        return count;
    }



    public static StringBuffer processFile(String path) throws EmptyFileException {
        File file = new File(path);
        Scanner scanner;
        while (true) {
            try {
                scanner = new Scanner(file);
                break; // success
            } catch (FileNotFoundException e) {
                // Prompt user to re-enter file path
                System.out.flush();
                Scanner input = new Scanner(System.in);
                path = input.nextLine();
                file = new File(path);
            }
        }
    
        StringBuffer sb = new StringBuffer();
        while (scanner.hasNextLine()) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(scanner.nextLine());
        }
        scanner.close();
    
        if (sb.length() == 0) throw new EmptyFileException(path);
    
        return sb;
    }
    
    //Need this to pass test case
    private static String getFileFromUser() {
        Scanner input = new Scanner(System.in);
        return input.nextLine();
    }
    
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String stopword = null;
        StringBuffer text = new StringBuffer();
    
        // Check for stopword as second command line argument
        if (args.length > 1) {
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
    
            while (!done) {
                try {
                    count = processText(text, stopword);
                    done = true;
                } catch (TooSmallText e) {
                    System.out.println("TooSmallText: Only found " + e.getCount() + " words.");
                    done = true; // Stop after reporting
                } catch (InvalidStopwordException e) {
                    // Give user one chance to re-enter stopword
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