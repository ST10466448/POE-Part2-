import javax.swing.JOptionPane;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;

public class Registration {
    private static List<Messages> sentMessages = new ArrayList<>();
    private static int numMessagesSent = 0;
    private static login loggedInUser = null;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        login login = new login();

        System.out.println("Enter a First Name:");
        String firstName = scanner.nextLine();

        System.out.println("Enter a Last Name:");
        String lastName = scanner.nextLine();

        String username;
        do {
            System.out.println("Enter a Username:");
            username = scanner.nextLine();
            if (!username.contains("_") || username.length() > 5) {
                JOptionPane.showMessageDialog(null,
                        "Username is not correctly formatted. It must contain an underscore and be no longer than five characters.");
            }
        } while (!username.contains("_") || username.length() > 5);
        JOptionPane.showMessageDialog(null, "Username successfully captured.");

// THEN PASSWORD
        String password;
        do {
            System.out.println("Enter a Password:");
            password = scanner.nextLine();
            if (!isValidPassword(password)) {
                JOptionPane.showMessageDialog(null,
                        "Password must be at least 8 characters long, include an uppercase letter, a number, and a special character.");
            }
        } while (!isValidPassword(password));
        JOptionPane.showMessageDialog(null, "Password successfully captured.");

        String cellPhoneNumber;
        do {
            System.out.println("Enter your cellphone number with international country code:");
            cellPhoneNumber = scanner.nextLine();
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
            startMessagingApplication(scanner);
        }

        scanner.close();
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

    public static void startMessagingApplication(Scanner scanner) {
        System.out.println("\nWelcome to QuickChat, " + loggedInUser.getFirstName() + "!");

        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Send Messages");
            System.out.println("2. Show recently sent messages");
            System.out.println("3. Logout");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    sendMessages(scanner);
                    break;
                case 2:
                    printMessages();
                    break;
                case 3:
                    System.out.println("Logged out successfully.");
                    loggedInUser = null;
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void sendMessages(Scanner scanner) {
        System.out.print("Enter the number of messages you want to send: ");
        int numToSend = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        for (int i = 0; i < numToSend; i++) {
            System.out.println("\n--- Sending Message " + (i + 1) + " ---");
            System.out.print("Enter recipient's phone number (e.g., +27821234567): ");
            String recipient = scanner.nextLine();
            if (!validateCellphoneNumber(recipient)) {
                System.out.println("Invalid recipient number.");
                continue;
            }

            System.out.print("Enter your message (max 250 characters): ");
            String messageText = scanner.nextLine();
            if (messageText.length() > 250) {
                JOptionPane.showMessageDialog(null, "Message too long. Please shorten it.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                continue;
            }

            Messages newMessage = new Messages(recipient, messageText);
            sentMessages.add(newMessage);
            numMessagesSent++;

            String details = String.format("MessageID: %s\nMessage Hash: %s\nRecipient: %s\nMessage: %s",
                    newMessage.getMessageID(), newMessage.getMessageHash(), newMessage.getRecipient(),
                    newMessage.getMessageText());

            JOptionPane.showMessageDialog(null, details, "Message Sent", JOptionPane.INFORMATION_MESSAGE);
        }

        JOptionPane.showMessageDialog(null, "Total messages sent: " + numMessagesSent);
    }

    public static void printMessages() {
        if (sentMessages.isEmpty()) {
            System.out.println("No messages sent yet.");
            return;
        }

        System.out.println("\n--- Sent Messages ---");
        for (Messages message : sentMessages) {
            System.out.println("MessageID: " + message.getMessageID());
            System.out.println("Hash: " + message.getMessageHash());
            System.out.println("To: " + message.getRecipient());
            System.out.println("Text: " + message.getMessageText());
            System.out.println("---------------------");
        }
    }
}
