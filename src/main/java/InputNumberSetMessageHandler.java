import addition.ActButton;
import addition.BotUser;
import addition.FlowersSet;
import addition.State;
import org.telegram.telegrambots.meta.api.objects.Message;

public class InputNumberSetMessageHandler extends MessageHandler {
    protected InputNumberSetMessageHandler(BotUser botUser) {
        super(botUser);
    }

    @Override
    public void handle(Message message, String buttonData) {
        if (message.getFrom().getIsBot()) return;
        if (message.getText().matches("[0-9]+")) {
            clean();
            int countSet = Integer.parseInt(message.getText());
            if (countSet > 0) {
                botUser.getOrder().addFlowerSet(new FlowersSet(botUser.getFlowersSet().getFlowers(), countSet, true));
                botUser.getFlowersSet().getFlowers().clear();
                addStateId(message);
                addId(sendMessageWithRowButton(chatId, ActButton.CANCEL_REGISTRATION.getReplay(), ActButton.getNextSetButtons()));
                botUser.setCurrantState(State.SELECT_ACT);
            } else {
                addId(message);
                addId(sendMessage(chatId, "Количество букетов должно быть больше нуля"));
            }
        } else {
            addId(message);
            addId(sendMessage(chatId, "Не понимаю Вас, напишите число"));
        }
    }

    @Override
    public boolean cancel() {
        System.out.println(botUser.getCurrantState() + "cancel");
        cleanState(State.SELECT_ACT);
        botUser.getFlowersSet().setCountSet(1);
        botUser.getFlowersSet().setInputSetNumber(false);
        cleanState(State.INPUT_NUMBER_FLOWERS);
        if (botUser.getFlowersSet().getFlowers().size() > 0) {
            botUser.removeLastFlower();
        }
        botUser.setCurrantState(State.INPUT_NUMBER_FLOWERS);
        return true;
    }
}
