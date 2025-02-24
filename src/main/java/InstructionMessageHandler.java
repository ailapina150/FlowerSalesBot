import addition.*;
import org.telegram.telegrambots.meta.api.objects.Message;

public class InstructionMessageHandler extends MessageHandler {
    public InstructionMessageHandler(BotUser botUser) {
        super(botUser);
    }

    @Override
    public void handle(Message message, String buttonData) {
        Button button = Button.getButtonOnNames(buttonData);
        if (button != null && button.equals(Button.YES)) {

            addStateId(sendResourcesPhoto(chatId, AppProperties.INSTRUCTION + "/" + "photo_1.jpg",
                    "1. Зайдите в профиль бота"));
            addStateId(sendResourcesPhoto(chatId, AppProperties.INSTRUCTION + "/" + "photo_2.jpg",
                    "2. Нажмите на три точки "));
            addStateId(sendResourcesPhoto(chatId, AppProperties.INSTRUCTION + "/" + "photo_3.jpg",
                    "3. Нажимаем на \"Поделиться\"."));
            addStateId(sendResourcesPhoto(chatId, AppProperties.INSTRUCTION + "/" + "photo_4.jpg",
                    "4. Найдите телеграмм"));
            addStateId(sendResourcesPhoto(chatId, AppProperties.INSTRUCTION + "/" + "photo_5.jpg", """
                    5. Выберите 50 или более контактов
                    6. Нажмите отправить."""));
            addStateId(sendResourcesPhoto(chatId, AppProperties.INSTRUCTION + "/" + "photo_6.jpg", """
                    7. Сделайте скриншоты или видео
                      в подтверждение рассылки
                    8. Отправите файлы в этот чат"""));
            botUser.setCurrantState(botUser.getLastState());
        } else if (button != null && button.equals(Button.NO)) {
            sendMessageWithRowButton(chatId, "Выберите действие", ActButton.getStartButton(), 2, false);
            botUser.setCurrantState(botUser.getLastState());
        } else {
            if (!message.getFrom().getIsBot()) addId(message);
            addId(sendMessage(chatId, "Ответьте, пожалуйста да или нет"));
        }
    }

}
