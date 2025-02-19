import addition.*;
import org.telegram.telegrambots.meta.api.objects.Message;

public class TelephoneMessageHandler extends MessageHandler{
    protected TelephoneMessageHandler(BotUser botUser) {
        super(botUser);
    }

    @Override
    public void handle(Message message, String buttonData) {
        if (message == null || message.getFrom().getIsBot() || message.getText() == null) return;
        String text = message.getText()
                .trim()
                .replaceAll("\s", "")
                .replaceAll("-", "")
                .replaceAll("[)]", "")
                .replaceAll("[(]", "");
        if (text.length() == 11 && text.startsWith("80")) {
            text = text.substring(2);
        }
        if (!text.startsWith("+375")) {
            text = "+375" + text;
        }
        if (text.matches("^[+][0-9]{12}$")) {
            addStateId(message);
            botUser.getOrder().setTelephone(text);
            if(!botUser.getTelephone().equals(text)) {
                sendMessage(AppProperties.COLLECTOR_CHAT_ID,
                        "@" + botUser.getUserName() + " тел: " + text +
                                " Id: " + chatId + " " + botUser.getFirstName()+ " " + getBotUsername());
            }
            botUser.setTelephone(text);
            clean();
            if(botUser.getLastState() == State.INPUT_QUESTION){
                sendMessage(AppProperties.ORDERS_CHAT_ID, "Вопрос:\n "
                        + botUser.getCurrantQuestion()
                        + "\nTelegram: " + " https://t.me/" + botUser.getUserName() + "\nТелефон: " + botUser.getTelephone());
                sendMessage(chatId, "Вопрос принят. Наш оператор свяжется с Вами в ближайшее время.");
                botUser.setCurrantQuestion("");
                botUser.setCurrantState(State.SELECT_QUESTION);
            } else if(botUser.getLastState() == State.INPUT_ADDRESS){
                addStateId(sendMessage(chatId, "Выберите материал для упаковки", WrapperButton.getNames()));
                botUser.setCurrantState(State.INPUT_WRAPPER);
            } else {
                sendMessageWithRowButton(chatId, "Выберите действие", ActButton.getStartButton(), 2, false);
                botUser.setCurrantState(State.START);
            }
        } else {
            addId(message);
            addId(sendMessage(chatId, "Введите пожалуйста телефон в формате YY XXX XX XX"));
        }
    }

    @Override
    public boolean cancel() {
        cleanState(State.INPUT_ADDRESS);
        botUser.setCurrantState(State.INPUT_ADDRESS);
        return true;
    }
}
