package quiz_java;


//QuestionServerWithGUI.java

import javax.swing.*;

import java.awt.BorderLayout;
import java.io.*;
import java.net.*;

public class QServerGUI {
 private static final int PORT = 12345;
 private static final String[] QUESTIONS = {
     // ... same questions as before ...
		 "Who was the first person to walk on the moon?",
		    "What is the capital of France?",
		    "What is the largest ocean on Earth?",
		    "Who wrote the novel '1984'?",
		    "What is the square root of 81?"
 };

 private JFrame frame;
 private JTextArea textArea;

 public QServerGUI() {
     frame = new JFrame("Question Server");
     textArea = new JTextArea(20, 50);
     textArea.setEditable(false);
     JScrollPane scrollPane = new JScrollPane(textArea);
     frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     frame.pack();
     frame.setVisible(true);
 }

 public void startServer() {
     try (ServerSocket serverSocket = new ServerSocket(PORT)) {
         textArea.append("Server started. Waiting for a client...\n");
         try (Socket clientSocket = serverSocket.accept();
              PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
              BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
             
             textArea.append("Client connected.\n");
             
             for (int i = 0; i < QUESTIONS.length; i++) {
                 out.println(QUESTIONS[i]); // Send question to client
                 String answer = in.readLine(); // Read answer from client
                 textArea.append("Client answered: " + answer + "\n");
             }
             
             out.println("END"); // Signal the end of the quiz
         }
     } catch (IOException e) {
         e.printStackTrace();
     }
 }

 public static void main(String[] args) {
     SwingUtilities.invokeLater(() -> {
    	 QServerGUI  server = new QServerGUI();
         server.startServer();
     });
 }
}
