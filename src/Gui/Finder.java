package Gui;

import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Finder {
    static String[] linesArray;

    public static boolean findUser(String username) {
        readUsersDoc();
        if (linesArray == null) {
            return false;
        }

        for (String user : linesArray) {
            // user = user.substring(0, user.length() - 1);
            System.out.println(user + "\n");
            if (user.equals(username)) {
                return true;
            }
        }
        return false;
    }

    public static void createUserDoc(String username) {
        String fileName = "D:/Programacion/Java/chatApp/resources/Users.txt";
        String content = username + "\n";

        try {
            FileWriter writer = new FileWriter(fileName, true); // Pass 'true' to enable append mode
            writer.write(content);
            writer.close();
            System.out.println("Content appended to the text file.");
        } catch (IOException e) {
            System.out.println("An error occurred while appending content to the text file.");
            e.printStackTrace();
        }
    }

    public static void readUsersDoc() {
        String filePath = "D:/Programacion/Java/chatApp/resources/Users.txt";
        File file = new File(filePath);

        List<String> lines = new ArrayList<>();

        try (FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                // Add each line to the list
                lines.add(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Convert the list to a String[] array
        linesArray = lines.toArray(new String[0]);

    }
}
