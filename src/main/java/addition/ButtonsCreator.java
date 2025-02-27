package addition;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ButtonsCreator {

    public static InlineKeyboardMarkup createButtonRows(List<List<String>> buttonsName) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        for (List<String> stringList : buttonsName) {
            List<InlineKeyboardButton> rowInline = stringList
                    .stream()
                    .map(s -> {
                        InlineKeyboardButton button = new InlineKeyboardButton();
                        button.setText(s);
                        button.setCallbackData(s);
                        return button;
                    })
                    .toList();
            rowsInLine.add(rowInline);
        }
        inlineKeyboardMarkup.setKeyboard(rowsInLine);
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup createButton(List<String> buttonsName) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        buttonsName.forEach(s -> {
            var button = new InlineKeyboardButton();
            button.setText(s);
            button.setCallbackData(s);
            rowInline.add(button);
        });
        rowsInLine.add(rowInline);
        inlineKeyboardMarkup.setKeyboard(rowsInLine);

        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup createButtons(List<String> buttonsName) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        for (String name : buttonsName) {
            var button = new InlineKeyboardButton();
            button.setText(name);
            button.setCallbackData(name);
            rowInline.add(button);
        }
        rowsInLine.add(rowInline);
        inlineKeyboardMarkup.setKeyboard(rowsInLine);

        return inlineKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup createKeyBoard(List<String> buttonsName) {
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        for (var name : buttonsName) {
            KeyboardRow keyboardRow = new KeyboardRow();
            KeyboardButton keyboardButton = new KeyboardButton(name);
            keyboardRow.add(keyboardButton);
            keyboardRows.add(keyboardRow);
        }
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setKeyboard(keyboardRows);
        return markup;
    }

    public static InlineKeyboardMarkup createButton(Map<String, String> buttonsName) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        for (String s : buttonsName.keySet()) {
            var button = new InlineKeyboardButton();
            button.setText(buttonsName.get(s));//значение
            button.setCallbackData(s);//ключ
            rowInline.add(button);
        }
        rowsInLine.add(rowInline);
        inlineKeyboardMarkup.setKeyboard(rowsInLine);

        return inlineKeyboardMarkup;
    }

}
