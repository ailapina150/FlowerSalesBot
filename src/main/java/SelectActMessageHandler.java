import addition.*;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

public class SelectActMessageHandler extends MessageHandler {
    protected SelectActMessageHandler(BotUser botUser) {
        super(botUser);
    }

    @Override
    public void handle(Message message, String buttonData) {
        ActButton button = ActButton.getButtonOnName(buttonData);
        if (button != null) {
            switch (button) {
                case ADD_FLOWER -> {
                    clean();
                    addId(sendMessage(chatId, ActButton.ADD_FLOWER.getReplay()));
                    botUser.setCurrantState(State.SELECT_FLOWER);
                }
                case NEW_SET -> {
                    clean();
                    if (botUser.getFlowersSet().getFlowers().size() > 0) {
                        botUser.getOrder().addFlowerSet(new FlowersSet(botUser.getFlowersSet().getFlowers(),
                                botUser.getFlowersSet().getCountSet(), botUser.getFlowersSet().isInputSetNumber()));
                        botUser.cleanFlowersSet();
                    }
                    addStateId(sendMessage(chatId, "БУКЕТ №" + (botUser.getOrder().getFlowerSetList().size() + 1) + ":"));
                    addId(sendMessage(chatId, ActButton.NEW_SET.getReplay()));
                    botUser.setCurrantState(State.SELECT_FLOWER);
                }
                case COUNT_OF_SET -> {
                    clean();
                    if (botUser.getFlowersSet().getFlowers().size() == 0) {
                        botUser.setFlowersSet(botUser.getOrder().getLastSet());
                        botUser.getOrder().delLastSet();
                    }
                    if (botUser.getFlowersSet().isInputSetNumber()) {
                        cleanState(State.INPUT_NUMBER_SET);
                        cleanState(State.SELECT_ACT);
                    }
                    addStateId(sendMessage(chatId, ActButton.COUNT_OF_SET.getReplay()));
                    botUser.setCurrantState(State.INPUT_NUMBER_SET);
                }
                case SUBMIT -> {
                    clean();
                    int countOfFlowers = (botUser.getOrder().getCountFlowers()
                            + botUser.getFlowersSet().getCountFlowers());
                    if (countOfFlowers < AppProperties.get().goods.getMinFlowerCount()) {
                        addId(sendMessage(chatId, "Количество заказанных цветов - "
                                + countOfFlowers + "шт.\nМинимальный заказ  - " + AppProperties.get().goods.getMinFlowerCount() + " шт"));
                        addId(sendMessageWithRowButton(chatId, ActButton.CANCEL_REGISTRATION.getReplay(),
                                ActButton.getContinualButton()));
                    } else {
                        if (botUser.getFlowersSet().getFlowers().size() > 0) {
                            botUser.getOrder().addFlowerSet(
                                    new FlowersSet(botUser.getFlowersSet().getFlowers(), 1, false));
                            botUser.cleanFlowersSet();
                        }
                        addStateId(sendMessage(chatId, botUser.getOrder().toString()));
                        addStateId(sendMessage(chatId, ActButton.SUBMIT.getReplay(), Button.getNames()));
                        botUser.setCurrantState(State.REGISTRATION);
                    }
                }
            }
        } else {
            addId(sendMessage(chatId, "Выберите действие из списка"));
        }
        if (!message.getFrom().getIsBot()) {
            addId(message);
        }
    }

    @Override
    public boolean cancel() {
        boolean result = true;
        System.out.println(botUser.getCurrantState() + "cancel");
        switch (botUser.getLastState()) {
            case INPUT_NUMBER_FLOWERS -> {
                cleanState(State.INPUT_NUMBER_FLOWERS);
                if (botUser.getFlowersSet().getFlowers().size() > 0) {
                    botUser.removeLastFlower();
                }
                botUser.setCurrantState(State.INPUT_NUMBER_FLOWERS);
            }
            case SELECT_FLOWER -> {

            }
            case INPUT_NUMBER_SET -> {
                cleanState(State.INPUT_NUMBER_SET);
                cleanState(State.SELECT_ACT);
                botUser.setFlowersSet(botUser.getOrder().getLastSet());
                botUser.getOrder().delLastSet();
                addStateId(sendMessage(chatId, ActButton.COUNT_OF_SET.getReplay()));
                botUser.setCurrantState(State.INPUT_NUMBER_SET);
            }
            case REGISTRATION -> {
                if (botUser.getFlowersSet() != null && botUser.getFlowersSet().getCountSet() > 1) {
                    botUser.setFlowersSet(botUser.getOrder().getLastSet());
                    botUser.getOrder().delLastSet();
                    cleanState(State.INPUT_NUMBER_SET);
                    botUser.setCurrantState(State.INPUT_NUMBER_SET);
                } else {
                    cleanState(State.INPUT_NUMBER_FLOWERS);
                    if (botUser.getFlowersSet().getFlowers().size() > 0) {
                        botUser.removeLastFlower();
                    }
                    botUser.setCurrantState(State.INPUT_NUMBER_FLOWERS);
                }
            }
            case SUBMIT -> {
                botUser.getOrder().addFlowerSet(new FlowersSet(
                        botUser.getFlowersSet().getFlowers(),
                        botUser.getFlowersSet().getCountSet(),
                        botUser.getFlowersSet().isInputSetNumber()));
                cleanState(State.SUBMIT);
                addStateId(sendMessage(chatId, botUser.getOrder().toString()));
                addStateId(sendMessage(chatId, ActButton.SUBMIT.getReplay(), Button.getNames()));
                addStateId(State.REGISTRATION, sendMessage(chatId, "Когда? Укажите дату доставки (с 13.02.2025 по 16.03.2025)"));
                addStateId(State.INPUT_DATE, sendMessage(chatId, botUser.getOrder().getDate()));
                addStateId(State.INPUT_DATE, sendMessageWithRowButton(chatId, "Выберите время", TimeButton.getNames(), 3, true));
                addStateId(State.SELECT_TIME, sendMessage(chatId, botUser.getOrder().getTime()));
                addStateId(State.SELECT_TIME, sendMessage(chatId, "ВАЖНО! Доставка только по БРЕСТУ. Напишите адрес"));
                addStateId(State.INPUT_ADDRESS, sendMessage(chatId, botUser.getOrder().getAddress()));
                addStateId(State.INPUT_ADDRESS, sendMessage(chatId, "Напишите телефон для обратной связи в формате YY XXX XX XX"));
                addStateId(State.INPUT_TELEPHONE, sendMessage(chatId, botUser.getOrder().getTelephone()));
                addStateId(State.INPUT_TELEPHONE, sendMessage(chatId, "Выберите материал для упаковки", WrapperButton.getNames()));
                addStateId(State.INPUT_WRAPPER, sendMessage(chatId, botUser.getOrder().getWrapper()));
                addStateId(State.INPUT_WRAPPER, sendMessageWithRowButton(chatId,
                        botUser.getOrder().toSend(),
                        List.of(ActButton.SEND.getName(), ActButton.CANCEL_REGISTRATION.getName())));
                botUser.setCurrantState(State.SUBMIT);
            }
            default -> result = false;
        }
        return result;
    }

}
