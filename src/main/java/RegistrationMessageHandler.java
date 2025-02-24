import addition.*;
import org.telegram.telegrambots.meta.api.objects.Message;

public class RegistrationMessageHandler extends MessageHandler {
    protected RegistrationMessageHandler(BotUser botUser) {
        super(botUser);
    }

    @Override
    public void handle(Message message, String buttonData) {
        Button button = Button.getButtonOnNames(buttonData);
        clean();
        if (!message.getFrom().getIsBot()) addId(message);
        if (botUser.getFlowersSet().getFlowers() != null && botUser.getFlowersSet().getFlowers().size() > 0) {
            botUser.getOrder().addFlowerSet(
                    new FlowersSet(
                            botUser.getFlowersSet().getFlowers(),
                            botUser.getFlowersSet().getCountSet(),
                            botUser.getFlowersSet().isInputSetNumber()));
            botUser.getFlowersSet().getFlowers().clear();
        }
        if (button != null && button.equals(Button.YES)) {
            addStateId(sendMessage(chatId, "Когда? Укажите дату доставки (с "
                    + AppProperties.get().getFirstDay() + " по " + AppProperties.get().getLastDay() + ")"));
            botUser.setCurrantState(State.INPUT_DATE);
        } else if (button != null && button.equals(Button.NO)) {
            cancel();
        } else {
            addId(sendMessage(chatId, "Не понимаю Вас. Ответьте, пожалуйста да или нет."));
        }
    }

    @Override
    public boolean cancel() {
        cleanState(State.SELECT_ACT);
        botUser.setFlowersSet(botUser.getOrder().getLastSet());
        botUser.getOrder().delLastSet();
        addId(sendMessageWithRowButton(chatId, ActButton.CANCEL_REGISTRATION.getReplay(),
                ActButton.getNextSetButtons()));
        botUser.setCurrantState(State.SELECT_ACT);
        return true;
    }

}
