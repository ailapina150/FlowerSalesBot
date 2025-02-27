import addition.ActButton;
import addition.BotUser;
import addition.State;
import addition.WrapperButton;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

public class WrapperMessageHandler extends MessageHandler {
    protected WrapperMessageHandler(BotUser botUser) {
        super(botUser);
    }

    @Override
    public void handle(Message message, String buttonData) {
        WrapperButton button = WrapperButton.getButtonOnNames(buttonData);
        if (button != null) {
            if (WrapperButton.CRAFT.equals(button)) {
                clean();
                botUser.getOrder().setWrapper(WrapperButton.CRAFT.getName());
                if (message.getFrom().getIsBot())
                    addStateId(sendMessage(chatId, WrapperButton.CRAFT.getName()));
                addStateId(sendMessageWithRowButton(chatId,
                        botUser.getOrder().toSend(),
                        List.of(ActButton.SEND.getName(), ActButton.CANCEL_REGISTRATION.getName())));
                botUser.setCurrantState(State.SUBMIT);
            }
            if (WrapperButton.FILM.equals(button)) {
                clean();
                botUser.getOrder().setWrapper(WrapperButton.FILM.getName());
                if (message.getFrom().getIsBot())
                    addStateId(sendMessage(chatId, WrapperButton.FILM.getName()));
                addStateId(sendMessageWithRowButton(chatId,
                        botUser.getOrder().toSend(),
                        List.of(ActButton.SEND.getName(), ActButton.CANCEL_REGISTRATION.getName())));
                botUser.setCurrantState(State.SUBMIT);
            }
        } else {
            if (!message.getFrom().getIsBot()) addId(message);
            addId(sendMessage(chatId, "Выберите вид упаковки нажатием соответствующей кнопки"));
        }
    }

    @Override
    public boolean cancel() {
        cleanState(State.INPUT_TELEPHONE);
        botUser.setCurrantState(State.INPUT_TELEPHONE);
        return true;
    }
}
