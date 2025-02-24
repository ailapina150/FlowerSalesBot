import addition.*;
import org.telegram.telegrambots.meta.api.objects.Message;

public class QuestionMessageHandler extends MessageHandler {

    protected QuestionMessageHandler(BotUser botUser) {
        super(botUser);
    }

    @Override
    public void handle(Message message, String buttonData) {
        if (message.getFrom().getIsBot()) {
            clean();
            FileLoader.readGoods();
            boolean menage = false;
            QuestionButton callBackButton = QuestionButton.getButtonOnName(buttonData);
            for (QuestionButton questionButton : QuestionButton.values()) {
                if (callBackButton != null && questionButton.equals(callBackButton)) {
                    menage = true;
                    sendMessage(chatId,
                            questionButton.getReplay(AppProperties.get().goods));
                    if (questionButton.equals(QuestionButton.CONNECTION)) {
                        botUser.setCurrantState(State.INPUT_QUESTION);
                    } else if (questionButton.equals(QuestionButton.ASSORTMENT)) {
                        showFlower();
                        sendMessage(chatId, "Готовы сделать заказ?", Button.getNames());
                        botUser.setCurrantState(State.MADE_ORDER);
                    } else if (questionButton.equals(QuestionButton.FOR_FREE)) {
                        sendMessage(chatId, "Показать инструкцию?", Button.getNames());
                        botUser.setCurrantState(State.INSTRUCTION);
                    }
                }
            }
            if (!menage) {
                MessageHandler messageHandler = new StartMessageHandler(botUser);
                messageHandler.handle(message, buttonData);
            }
        } else {
            addId(message);
            addId(sendMessage(chatId, "Выбери вопрос из списка или воспользуйся меню"));
        }
    }

    @Override
    public boolean cancel() {
        if (botUser.getLastState() == State.INSTRUCTION) {
            cleanState(State.INSTRUCTION);
            botUser.setCurrantState(State.INSTRUCTION);
            return true;
        }
        return false;
    }

}
