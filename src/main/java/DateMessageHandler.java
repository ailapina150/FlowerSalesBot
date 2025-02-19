import addition.*;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.LocalDate;

public class DateMessageHandler extends MessageHandler{
    public DateMessageHandler(BotUser botUser) {
        super(botUser);
    }

    @Override
    public void handle(Message message, String buttonData) {
        if (message.getFrom().getIsBot()) return;
        String text = message.getText().trim()
                .replaceAll("[/]", ".")
                .replaceAll("[-]", ".");
        if (!(text.endsWith("." + AppProperties.get().lastDay.getYear())))
            text = text + "." + AppProperties.get().lastDay.getYear();
        if (text.matches("^[0-9]{2}[.][0-9]{2}[.][0-9]{4}$")) {
            try {
                int day = Integer.parseInt(text.substring(0, text.indexOf(".")));
                int month = Integer.parseInt(text.substring(text.indexOf(".") + 1, text.lastIndexOf(".")));
                LocalDate date = LocalDate.of(AppProperties.get().lastDay.getYear(), month, day);
                LocalDate dateBefore = AppProperties.get().firstDay;
                if (dateBefore.isBefore(LocalDate.now().plusDays(1))) {
                    dateBefore = LocalDate.now().plusDays(1);
                }
                if (date.isBefore(dateBefore) || date.isAfter(AppProperties.get().lastDay)) {
                    addId(message);
                    addId(sendMessage(chatId, "Введите, пожалуйста, дату в c "
                            + AppProperties.get().getFirstDay() + " по " + AppProperties.get().getLastDay()));
                } else {
                    clean();
                    addStateId(message);
                    botUser.getOrder().setDate(text);
                    addStateId(sendMessageWithRowButton(chatId, "Выберите время", TimeButton.getNames(),
                            3, true));
                    botUser.setCurrantState(State.SELECT_TIME);
                }
            } catch (Exception e) {
                addId(message);
                addId(sendMessage(chatId, "Введение вами значение не является датой." +
                        " Введите, пожалуйста, дату в формате dd.MM"));
            }
        } else {
            addId(message);
            addId(sendMessage(chatId, "Введите, пожалуйста, дату в формате dd.MM"));
        }
    }

    @Override
    public boolean cancel() {
        cleanState(State.REGISTRATION);
        botUser.setCurrantState(State.REGISTRATION);
        return true;
    }


}
