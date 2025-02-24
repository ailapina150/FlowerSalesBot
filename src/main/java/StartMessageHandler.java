import addition.*;
import org.telegram.telegrambots.meta.api.objects.Message;

public class StartMessageHandler extends MessageHandler {

    protected StartMessageHandler(BotUser botUser) {
        super(botUser);
    }

    @Override
    public void handle(Message message, String buttonData) {
        ActButton button = ActButton.getButtonOnName(buttonData);
        clean();

        if (button != null) {
            switch (button) {
                case QUESTION -> {
                    Integer messageId = sendMessageWithRowButton(chatId,
                            ActButton.QUESTION.getReplay(),
                            QuestionButton.getNames()).getMessageId();
                    pinMessage(chatId, messageId);
                    botUser.setCurrantState(State.SELECT_QUESTION);
                }
                case FOR_FREE -> {
                    sendMessage(chatId, ActButton.FOR_FREE.getReplay());
                    sendMessage(chatId, "Показать инструкцию?", Button.getNames());
                    botUser.setCurrantState(State.INSTRUCTION);
                }
                case ORDER, NEW_ORDER -> showFlower();
                default -> {
                    addId(message);
                    addId(sendMessage(chatId, "Выбери вопрос из списка или воспользуйся меню"));
                }
            }
        } else {
            sendMessage(chatId, "Создайте заказ или задайте вопрос");
        }
    }

    @Override
    public boolean cancel() {
        if (botUser.getLastState() == State.SUBMIT) {
            sendMessage(chatId, QuestionButton.CORRECT.getReplay());
        }
        return true;
    }

}
