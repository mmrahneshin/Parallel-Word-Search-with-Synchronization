import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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
        String inputWord = new String();

        input = readFile("./input2.txt");
        inputWord = readFile("./inputWord2.txt");
        String[] words = inputWord.split("\\s+");

        ArrayList<WordWithLine>[] sections = new ArrayList[numberOfSection];
        ArrayList<WordWithLine> history = new ArrayList<WordWithLine>();
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
        PrintWriter myWriter = new PrintWriter("output.txt", "UTF-8");

        for (int i = 0; i < numberOfThread; i++) {
            excecutor.submit(new Mutex(i, sections[i], words, mutex, myWriter, history));
        }

        excecutor.shutdown();
        excecutor.awaitTermination(1, TimeUnit.DAYS);
        myWriter.close();

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

    public static String readFile(String path) throws IOException {
        Path fileName = Path.of(path);
        return Files.readString(fileName);
    }

}