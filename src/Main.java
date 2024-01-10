import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;

public class Main {
    private static JTextField nicknameField;
    private static JPasswordField passwordField;

    public static void main(String[] args) {
        JFrame();
    }

    public static void JFrame() {
        JFrame frame = new JFrame("User Registration");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel userLabel = new JLabel("Nickname:");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        nicknameField = new JTextField(20);
        nicknameField.setBounds(100, 20, 165, 25);
        panel.add(nicknameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField(20);
        passwordField.setBounds(100, 50, 165, 25);
        panel.add(passwordField);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(10, 80, 80, 25);
        panel.add(registerButton);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(100, 80, 80, 25);
        panel.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginUser();
            }
        });
    }

    private static void registerUser() {
        String nickname = nicknameField.getText();
        char[] passwordChars = passwordField.getPassword();
        String password = new String(passwordChars);

        try {
            validateInput(nickname, password);
            saveCredentials(nickname, password);
            displayTextInFrame("Реєєстрація успішна! Добрий день " + nickname + "!");
        } catch (ShortNicknameException e) {
            displayTextInFrame("Помилка: " + e.getMessage());
        } catch (InvalidInputException e) {
            displayTextInFrame("Помилка: " + e.getMessage());
        }
    }

    private static void loginUser() {
        String enteredNickname = nicknameField.getText();
        char[] enteredPasswordChars = passwordField.getPassword();
        String enteredPassword = new String(enteredPasswordChars);

        String storedNickname = readStoredNickname();
        String storedPassword = readStoredPassword();

        if (enteredNickname.equals(storedNickname) && enteredPassword.equals(storedPassword)) {
            displayTextInFrame("Вхід успішний! З поверненням " + storedNickname + "!");
        } else {
            displayTextInFrame("Вхід не успішний :( Спробуйте знов");
        }
    }

    private static String readStoredNickname() {
        try {
            File myObj = new File("filename.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNext()) {
                String line = myReader.nextLine();
                if (line.startsWith("Nickname: ")) {
                    return line.substring(10);
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String readStoredPassword() {
        try {
            File myObj = new File("filename.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNext()) {
                String line = myReader.nextLine();
                if (line.startsWith("Password: ")) {
                    return line.substring(10);
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static void saveCredentials(String nickname, String password) {
        try {
            FileWriter myWriter = new FileWriter("filename.txt");
            myWriter.write("Nickname: " + nickname + "\n");
            myWriter.write("Password: " + password);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (Exception e) {
            System.out.println("An error occurred while saving credentials.");
            e.printStackTrace();
        }
    }

    private static void displayTextInFrame(String text) {
        JFrame textFrame = new JFrame("Message");
        textFrame.setSize(400, 100);
        textFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        textFrame.add(panel);

        JLabel label = new JLabel(text);
        panel.add(label);

        textFrame.setVisible(true);
    }

    public static class ShortNicknameException extends Exception {
        public ShortNicknameException(String message) {
            super(message);
        }
    }

    public static class InvalidInputException extends Exception {
        public InvalidInputException(String message) {
            super(message);
        }
    }

    private static void validateInput(String nickname, String password) throws InvalidInputException, ShortNicknameException {
        if (nickname.length() <= 5) {
            throw new ShortNicknameException("Ім'я користувача повинно бути більше 5 символів.");
        }
        if (password.length() <= 6) {
            throw new InvalidInputException("Пароль повинен бути більше 6 символів.");
        }
    }
}