import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class Mutex implements Runnable {
    private ArrayList<WordWithLine> section;
    private int id;
    private String search;
    private ReentrantLock mutex;

    public Mutex(int id, ArrayList<WordWithLine> section, String search, ReentrantLock mutex) {
        this.section = section;
        this.id = id;
        this.search = search.toLowerCase();
        this.mutex = mutex;
    }

    @Override
    public void run() {
        try (PrintWriter myWriter = new PrintWriter("output.txt", "UTF-8")) {
            for (int i = 0; i < section.size(); i++) {
                LocalTime time = java.time.LocalTime.now();
                if (section.get(i).getWord().equals(search)) {
                    mutex.lock();
                    try {
                        myWriter.println("line = " + section.get(i).getLine() + " nThread = " +
                                this.id +
                                " Time of find word : " + time + " Time of write in file : "
                                + java.time.LocalTime.now());
                    } finally {
                        mutex.unlock();
                    }

                }
            }
            myWriter.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
        }
    }

}
