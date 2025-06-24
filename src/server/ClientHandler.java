package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;


 

public class ClientHandler implements Runnable {
    private Socket socket; // Connection to the client

    //accepts the clients socket connection
    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    
    public void run() {
        try (
            
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String inputLine;

            
            while ((inputLine = in.readLine()) != null) {
                System.out.println("📨 Received: " + inputLine);         //listen for messages from the client

                
                String[] parts = inputLine.split(":");             // Split the message by ":"
                String command = parts[0];

                
                
                if (command.equals("SIGNUP") && parts.length == 3) {
                    String user = parts[1];
                    String pass = parts[2];

                    if (Userdatabase.userExists(user)) {
                        out.println("FAIL:Username already exists");
                    } else {
                        boolean success = Userdatabase.addUser(user, pass);
                        out.println(success ? "SUCCESS:Account created" : "FAIL:Could not save user");
                    }
                }

                
                
                else if (command.equals("LOGIN") && parts.length == 3) {     //LOGIN:username:password
                    String user = parts[1];
                    String pass = parts[2];

                    if (Userdatabase.validateLogin(user, pass)) {
                    
                        Userdata data = Userdatabase.loadUserData(user);
                        out.println("SUCCESS:Login successful");
                    } else {
                        out.println("FAIL:Invalid credentials");
                    }
                }
                else if (command.equals("UPDATE_STATUS") && parts.length == 8) {
    String theName = parts[1];
    String age = parts[2];
    String gender = parts[3];
    String freq = parts[4];
    String cals = parts[5];
    String goal = parts[6];
    String advice = parts[7];

    // Save status data
    boolean success = Userdatabase.updateStatus(theName, age, gender, freq, cals, goal, advice);

    if (success) {
        out.println("SUCCESS:Status updated");
    } else {
        out.println("FAIL:Could not update status");
    }
}
else if (command.equals("UPDATE_STATUS_PROGRAM") && parts.length == 5) {
    String user = parts[1];
    String goal = parts[2];
    String macros = parts[3].replace("\\n", "\n");
    String sampleMeals = parts[4].replace("\\n", "\n");

    Userdata data = Userdatabase.loadUserData(user);
    data.goal = goal;
    data.macros = macros;
    data.sampleMeals = sampleMeals;

    Userdatabase.saveUserData(user, data);
    out.println("SUCCESS:Program status updated");
}
else if (command.equals("UPDATE_STATUS_BMI") && parts.length == 4) {
    String username = parts[1];
    double bmiValue = Double.parseDouble(parts[2]);
    String category = parts[3];

    Userdata data = Userdatabase.loadUserData(username);
    data.bmiValue = bmiValue;
    data.bmiCategory = category;
    Userdatabase.saveUserData(username, data);

    out.println("SUCCESS:BMI updated");
}

else if (command.equals("GET_STATUS") && parts.length == 2) {
    String username = parts[1];
   Userdata data = Userdatabase.loadUserData(username);
if (data == null) {
    data = new Userdata();
}
ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
oos.writeObject(data);
oos.flush();

}

                
                else {
                    out.println("FAIL:Unknown command");
                }
            }

        } catch (IOException e) {
            e.printStackTrace(); 
        }
    }
}

