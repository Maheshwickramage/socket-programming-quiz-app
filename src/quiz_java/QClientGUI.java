package quiz_java;

//QuestionClientWithGUI.java

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class QClientGUI {
 private static final String SERVER_ADDRESS = "localhost";
 private static final int SERVER_PORT = 12345;

 private JFrame frame;
 private JTextArea questionArea;
 private JTextField answerField;
 private JButton submitButton;
 private PrintWriter out;
 private BufferedReader in;

 public QClientGUI() {
     frame = new JFrame("Question Client");
     questionArea = new JTextArea(50, 50);
     questionArea.setEditable(true);
     JScrollPane questionScrollPane = new JScrollPane(questionArea);
     answerField = new JTextField(50);
     submitButton = new JButton("Submit Answer");

     frame.getContentPane().add(questionScrollPane, BorderLayout.NORTH);
     frame.getContentPane().add(answerField, BorderLayout.CENTER);
     frame.getContentPane().add(submitButton, BorderLayout.SOUTH);
     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     frame.pack();
     frame.setVisible(true);

     submitButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
             String answer = answerField.getText();
             if (answer != null && !answer.isEmpty()) {
                 out.println(answer); // Send answer to server
                 answerField.setText("");
             }
         }
     });

     answerField.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
             submitButton.doClick();
         }
     });
 }

 // Modify the connectToServer method
//Modify the connectToServer method
public void connectToServer() {
  try {
      Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
      out = new PrintWriter(socket.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

      new Thread(() -> {
          try {
              String fromServer;
              while ((fromServer = in.readLine()) != null) {
                  if ("END".equals(fromServer)) {
                      questionArea.append("Quiz finished. Thank you for participating!\n");
                      break;
                  } else if ("Correct".equals(fromServer) || "Incorrect".equals(fromServer)) {
                      questionArea.append("Your answer is " + fromServer + "\n");
                  } else {
                      String choices = in.readLine(); // Read choices from server
                      questionArea.append("Server asks: " + fromServer + "\n");
                      questionArea.append("Choices: " + choices.replace(",", ", ") + "\n");
                  }
              }
          } catch (IOException ex) {
              ex.printStackTrace();
          }
      }).start();
  } catch (IOException ex) {
      ex.printStackTrace();
  }
}
 public static void main(String[] args) {
     SwingUtilities.invokeLater(() -> {
    	 QClientGUI client = new QClientGUI();
         client.connectToServer();
     });
 }
}
