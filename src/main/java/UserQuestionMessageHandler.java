import addition.AppProperties;
import addition.BotUser;
import addition.QuestionButton;
import addition.State;
import org.telegram.telegrambots.meta.api.objects.Message;

public class UserQuestionMessageHandler extends MessageHandler {

    protected UserQuestionMessageHandler(BotUser botUser) {
        super(botUser);
    }

    @Override
    public void handle(Message message, String buttonData) {
        QuestionButton callBackButton = QuestionButton.getButtonOnName(buttonData);
        if(callBackButton != null){
            botUser.setCurrantState(State.SELECT_QUESTION);
            MessageHandler messageHandler = new QuestionMessageHandler(botUser);
            messageHandler.handle(message,buttonData);
            return;
        }
        if (!message.getFrom().getIsBot()) {
            if (botUser.getTelephone().equals("")) {
                botUser.setCurrantQuestion(message.getText());
                sendMessage(chatId, "Напишите телефон для обратной связи в формате YY XXX XX XX:");
                botUser.setCurrantState(State.INPUT_TELEPHONE);
            } else {
                sendMessage(AppProperties.ORDERS_CHAT_ID, "Вопрос:\n "
                        + message.getText()
                        + "\n telegram: " + " https://t.me/" + botUser.getUserName() + " тел: " + botUser.getTelephone());
                sendMessage(chatId, "Вопрос принят. Наш оператор свяжется с Вами в ближайшее время.");
                botUser.setCurrantState(State.SELECT_QUESTION);
            }
        }

    }

}
