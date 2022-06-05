import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class Mutex implements Runnable {
    private ArrayList<WordWithLine> section;
    private ArrayList<WordWithLine> history;
    private int id;
    private String[] search;
    private ReentrantLock mutex;
    private PrintWriter myWriter;
    private File output;

    public Mutex(int id, ArrayList<WordWithLine> section, String[] search, ReentrantLock mutex, PrintWriter myWriter,
            ArrayList<WordWithLine> history, File output) {
        this.section = section;
        this.id = id;
        this.search = search;
        this.mutex = mutex;
        this.myWriter = myWriter;
        this.history = history;
        this.output = output;
    }

    @Override
    public void run() {

        for (int i = 0; i < section.size(); i++) {
            LocalTime time = java.time.LocalTime.now();
            if (check(section.get(i).getWord())) {
                mutex.lock();
                try {
                    int index = checkHistory(section.get(i));
                    if (index != -1) {
                        if (index != -2) {
                            replaceInOutput(index, section.get(i));
                            history.remove(index);
                            history.add(section.get(i));
                        }
                    } else {
                        history.add(section.get(i));
                        myWriter.println("word : " + section.get(i).getWord() + " line = " + section.get(i).getLine()
                                + " nThread = " +
                                this.id +
                                " Time of find word : " + time + " Time of write in file : "
                                + java.time.LocalTime.now());
                    }
                } catch (IOException e) {
                } finally {
                    mutex.unlock();
                }

            }
        }

    }

    private void replaceInOutput(int index, WordWithLine obj) throws IOException {

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
            if (word.equals(search[i])) {
                return true;
            }
        }
        return false;
    }

}
