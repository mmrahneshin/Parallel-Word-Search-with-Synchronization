public class WordWithLine {
    private int line;
    private String word;

    public void set(String word, int line) {
        this.word = word;
        this.line = line;
    }

    public String getWord() {
        return this.word;
    }

    public int getLine() {
        return this.line;
    }
}
