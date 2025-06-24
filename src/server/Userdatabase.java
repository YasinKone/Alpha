package server;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class Userdatabase {
    private static final String FILE_PATH = "users.txt";
    private static final HashMap<String, String> users = new HashMap<>();
// File where usernames and hashed passwords are stored
    
    static {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");                          //Loads data from file
                if (parts.length == 2) {
                    users.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("User file not found — starting fresh.");
        }
    }

    public static synchronized boolean userExists(String username) {                    //validates login credentials
        return users.containsKey(username); 
    }

    public static synchronized boolean addUser(String username, String password) {
        if (users.containsKey(username)) return false;
        String hashed = Passwordutils.hashPassword(password);
        users.put(username, hashed);

        saveToFile();
        return true;
    }

    public static synchronized boolean validateLogin(String username, String password) {
        if (!users.containsKey(username)) return false;
 String hashedInput = Passwordutils.hashPassword(password);
 return users.get(username).equals(hashedInput);

    }
 
    private static void saveToFile() {                                                // Saves all users from the map to the users.txt file
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {                  
            for (Map.Entry<String, String> entry : users.entrySet()) {
                writer.println(entry.getKey() + ":" + entry.getValue());
            }
        } catch (IOException e) {
            System.out.println("Failed to save users.");
        }
    }

    public static Userdata loadUserData(String username) {              // Loads a users data from JSON file
    File file = new File("data/" + username + ".json");

    if (!file.exists()) {
        return new Userdata(); // Return empty data if new user
    }

    try (Reader reader = new FileReader(file)) {
        Gson gson = new Gson();
        return gson.fromJson(reader, Userdata.class);
    } catch (IOException e) {
        e.printStackTrace();
        return new Userdata(); 
    }
}

public static void saveUserData(String username, Userdata data) {                   // Saves a users data to a JSON file

    File folder = new File("data");
    if (!folder.exists()) folder.mkdir();

     File file = new File(folder, username + ".json");

    try (Writer writer = new FileWriter("data/" + username + ".json")) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        gson.toJson(data, writer);

        System.out.println("Saving to: " + file.getAbsolutePath());

    } catch (IOException e) {
        e.printStackTrace();
    }
}

public static boolean updateStatus(String name, String age, String gender, String freq,
                                   String cals, String goal, String advice) {
    try (PrintWriter out = new PrintWriter(new FileWriter("data/status.txt", true))) {
        out.println(name + "," + age + "," + gender + "," + freq + "," + cals + "," + goal + "," + advice);
        return true;
    } catch (IOException e) {
        e.printStackTrace();
        return false;
    }
}

}
