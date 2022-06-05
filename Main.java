import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    private static int numberOfSection = 4;
    private static int numberOfThread = 4;

    public static void main(String[] args) throws IOException {
        String input = new String();

        input = readFile();

        ArrayList<WordWithLine>[] sections = new ArrayList[numberOfSection];
        for (int i = 0; i < numberOfSection; i++) {
            sections[i] = new ArrayList<WordWithLine>();
        }

        devideData(input, numberOfSection, sections);

        // ExecutorService excecutor = Executors.newFixedThreadPool(numberOfThread);

        // for (int i = 0; i < numberOfThread; i++) {
        // excecutor.submit();
        // }
        // excecutor.shutdownNow();

        // excecutor.awaitTermination(1, TimeUnit.DAYS);
    }

    public static void devideData(String input, int section, ArrayList<WordWithLine>[] sections) {
        String[] words = input.split("\\s+");
        String[] lines = input.split("\r\n|\r|\n");
        int devideLength = words.length / section;
        int countWord = 0;
        int sectionCount = 0;

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            String[] wordsInLine = line.split("\\s+");
            for (String w : wordsInLine) {
                WordWithLine wordWithLine = new WordWithLine();
                if (countWord >= (devideLength * (sectionCount + 1)) && sectionCount != section - 1) {
                    sectionCount++;
                }
                wordWithLine.set(w, i + 1);
                sections[sectionCount].add(wordWithLine);
                countWord++;
            }
        }
    }

    public static String readFile() throws IOException {
        Path fileName = Path.of("./input2.txt");
        return Files.readString(fileName);
    }
}