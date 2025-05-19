import java.util.UUID;

public class Messages {
    private final String recipient;
    private final String messageText;
    private final String messageID;
    private final int messageHash;

    public Messages(String recipient, String messageText) {
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageID = UUID.randomUUID().toString();
        this.messageHash = messageText.hashCode();
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessageID() {
        return messageID;
    }

    public int getMessageHash() {
        return messageHash;
    }
}
