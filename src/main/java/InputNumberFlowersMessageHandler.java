import addition.ActButton;
import addition.BotUser;
import addition.State;
import org.telegram.telegrambots.meta.api.objects.Message;

public class InputNumberFlowersMessageHandler extends MessageHandler{

    public InputNumberFlowersMessageHandler(BotUser botUser) {
        super(botUser);
    }

    @Override
    public void handle(Message message, String buttonData) {
        if (message.getFrom().getIsBot()) return;
        if (message.getText().matches("[0-9]+")) {
            clean();
            int countOfFlower = Integer.parseInt(message.getText());
            if (countOfFlower> 0) {
                botUser.addFlower(countOfFlower);
                addStateId(message);
                addId(sendMessageWithRowButton(chatId, ActButton.CANCEL_REGISTRATION.getReplay(), ActButton.getContinualButton()));
                botUser.setCurrantState(State.SELECT_ACT);
            }else{
                addId(message);
                addId(sendMessage(chatId, "Количество цветов должно быть больше нуля"));
            }
        } else {
            addId(message);
            addId(sendMessage(chatId, "Не понимаю Вас, напишите число"));
        }
    }

    @Override
    public boolean cancel() {
        System.out.println(botUser.getCurrantState() + "cancel");
        cleanState(State.SELECT_FLOWER);
        botUser.setCurrantState(State.SELECT_FLOWER);
        addId(sendMessage(chatId, "Прокрутите вверх и выберите другой цветок."));
        return true;
    }
}
