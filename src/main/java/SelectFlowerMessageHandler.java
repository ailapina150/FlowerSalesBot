import addition.*;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

public class SelectFlowerMessageHandler extends MessageHandler {
    protected SelectFlowerMessageHandler(BotUser botUser) {
        super(botUser);
    }

    @Override
    public void handle(Message message, String buttonData) {
        if (!message.getFrom().getIsBot()) {
            addId(message);
            addId(sendMessage(chatId, ActButton.ORDER.getReplay()));
        } else {
            for (List<String> fileNames : FlowerLoader.get()) {
                for (String fileName : fileNames) {
                    String name = FileLoader.getSingleName(fileName);
                    if (buttonData.equals(name)) {
                        if (botUser.getOrder().getFlowerSetList().size() == 0
                                && (botUser.getFlowersSet().getFlowers().size() == 0)) {
                            addStateId(sendMessage(chatId,
                                    "БУКЕТ №" + (botUser.getOrder().getFlowerSetList().size() + 1) + ":"));
                        }
                        clean();
                        double flowerPrice = fileName.contains(AppProperties.PEONY)
                                ? botUser.getPricePeony()
                                : botUser.getPriceTulip();
                        addStateId(sendPhoto(chatId, fileName, name + " - " + flowerPrice + AppProperties.MONEY));
                        botUser.setCurrantFileName(fileName);
                        addStateId(sendMessage(chatId, "Сколько нужно цветов?"));
                        botUser.setCurrantState(State.INPUT_NUMBER_FLOWERS);
                    }
                }
            }
        }
    }

    @Override
    public boolean cancel() {
        System.out.println(botUser.getCurrantState() + "cancel" + botUser.getLastState());
        switch (botUser.getLastState()) {
            case INPUT_NUMBER_FLOWERS, SELECT_ACT -> {
                cleanState(State.SELECT_ACT);
                if (botUser.getFlowersSet().getFlowers().size() == 0 && botUser.getOrder().getCountFlowers() > 0) {
                    botUser.setFlowersSet(botUser.getOrder().getLastSet());
                    botUser.getOrder().delLastSet();
                }
                if (botUser.getFlowersSet().isInputSetNumber()) {
                    cleanState(State.INPUT_NUMBER_SET);
                    botUser.setCurrantState(State.INPUT_NUMBER_SET);
                } else {
                    botUser.getFlowersSet().getFlowers().remove(botUser.getFlowersSet().getFlowers().size() - 1);
                    cleanState(State.INPUT_NUMBER_FLOWERS);
                    botUser.setCurrantState(State.INPUT_NUMBER_FLOWERS);
                }
            }
            case INPUT_NUMBER_SET -> cleanState(State.INPUT_NUMBER_SET);
            case START -> {
                sendMessageWithRowButton(chatId, ActButton.CANCEL_REGISTRATION.getReplay(), ActButton.getStartButton());
                botUser.setCurrantState(State.START);
            }
        }
        return true;
    }
}
