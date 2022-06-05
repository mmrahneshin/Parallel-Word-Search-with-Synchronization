import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    private static int numberOfSection = 4;
    private static int numberOfThread = 4;
    private static ReentrantLock mutex = new ReentrantLock();

    public static void main(String[] args) throws IOException, InterruptedException {
        String input = new String();

        input = readFile();

        ArrayList<WordWithLine>[] sections = new ArrayList[numberOfSection];
        for (int i = 0; i < numberOfSection; i++) {
            sections[i] = new ArrayList<WordWithLine>();
        }

        devideData(input, numberOfSection, sections);

        ExecutorService excecutor = Executors.newFixedThreadPool(numberOfThread);

        File output = new File("output.txt");
        try {
            output.createNewFile();
        } catch (IOException e) {
        }

        for (int i = 0; i < numberOfThread; i++) {
            excecutor.submit(new Mutex(i, sections[i], "The", mutex));
        }

        excecutor.shutdown();
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
                wordWithLine.set(w.toLowerCase(), i + 1);
                sections[sectionCount].add(wordWithLine);
                countWord++;
            }
        }
    }

    public static String readFile() throws IOException {
        Path fileName = Path.of("./input.txt");
        return Files.readString(fileName);
    }

}