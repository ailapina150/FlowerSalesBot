import addition.BotUser;
import addition.State;
import addition.TimeButton;
import org.telegram.telegrambots.meta.api.objects.Message;

public class TimeMessageHandler extends MessageHandler{
    protected TimeMessageHandler(BotUser botUser) {
        super(botUser);
    }

    @Override
    public void handle(Message message, String buttonData) {
        TimeButton button = TimeButton.getButtonOnName(buttonData);
        if (button != null) {
            for (TimeButton b : TimeButton.values()) {
                if (b.equals(button)) {
                    botUser.getOrder().setTime(b.getName());
                    clean();
                    if (message.getFrom().getIsBot()) {
                        addStateId(sendMessage(chatId, b.getName()));
                    }else{
                        addStateId(message);
                    }
                    addStateId(sendMessage(chatId, "ВАЖНО! Доставка только по БРЕСТУ. Напишите адрес"));
                    botUser.setCurrantState(State.INPUT_ADDRESS);
                }
            }
        } else {
            if (!message.getFrom().getIsBot()) addId(message);
            addId(sendMessage(chatId, "Выберите время нажатием соответствующей кнопки"));
        }
    }


    @Override
    public boolean cancel() {
        cleanState(State.INPUT_DATE);
        botUser.setCurrantState(State.INPUT_DATE);
        return true;
    }
}
