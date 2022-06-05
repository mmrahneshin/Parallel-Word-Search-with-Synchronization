import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws IOException {
        String input = new String();
        readFile(input);
        System.out.println(input);
        // ExecutorService excecutor = Executors.newFixedThreadPool(4);

        // for (int i = 0; i < 4; i++) {
        // excecutor.submit();
        // }
        // excecutor.shutdownNow();

        // excecutor.awaitTermination(1, TimeUnit.DAYS);
    }

    public static void readFile(String str) throws IOException {
        Path fileName = Path.of("./input.txt");
        str = Files.readString(fileName);
        System.out.println(str);
    }
}