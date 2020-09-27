package readability;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {

    static final String splitWordsPattern = "[?!.,]?\\s+";
    static final String splitSentencesPattern = "[.!?]";
    static final Scanner in = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        String text = Files.readString(Path.of(args[0]));
        System.out.println("The text is:\n" + text);

        String[] words = text.split(splitWordsPattern);

        int wordCount = words.length;
        System.out.println("\nWords: " + wordCount);

        int sentenceCount = text.split(splitSentencesPattern).length;
        System.out.println("Sentences: " + sentenceCount);

        int charCount = text.replaceAll("\\s+", "").length();
        System.out.println("Characters: " + charCount);

        int syllablesCount = countTextSyllables(text);
        System.out.println("Syllables: " + syllablesCount);

        int polysyllablesWordsCount = countPolysyllablesWords(text);
        System.out.println("Polysyllables: " + polysyllablesWordsCount);

        System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
        String answer = in.next();
        System.out.println();
        switch (answer) {
            case "ARI":
                automatedReadabilityIndex(charCount, wordCount, sentenceCount);
                break;
            case "FK":
                fleschKincaidReadabilityTests(syllablesCount, wordCount, sentenceCount);
                break;
            case "SMOG":
                simpleMeasureOfGobbledygook(polysyllablesWordsCount, sentenceCount);
                break;
            case "CL":
                colemanLiauIndex(charCount, wordCount, sentenceCount);
                break;
            case "all":
                automatedReadabilityIndex(charCount, wordCount, sentenceCount);
                fleschKincaidReadabilityTests(syllablesCount, wordCount, sentenceCount);
                simpleMeasureOfGobbledygook(polysyllablesWordsCount, sentenceCount);
                colemanLiauIndex(charCount, wordCount, sentenceCount);
        }
    }

    static void automatedReadabilityIndex(int charCount, int wordCount, int sentenceCount) {
        double score = 4.71 * charCount / wordCount + 0.5 * wordCount / sentenceCount - 21.43;
        System.out.printf("Automated Readability Index: %.2f (about %s year olds).\n",
                score, readabilityScoreToAge(score));
    }

    static void fleschKincaidReadabilityTests(int syllablesCount, int wordCount, int sentenceCount) {
        double score = 0.39 * wordCount / sentenceCount + 11.8 * syllablesCount / wordCount - 15.59;
        System.out.printf("Flesch–Kincaid readability tests: %.2f (about %s year olds).\n",
                score, readabilityScoreToAge(score));
    }

    static void simpleMeasureOfGobbledygook(int polysyllablesWordsCount, int sentenceCount) {
        double score = 1.043 * Math.sqrt(polysyllablesWordsCount * 30.0 / sentenceCount) + 3.1291;
        System.out.printf("Simple Measure of Gobbledygook: %.2f (about %s year olds).\n",
                score, readabilityScoreToAge(score));
    }

    static void colemanLiauIndex(int charCount, int wordCount, int sentenceCount) {
        double L = 100.0 * charCount / wordCount;
        double S = 100.0 * sentenceCount / wordCount;
        double score = 0.0588 * L - 0.296 * S - 15.8;
        System.out.printf("Coleman–Liau index: %.2f (about %s year olds).\n",
                score, readabilityScoreToAge(score));
    }

    static String readabilityScoreToAge(double score) {
        switch ((int) Math.round(score)) {
            case 1:
                return "6";
            case 2:
                return "7";
            case 3:
                return "9";
            case 4:
                return "10";
            case 5:
                return "11";
            case 6:
                return "12";
            case 7:
                return "13";
            case 8:
                return "14";
            case 9:
                return "15";
            case 10:
                return "16";
            case 11:
                return "17";
            case 12:
                return "18";
            case 13:
                return "24";
            case 14:
                return "24";
            default:
                throw new IllegalStateException();
        }
    }

    public static int countPolysyllablesWords(String text) {
        String[] words = text.split(splitWordsPattern);
        int polysyllables = 0;
        for (String word : words) {
            if (isPolysyllablesWord(word)) {
                polysyllables++;
            }
        }
        return polysyllables;
    }

    private static boolean isPolysyllablesWord(String word) {
        return countWordSyllables(word) > 2;
    }

    public static int countTextSyllables(String text) {
        String[] words = text.split(splitWordsPattern);
        int syllables = 0;
        for (String word : words) {
            syllables += countWordSyllables(word);
        }
        return syllables;
    }

    private static int countWordSyllables(String word) {
        int syllableNumber = 0;
        for (int i = 0; i < word.length(); i++) {
            if (isVowel(word.charAt(i)) && (i == 0 || !isVowel(word.charAt(i - 1)))) {
                syllableNumber++;
            }
        }
        if (word.charAt(word.length() - 1) == 'e') {
            syllableNumber--;
        }
        if (syllableNumber == 0) {
            syllableNumber = 1;
        }
        return syllableNumber;
    }

    private static Boolean isVowel(char letter) {
        return "aAeEiIoOuUyY".contains(String.valueOf(letter));
    }
}
