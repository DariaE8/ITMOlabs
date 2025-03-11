package cli;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ArrayList;

// Чтение данных из файла необходимо реализовать с помощью класса java.io.BufferedInputStream
// Запись данных в файл необходимо реализовать с помощью класса java.io.BufferedOutputStream
public class DumpManager {
    String fname;
    // DumpManager dm = new DumpManager("db.csv", manager);
    public DumpManager(String fname) {
        //fname = db.csv
        this.fname = fname;
    }

    public DumpManager() {
        this("db.csv");
    }

    public void save(List<String> dump) {
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fname))) {
            for (String line : dump) {
                bos.write((line + "\n").getBytes(StandardCharsets.UTF_8));
            }
            System.out.println("CSV file written successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> load() {
         List<String> dump = new ArrayList<>();

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fname))) {
            StringBuilder sb = new StringBuilder();
            int byteRead;
            
            while ((byteRead = bis.read()) != -1) {
                char ch = (char) byteRead;
                
                if (ch == '\n') {
                    dump.add(sb.toString());
                    sb.setLength(0); // Clear StringBuilder for the next line
                } else {
                    sb.append(ch);
                }
            }
            
            // Add the last line if the file doesn't end with a newline
            if (sb.length() > 0) {
                dump.add(sb.toString());
            }

            System.out.println("CSV file read successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dump;
    }

}


