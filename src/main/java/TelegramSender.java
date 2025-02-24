
import addition.AppProperties;
import addition.ButtonsCreator;
import addition.FileLoader;
import org.json.JSONObject;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.pinnedmessages.PinChatMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;

import java.io.File;
import java.net.URL;
import java.util.*;

public abstract class TelegramSender extends TelegramLongPollingBot {

    @Override
    public String getBotUsername() {
        return AppProperties.BOT_USER_NAME;
    }

    @Override
    public String getBotToken() {
        return AppProperties.BOT_TOKEN;
    }

    public long getChatId(Update update) {
        return update.hasCallbackQuery()
                ? update.getCallbackQuery().getMessage().getChatId()
                : update.getMessage().getChatId();
    }

    public Message getMessage(Update update) {
        return update.hasCallbackQuery()
                ? update.getCallbackQuery().getMessage()
                : update.getMessage();
    }

    public String getButtonData(Update update) {
        return update.hasCallbackQuery()
                ? update.getCallbackQuery().getData()
                : update.getMessage().hasText()
                ? update.getMessage().getText().trim()
                : "";
    }

    public void editMessageText(Long chartId, int messageId, String text, List<String> buttons) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(chartId.toString());
        editMessageText.setMessageId(messageId);
        editMessageText.setText(text);
        editMessageText.setReplyMarkup(ButtonsCreator.createButton(buttons));
        try {
            execute(editMessageText);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public String getCharDescription(Long chartId) {
        try {
            URL url = new URL("https://api.telegram.org/bot" + AppProperties.BOT_TOKEN + "/getChat?chat_id=" + chartId);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
            String fileResponse = bufferedReader.readLine();
            JSONObject response = new JSONObject(fileResponse);
            JSONObject result = response.getJSONObject("result");
            return result.getString("description");
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public String setCharDescription(Long chartId) {
        try {
            URL url = new URL("https://api.telegram.org/bot" + AppProperties.BOT_TOKEN + "/setChat?chat_id=" + chartId);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
            String fileResponse = bufferedReader.readLine();
            JSONObject response = new JSONObject(fileResponse);
            JSONObject result = response.getJSONObject("result");
            return result.getString("description");
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void deleteMessage(Long chartId, int messageId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(chartId.toString());
        deleteMessage.setMessageId(messageId);
        try {
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public Message sendMessage(Long chartId, String text) {
        return sendMessage(chartId, text, null);
    }

    public void sendMessage(String chartId, String text) {
        SendMessage reply = new SendMessage();
        reply.setChatId(chartId);
        reply.setText(text);
        try {
            execute(reply);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public Message sendMessage(Long chartId, String text, List<String> buttons) {
        SendMessage reply = new SendMessage();
        reply.setChatId(chartId.toString());
        reply.setText(text);
        if (buttons != null) {
            reply.setReplyMarkup(ButtonsCreator.createButton(buttons));
        }
        try {
            return execute(reply);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Message sendMessageWithRowButton(Long chartId, String text, List<String> buttons) {
        return sendMessageWithRowButton(chartId, text, buttons, 1, true);
    }

    public void pinMessage(Long chartId, Integer messageId) {
        PinChatMessage reply = new PinChatMessage();
        reply.setChatId(chartId.toString());
        reply.setMessageId(messageId);
        try {
            execute(reply);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    public Message sendMessageWithRowButton(Long chartId, String text, List<String> buttons, int numberInRow, boolean remFirst) {
        SendMessage reply = new SendMessage();
        reply.setChatId(chartId.toString());
        reply.setText(text);
        if (buttons != null) {
            var markup = ButtonsCreator.createButtonRows(FileLoader.makeListOfList(buttons, numberInRow, remFirst));
            reply.setReplyMarkup(markup);
        }
        try {
            return execute(reply);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Message sendMessageWithKeyboardButton(Long chartId, String text, List<List<String>> buttons) {
        SendMessage reply = new SendMessage();
        reply.setChatId(chartId.toString());
        reply.setText(text);
        if (buttons != null) {
            var markup = ButtonsCreator.createButtonRows(buttons);
            reply.setReplyMarkup(markup);
        }
        try {
            return execute(reply);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            return null;
        }
    }


    public void sendMessageWithKeyBoard(Long chartId, String text, List<String> buttons) {
        System.out.println(chartId);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setReplyMarkup(ButtonsCreator.createKeyBoard(buttons));
        sendMessage.setChatId(AppProperties.ORDERS_CHAT_ID.toString());
        sendMessage.setText(text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendPhoto(String chartId, String fileName, String caption) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chartId);
        try {
            sendPhoto.setPhoto(new InputFile(new FileInputStream(fileName), FileLoader.getSingleName(fileName)));
            sendPhoto.setCaption(caption);
            execute(sendPhoto);
        } catch (FileNotFoundException | TelegramApiException e) {
            e.printStackTrace();
        }

    }

    public void sendVideo(String chartId, String fileName, String caption) {
        try {

            File file = new File(fileName);
            InputFile inputFile = new InputFile();
            inputFile.setMedia(file);
            SendVideo sendVideo = new SendVideo();
            sendVideo.setVideo(inputFile);
            sendVideo.setCaption(caption);
            sendVideo.setChatId(chartId);
            execute(sendVideo).getMessageId();
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public String getPhotoId(Message message) {
        return Objects.requireNonNull(message.getPhoto().stream().max(Comparator.comparing(PhotoSize::getFileSize))
                .orElse(null)).getFileId();
    }

    public Message sendPhoto(Long chartId, String fileName, String caption) {
        try {
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(chartId.toString());
            sendPhoto.setPhoto(new InputFile(new FileInputStream(fileName), FileLoader.getSingleName(fileName)));
            sendPhoto.setCaption(caption);
            return execute(sendPhoto);

        } catch (FileNotFoundException | TelegramApiException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Integer sendPhoto(Long chartId, String fileName, String caption, List<String> buttons) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chartId.toString());
        try {
            sendPhoto.setPhoto(new InputFile(new FileInputStream(fileName), caption));
            if (!caption.isBlank()) sendPhoto.setCaption(caption);
            if (buttons != null) {
                sendPhoto.setReplyMarkup(ButtonsCreator.createButton(buttons));
            }
            return execute(sendPhoto).getMessageId();
        } catch (IOException | TelegramApiException e) {
            e.printStackTrace();
            return  null;
        }
    }

    public Message sendResourcesPhoto(Long chartId, String fileName, String caption) {
        try {
            System.out.println(fileName);
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(chartId.toString());
            //   var name = TelegramSender.class.getResource("/" + fileName).getPath();
            sendPhoto.setPhoto(new InputFile(new FileInputStream("./" + fileName), FileLoader.getSingleName(fileName)));
            sendPhoto.setCaption(caption);
            return execute(sendPhoto);

        } catch (TelegramApiException | FileNotFoundException | NullPointerException e) {
            TelegramSender.class.getResource("./" + fileName);
            e.printStackTrace();
            return null;
        }
    }

    public List<Integer> sendMedia(Long chartId, List<String> names) {
        SendMediaGroup sendMediaGroup = new SendMediaGroup();
        sendMediaGroup.setChatId(chartId.toString());
        List<InputMedia> inputMediaList = new ArrayList<>();

        for (String fileName : names) {
            InputMediaPhoto inputMedia = new InputMediaPhoto();
            File file = new File(fileName);
            inputMedia.setMedia(file, fileName);
            inputMedia.setCaption(FileLoader.getSingleName(fileName));
            inputMediaList.add(inputMedia);
        }
        sendMediaGroup.setMedias(inputMediaList);
        List<Integer> listMessageId = new ArrayList<>();
        try {
            var list = execute(sendMediaGroup);
            listMessageId = list.stream().map(Message::getMessageId).toList();
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return listMessageId;
    }

    public List<Integer> sendResourcesMedia(Long chartId, List<String> names) {
        SendMediaGroup sendMediaGroup = new SendMediaGroup();
        sendMediaGroup.setChatId(chartId.toString());
        List<InputMedia> inputMediaList = new ArrayList<>();

        for (String fileName : names) {
            InputMediaPhoto inputMedia = new InputMediaPhoto();
            var name = Objects.requireNonNull(TelegramSender.class.getResource("/" + fileName)).getPath();
            File file = new File(fileName);
            inputMedia.setMedia(file, fileName);
            inputMedia.setCaption(FileLoader.getSingleName(fileName));
            inputMediaList.add(inputMedia);
        }
        sendMediaGroup.setMedias(inputMediaList);
        List<Integer> listMessageId = new ArrayList<>();
        try {
            var list = execute(sendMediaGroup);
            listMessageId = list.stream().map(Message::getMessageId).toList();
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return listMessageId;
    }

}
