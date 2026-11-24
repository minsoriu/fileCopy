import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.Instant;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Введите имя первого исходного файла:");
        Path source1 = Paths.get(sc.nextLine());
        System.out.println("Введите путь первого конечного файла:");
        Path dest1 = Paths.get(sc.nextLine());
        System.out.println("Введите имя второго исходного файла:");
        Path source2 = Paths.get(sc.nextLine());
        System.out.println("Введите путь второго конечного файла:");
        Path dest2 = Paths.get(sc.nextLine());
        try {
            Instant startSeq = Instant.now();
            Files.copy(source1, dest1, StandardCopyOption.REPLACE_EXISTING);
            Files.copy(source2, dest2, StandardCopyOption.REPLACE_EXISTING);
            Instant endSeq = Instant.now();
            Duration durationSeq = Duration.between(startSeq, endSeq);
            System.out.println("Время последовательного копирования: " + durationSeq.toMillis() + " мс");

            Instant startPar = Instant.now();
            CopyFileThread thread1 = new CopyFileThread(source1, dest1);
            CopyFileThread thread2 = new CopyFileThread(source2, dest2);
            thread1.start();
            thread2.start();
            thread1.join();
            thread2.join();
            Instant endPar = Instant.now();
            Duration durationPar = Duration.between(startPar, endPar);
            System.out.println("Время параллельного копирования: " + durationPar.toMillis() + " мс");
        } catch (IOException e) {
            System.out.println("!!! Ошибка при работе с файлами: " + e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("Поток прервался :(");
        }
    }
}

class CopyFileThread extends Thread {
    Path sourceFile;
    Path destinationFile;

    CopyFileThread(Path sourceName, Path destinationName) {
        this.sourceFile = sourceName;
        this.destinationFile = destinationName;
    }

    @Override
    public void run() {
        try {
            Files.copy(sourceFile, destinationFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println("!!! Ошибка копирования в потоке: " + e.getMessage());
        }
    }
}