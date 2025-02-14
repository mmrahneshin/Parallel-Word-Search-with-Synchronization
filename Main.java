import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    private static int numberOfSection = 6;
    private static int numberOfThread = 6;

    public static void main(String[] args) throws IOException, InterruptedException {
        long startTime = System.nanoTime();

        String input = new String();
        String inputWord = new String();

        input = readFile("./input.txt");
        inputWord = readFile("./inputWord.txt");
        String[] words = inputWord.split("\\s+");

        ArrayList<WordWithLine>[] sections = new ArrayList[numberOfSection];
        ArrayList<WordWithLine> history = new ArrayList<WordWithLine>();
        for (int i = 0; i < numberOfSection; i++) {
            sections[i] = new ArrayList<WordWithLine>();
        }

        devideData(input, numberOfSection, sections);

        ExecutorService excecutor = Executors.newFixedThreadPool(numberOfThread);

        File output = new File("output.txt");
        if (!output.createNewFile()) {
            output.delete();
            output.createNewFile();
        }
        // process with mutex
        // --------------------------------------------------------------
        // ReentrantLock mutex = new ReentrantLock();

        // for (int i = 0; i < numberOfThread; i++) {
        // excecutor.submit(new Mutex(i, sections[i], words, mutex, history, output));
        // }
        // process with mutex
        // --------------------------------------------------------------

        // process with semaphore
        // --------------------------------------------------------------
        Semaphore sem = new Semaphore(1);

        for (int i = 0; i < numberOfThread; i++) {
            excecutor.submit(new ProcessWithSemaphore(i, sections[i], words, sem,
                    history, output));
        }
        // process with semaphore
        // --------------------------------------------------------------

        excecutor.shutdown();
        excecutor.awaitTermination(1, TimeUnit.DAYS);

        // simple process
        // --------------------------------------------------------------
        // Processing p = new Processing(sections[0], words, history, output);

        // p.process();
        // simple process
        // --------------------------------------------------------------

        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;

        System.out.println(totalTime);
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