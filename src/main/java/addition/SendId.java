package addition;

import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.LocalDateTime;

public record SendId(Message message, State state, LocalDateTime dateTime) {
    public int getMessageId() {
        return message.getMessageId();
    }

    public String getText() {
        return message.getText();
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public State getState() {
        return state;
    }

}
