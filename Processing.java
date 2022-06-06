import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.time.LocalTime;
import java.util.ArrayList;

public class Processing {
    private ArrayList<WordWithLine> section;
    private ArrayList<WordWithLine> history;
    private String[] search;
    private File output;

    public Processing(ArrayList<WordWithLine> sections, String[] search,
            ArrayList<WordWithLine> history, File output) {
        this.section = sections;
        this.search = search;
        this.history = history;
        this.output = output;
    }

    public void process() throws IOException {
        for (int i = 0; i < section.size(); i++) {
            LocalTime time = java.time.LocalTime.now();
            if (check(section.get(i).getWord())) {
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
                + " Process" +
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

    private boolean check(String word) {
        for (int i = 0; i < search.length; i++) {
            if (word.equals(search[i].toLowerCase())) {
                return true;
            }
        }
        return false;
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
}
