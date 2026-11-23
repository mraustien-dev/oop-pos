import java.io.*;
import java.util.*;
public class FileManager {
    private String folder;

    public FileManager(String folder) {
        this.folder = folder;
        new File(folder).mkdirs();
    }

    public boolean exists(String fileName) {
        return new File(folder, fileName).exists();
    }

    public ArrayList<String> readLines(String fileName) {
        ArrayList<String> lines = new ArrayList<>();
        File file = new File(folder, fileName);
        if (!file.exists()) {
            return lines;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Could not read " + fileName);
        }
        return lines;
    }

    public void writeLines(String fileName, ArrayList<String> lines) {
        File file = new File(folder, fileName);
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            for (String line : lines) {
                writer.println(line);
            }
        } catch (IOException e) {
            System.out.println("Could not save " + fileName + ". Close it if it is open in Excel.");
        }
    }
}
