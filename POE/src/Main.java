import javax.swing.JOptionPane;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static List<Messages> sentMessages = new ArrayList<>();
    private static int numMessagesSent = 0;
    private static login loggedInUser = null;

    public static void main(String[] args) {
        login login = new login();

        String firstName = JOptionPane.showInputDialog("Enter a First Name:");
        String lastName = JOptionPane.showInputDialog("Enter a Last Name:");

        String username;
        do {
            username = JOptionPane.showInputDialog("Enter a Username:");
            if (!username.contains("_") || username.length() > 5) {
                JOptionPane.showMessageDialog(null,
                        "Username is not correctly formatted. It must contain an underscore and be no longer than five characters.");
            }
        } while (!username.contains("_") || username.length() > 5);
        JOptionPane.showMessageDialog(null, "Username successfully captured.");

        String password;
        do {
            password = JOptionPane.showInputDialog("Enter a Password:");
            if (!isValidPassword(password)) {
                JOptionPane.showMessageDialog(null,
                        "Password must be at least 8 characters long, include an uppercase letter, a number, and a special character.");
            }
        } while (!isValidPassword(password));
        JOptionPane.showMessageDialog(null, "Password successfully captured.");

        String cellPhoneNumber;
        do {
            cellPhoneNumber = JOptionPane.showInputDialog("Enter your cellphone number with international country code (e.g. +27821234567):");
            if (validateCellphoneNumber(cellPhoneNumber)) {
                JOptionPane.showMessageDialog(null, "Cellphone number successfully added: " + cellPhoneNumber);
            } else {
                JOptionPane.showMessageDialog(null,
                        "Invalid format. Use format +[country code][number] (e.g. +27821234567), max 14 characters.");
            }
        } while (!validateCellphoneNumber(cellPhoneNumber));

        login.registerUser(firstName, lastName, username, password);
        loggedInUser = login;
        JOptionPane.showMessageDialog(null, "Registration successful!");

        if (loggedInUser != null) {
            startMessagingApplication();
        }
    }

    public static boolean validateCellphoneNumber(String cellPhoneNumber) {
        String regex = "^\\+[0-9]{1,13}$";
        return Pattern.matches(regex, cellPhoneNumber);
    }

    public static boolean isValidPassword(String password) {
        return password.length() >= 8 &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[0-9].*") &&
                password.matches(".*[!@#$%^&*()+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");
    }

    public static void startMessagingApplication() {
        JOptionPane.showMessageDialog(null, "Welcome to QuickChat, " + loggedInUser.getFirstName() + "!");

        while (true) {
            String[] options = {"Send Messages", "Show Sent Messages", "Logout"};
            int choice = JOptionPane.showOptionDialog(null, "Choose an option:",
                    "QuickChat Menu", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, options, options[0]);

            if (choice == 0) {
                sendMessages();
            } else if (choice == 1) {
                printMessages();
            } else if (choice == 2) {
                JOptionPane.showMessageDialog(null, "Logged out successfully.");
                loggedInUser = null;
                break;
            }
        }
    }

    private static void sendMessages() {
        int numToSend;
        try {
            numToSend = Integer.parseInt(JOptionPane.showInputDialog("Enter the number of messages you want to send:"));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid number. Returning to menu.");
            return;
        }

        for (int i = 0; i < numToSend; i++) {
            String recipient = JOptionPane.showInputDialog("Enter recipient's phone number (e.g., +27821234567):");
            if (!validateRecipientCell(recipient)) {
                JOptionPane.showMessageDialog(null, "Invalid recipient number.");
                continue;
            }

            String messageText = JOptionPane.showInputDialog("Enter your message (max 250 characters):");
            if (messageText.length() > 250) {
                JOptionPane.showMessageDialog(null, "Message too long. Please shorten it.", "Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            Messages newMessage = new Messages(recipient, messageText);
            sentMessages.add(newMessage);
            numMessagesSent++;

            String details = String.format("MessageID: %s\nMessage Hash: %s\nRecipient: %s\nMessage: %s",
                    newMessage.getMessageID(), newMessage.getMessageHash(), newMessage.getRecipient(), newMessage.getMessageText());

            JOptionPane.showMessageDialog(null, details, "Message Sent", JOptionPane.INFORMATION_MESSAGE);
        }

        JOptionPane.showMessageDialog(null, "Total messages sent: " + numMessagesSent);
    }

    public static boolean validateRecipientCell(String recipient) {
        String regex = "^\\+[0-9]{1,13}$";
        return Pattern.matches(regex, recipient);
    }

    public static void printMessages() {
        if (sentMessages.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No messages sent yet.");
            return;
        }

        StringBuilder allMessages = new StringBuilder("--- Sent Messages ---\n");
        for (Messages message : sentMessages) {
            allMessages.append("MessageID: ").append(message.getMessageID()).append("\n");
            allMessages.append("Hash: ").append(message.getMessageHash()).append("\n");
            allMessages.append("To: ").append(message.getRecipient()).append("\n");
            allMessages.append("Text: ").append(message.getMessageText()).append("\n");
            allMessages.append("---------------------\n");
        }

        JOptionPane.showMessageDialog(null, allMessages.toString());
    }
}
