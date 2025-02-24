import addition.BotUser;
import addition.Button;
import addition.State;
import org.telegram.telegrambots.meta.api.objects.Message;

public class MadeOrderMessageHandler extends MessageHandler {
    protected MadeOrderMessageHandler(BotUser botUser) {
        super(botUser);
    }

    @Override
    public void handle(Message message, String buttonData) {
        Button button = Button.getButtonOnNames(buttonData);
        if (button != null && button.equals(Button.YES)) {
            sendMessage(chatId, "Прокрутите вверх и выберите цветок нажатие соответствующей кнопки. " +
                    "Для отмены выбора воспользуйтесь меню");
            botUser.setCurrantState(State.SELECT_FLOWER);
        } else {
            botUser.setCurrantState(State.SELECT_QUESTION);
            MessageHandler messageHandler = new QuestionMessageHandler(botUser);
            messageHandler.handle(message, buttonData);
        }
    }

}
