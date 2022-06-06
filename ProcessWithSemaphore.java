import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class ProcessWithSemaphore implements Runnable {
    private ArrayList<WordWithLine> section;
    private ArrayList<WordWithLine> history;
    private int id;
    private String[] search;
    private Semaphore sem;
    private File output;

    public ProcessWithSemaphore(int id, ArrayList<WordWithLine> section, String[] search,
            Semaphore sem,
            ArrayList<WordWithLine> history, File output) {
        this.section = section;
        this.id = id;
        this.search = search;
        this.sem = sem;
        this.history = history;
        this.output = output;
    }

    @Override
    public void run() {

        for (int i = 0; i < section.size(); i++) {
            LocalTime time = java.time.LocalTime.now();
            if (check(section.get(i).getWord())) {
                try {
                    sem.acquire();
                } catch (InterruptedException e1) {
                }
                try {
                    int index = checkHistory(section.get(i));
                    if (index != -1) {
                        if (index != -2) {
                            replaceInOutput(index, section.get(i));
                            history.remove(index);
                            history.add(section.get(i));
                            writeToFile(index, section.get(i), time);
                        }
                    } else {
                        history.add(section.get(i));
                        writeToFile(index, section.get(i), time);
                    }
                } catch (IOException e) {
                } finally {
                    sem.release();
                }
            }
        }

    }

    private void writeToFile(int index, WordWithLine obj, LocalTime time) throws IOException {
        String str = Files.readString(output.toPath());
        String[] lines = str.split("\r\n|\r|\n");

        PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
        writer.print("");
        writer.close();

        PrintWriter myWriter = new PrintWriter("output.txt", "UTF-8");

        for (int i = 0; i < lines.length; i++) {
            myWriter.println(lines[i]);
        }
        myWriter.println("word : " + obj.getWord() + " line = " + obj.getLine()
                + " nThread = " +
                this.id +
                " Time of find word : " + time + " Time of write in file : "
                + java.time.LocalTime.now());
        myWriter.close();
    }

    private void replaceInOutput(int index, WordWithLine obj) throws IOException {
        String str = Files.readString(output.toPath());
        String[] lines = str.split("\r\n|\r|\n");

        PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
        writer.print("");
        writer.close();

        PrintWriter myWriter = new PrintWriter("output.txt", "UTF-8");

        String lineToRemove = "word : " + obj.getWord() + " line = " + history.get(index).getLine();

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains(lineToRemove)) {
                continue;
            }
            myWriter.println(lines[i]);
        }
        myWriter.close();
    }

    private int checkHistory(WordWithLine obj) {
        for (int i = 0; i < history.size(); i++) {
            if (history.get(i).getWord().equals(obj.getWord())) {
                if (history.get(i).getLine() > obj.getLine()) {
                    return i;
                } else {
                    return -2;
                }
            }
        }
        return -1;
    }

    private boolean check(String word) {
        for (int i = 0; i < search.length; i++) {
            if (word.equals(search[i].toLowerCase())) {
                return true;
            }
        }
        return false;
    }

}