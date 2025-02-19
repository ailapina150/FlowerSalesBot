import addition.ActButton;
import addition.AppProperties;
import addition.BotUser;
import addition.State;
import org.telegram.telegrambots.meta.api.objects.Message;

public class SubmitMessageHandler extends MessageHandler{
    protected SubmitMessageHandler(BotUser botUser) {
        super(botUser);
    }

    @Override
    public void handle(Message message, String buttonData) {
        ActButton button = ActButton.getButtonOnName(buttonData);
        if (button != null) {
            switch (button) {
                case SEND -> {
                    sendMessage(AppProperties.ORDERS_CHAT_ID, message.getText() + "\n telegram: "
                            + " https://t.me/" + botUser.getUserName());
                    sendMessage(chatId, ActButton.SEND.getReplay());
                    sendMessageWithRowButton(chatId, "Создайте новый заказ или задайте вопрос",
                            ActButton.getFirstButton());
                    botUser.setCurrantState(State.START);
                }
                case CANCEL_REGISTRATION -> {
                    cleanState();
                    botUser.setFlowersSet(botUser.getOrder().getLastSet());
                    botUser.getOrder().delLastSet();
                    addId(sendMessageWithRowButton(chatId, ActButton.CANCEL_REGISTRATION.getReplay(),
                            ActButton.getNextSetButtons()));
                    botUser.setCurrantState(State.SELECT_ACT);
                }
            }
        } else {
            if (!message.getFrom().getIsBot()) addId(message);
            addId(sendMessage(chatId, "Подтвердите или отмените заказ"));
        }
    }

    @Override
    public boolean cancel() {
        cleanState(State.INPUT_WRAPPER);
        botUser.setCurrantState(State.INPUT_WRAPPER);
        return true;
    }
}
