import addition.*;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDateTime;
import java.util.List;

public abstract class MessageHandler extends TelegramSender {
    protected final BotUser botUser;
    protected final long chatId;

    public MessageHandler(BotUser botUser) {
        this.botUser = botUser;
        chatId = botUser.getUserId();
    }

    public abstract void handle(Message message, String buttonData);

    public boolean cancel() {
        return false;
    }

    @Override
    public void onUpdateReceived(Update update) {
    }

    public void showFlower() {
        botUser.cleanOrder();
        botUser.cleanFlowersSet();
        FileLoader.readGoods();
        FlowerLoader.remake();
        botUser.setPriceTulip(AppProperties.get().goods.getPriceTulip());
        botUser.setPricePeony(AppProperties.get().goods.getPricePeony());
        Integer messageId;
        for (List<String> names : FlowerLoader.get()) {
            String flowerPrice = names.get(0).contains(AppProperties.PEONY)
                    ? "Цена " + botUser.getPricePeony() + AppProperties.MONEY + ". "
                    : "Цена " + botUser.getPriceTulip() + AppProperties.MONEY + ". ";
            if (names.size() > 1) {
                sendMedia(chatId, names);
                messageId =sendMessageWithRowButton(chatId, flowerPrice + ActButton.ORDER.getReplay(),
                        FileLoader.getAllSingleNames(names),
                        AppProperties.NUMBER_BUTTON_IN_ROW,
                        true).getMessageId();
            } else {
                messageId = sendPhoto(chatId, names.get(0),
                        flowerPrice + ActButton.ORDER.getReplay(),
                        FileLoader.getAllSingleNames(names));
            }
            if (names.equals(FlowerLoader.get().stream().findFirst().get())){
                pinMessage(chatId,messageId);
                System.out.println("Сообщение закреплено");
            }
        }
        sendMessage(chatId, QuestionButton.MIN.getReplay(AppProperties.get().goods,
                AppProperties.get().getFirstDay(), AppProperties.get().getLastDay()));
        addId(sendMessage(chatId, "Выберите цветок нажатием соответствующей кнопки. " +
                "Для отмены выбора воспользуйтесь меню"));
        botUser.setCurrantState(State.SELECT_FLOWER);
    }

    protected void addId(Message message) {
        if (message != null) {
            System.out.println("null: " + message.getText());
            botUser.getIds().add(new SendId(message, null, LocalDateTime.now()));
        }
    }

    protected void addStateId(Message message) {
        if (message != null) {
            System.out.println(botUser.getCurrantState() + ": " + message.getText());
            botUser.getIds().add(new SendId(message, botUser.getCurrantState(), LocalDateTime.now()));
        }
    }

    protected void addStateId(State state, Message message) {
        if (message != null) {
            System.out.println(state + ": " + message.getText());
            botUser.getIds().add(new SendId(message, state, LocalDateTime.now()));
        }
    }

    protected void clean() {
        if (botUser.getIds() != null && botUser.getIds().size() > 0) {
            var list = botUser.getIds().stream().filter(id -> id.getState() == null).toList();
            if (list != null && list.size() > 0) {
                list.forEach(id -> deleteMessage(chatId, id.getMessageId()));
                list.forEach(id -> botUser.getIds().remove(id));
            }
        }
    }

    protected void cleanState(State state) {
        clean();
        boolean find = false;
        int n = botUser.getIds().size() - 1;
        for (int i = n; i >= 0; i--) {
            if (botUser.getIds().get(i).getState().equals(state)) {
                find = true;
                deleteMessage(chatId, botUser.getIds().get(i).getMessageId());
                botUser.getIds().remove(i);
            } else if (find) {
                break;
            }

        }
    }

    protected void cleanState() {
        clean();
        cleanState(State.INPUT_DATE);
        cleanState(State.SELECT_TIME);
        cleanState(State.REGISTRATION);
        cleanState(State.INPUT_ADDRESS);
        cleanState(State.INPUT_WRAPPER);
        cleanState(State.INPUT_TELEPHONE);
        cleanState(State.SELECT_ACT);
    }

}
