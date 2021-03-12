import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class ReadData {
    String text;
    String pattern;
}
public class SubstringMatching {

    static String uniqueChars = "";
    static String text;
    static String pattern;

    public static int FindSubstringMatch(int[][] dfa, String text) {
        System.out.println("Search for: " + pattern + " in " + text);
        int j = 1; // current pointer
        int k = 0; // end index to return
        int i = 0;
        int m = 0; // text increment
        boolean wholeMatch = false;
        while (i < text.length()) {
            int index = 0; // input count
            boolean charFound = false;
            for (int state = 0; state < text.length(); state++) {
                char ch = text.charAt(m);
                for (int ar = 0; ar < uniqueChars.length(); ar++) {
                    if (ch == uniqueChars.charAt(ar)) {
                        if (index == ar) {
                            if (dfa[state][i] == j) {
                                // matches character at index
                                System.out.println(dfa[state][i] + " : " + j + " : " + index + " : " + ar + " : "+ ch + " MATCH");
                                if (j == pattern.length()) {
                                    // once the last index is hit a match is found
                                    System.out.println("WHOLE MATCH FOUND");
                                    wholeMatch = true;
                                    break;
                                } else {
                                    j++;
                                    charFound = true;
                                }
                            } else {
                                // not a match for this letter send back to spot according to dfa
                                j = dfa[state][i];
                                charFound = true;
                            }
                        }
                    }
                    if (charFound) {
                        break;
                    }
                }
                if (wholeMatch) {
                    k = m + 1;
                    break;
                } else {
                    if (charFound) { // if char is found move states
                        if (j == 0) {
                            i = j;
                        } else {
                            i = j - 1;
                        }
                        m++;
                        break;
                    } else {
                        if (index == text.length() - 1) {
                            // at the end of the indexes no match = move to 0
                            j = 1;
                            i = 0;
                            m++;
                            break;
                        } else {
                            index++; // else continue through the inputs
                        }
                    }
                }
            }
            if (wholeMatch) {
                break;
            }
            if (m == text.length()) {
                System.out.println("end"); // end of the searched string
                break;
            }
        }
        if (wholeMatch) {
            k = k - pattern.length(); // locate starting index
        } else {
            k = -1;
        }
        return k;
    }

    public static int[][] BuildDFA(String pattern) {
        int[][] dfa = new int[uniqueChars.length()][pattern.length()];
        for(int i = 0; i < pattern.length(); i++) {
            int j = 0;
            while (j < uniqueChars.length()) {
              if (uniqueChars.charAt(j) == pattern.charAt(i)) {
                    dfa[j][i] = i + 1;
                } else {
                   dfa[j][i] = findLast(j, i, dfa);
                }
                j++;
            }
        }

        for(int i = 0; i < uniqueChars.length(); i++) {
            System.out.print(uniqueChars.charAt(i) + ": ");
            for(int j = 0; j < pattern.length(); j++) {
                System.out.print(dfa[i][j]);
            }
            System.out.println();
        }
        return dfa;
    }


    public static Integer findLast(int input, int currentState, int[][] dfa) {
        int lastState = 0;
        int updatedState;

        if (currentState < 2 ) {
            updatedState = 0;
        } else {
            updatedState = currentState - 1;
        }
        if (currentState < pattern.length()) {
            currentState = currentState + 1;
        }
        String sub = pattern.substring(0,currentState);
        String check = pattern.substring(updatedState, currentState); // check against sub
        int initCheckLen = check.length();
        if (currentState > 2) {
                check =  check.substring(0, initCheckLen - 1) + uniqueChars.charAt(input);
        }
        boolean cont = sub.contains(check);
        if (cont) {
            if (currentState > 2){
                check =  sub.substring(2, sub.length() - 1) + uniqueChars.charAt(input);
                if (sub.contains(check)) { // if sub contains a lookback version
                lastState = sub.lastIndexOf(uniqueChars.charAt(input)); // return the last index
            } else {
                lastState = sub.indexOf(uniqueChars.charAt(input)); // else return the first index
            }
            } else {
                lastState = sub.lastIndexOf(uniqueChars.charAt(input));
            }

            if (lastState == -1) {
                lastState = 0;
            }
        } else { // if sub does not contain value then return 0
            lastState = 0;
        }

        return dfa[input][lastState];
    }


     static ReadData readFile(String filename) throws FileNotFoundException {
        File file = new File(filename);
        Scanner read = new Scanner(file);
         String[] dataArray = new String[2];
         int i = 0;

        while (read.hasNextLine()) {
             dataArray[i] = read.nextLine();
            i++;
        }
        read.close();
        ReadData newData = new ReadData();
        newData.text = dataArray[0];
        newData.pattern = dataArray[1];

         return newData;
    }

    public static String UniqueCharacters(String pattern) {
        String result = "";
        pattern = pattern;
        for (int i = 0; i < pattern.length(); i++) {
            if(!result.contains(String.valueOf(pattern.charAt(i)))) {
                if (Character.isLetter(pattern.charAt(i))) {
                    result += String.valueOf(pattern.charAt(i));
                } else {
                    break;
                }
            }
        }
        return result;
    }

    public static void main(String[] args) throws FileNotFoundException {
        ReadData data = readFile(args[0]);
         text = data.text;
         pattern = data.pattern;

        uniqueChars = UniqueCharacters(pattern);

        int[][] dfa = BuildDFA(pattern);
        int k = FindSubstringMatch(dfa, text);
        if (k == -1) System.out.println("Substring " + pattern + " cannot be found in " + text);
        else System.out.println(text.substring(k, k+pattern.length()) + " at position " + k);
    }
}