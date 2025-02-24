import addition.BotUser;
import addition.State;
import org.telegram.telegrambots.meta.api.objects.Message;

public class AddressMessageHandler extends MessageHandler {
    public AddressMessageHandler(BotUser botUser) {
        super(botUser);
    }

    @Override
    public void handle(Message message, String buttonData) {
        if (message.getFrom().getIsBot()) return;
        addStateId(message);
        botUser.getOrder().setAddress(message.getText());
        addStateId(sendMessage(chatId, "Напишите телефон для обратной связи в формате YY XXX XX XX"));
        botUser.setCurrantState(State.INPUT_TELEPHONE);
    }

    @Override
    public boolean cancel() {
        cleanState(State.SELECT_TIME);
        botUser.setCurrantState(State.SELECT_TIME);
        return true;
    }
}
