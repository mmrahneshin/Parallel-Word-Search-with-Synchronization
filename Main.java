import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {

        ExecutorService excecutor = Executors.newFixedThreadPool(4);

        for (int i = 0; i < 4; i++) {
            excecutor.submit();
        }
    }
}