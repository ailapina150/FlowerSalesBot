import addition.*;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

public class SaleTelegramBot extends TelegramSender {
    private final Map<Long, BotUser> botUsers;
    private ManagingSettingsButtons currant = null;

    public SaleTelegramBot() {
        botUsers = new HashMap<>();

        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand(ActButton.START.getCommand(), ActButton.START.getName()));
        listOfCommands.add(new BotCommand(ActButton.NEW_ORDER.getCommand(), ActButton.NEW_ORDER.getName()));
        listOfCommands.add(new BotCommand(ActButton.CANCEL.getCommand(), ActButton.CANCEL.getName()));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasCallbackQuery() && !update.hasMessage()) return;
        long chatId = getChatId(update);
        Message message = getMessage(update);
        String text = message.hasText() ? message.getText() : "";
        String buttonData = getButtonData(update);

        if (chatId == AppProperties.ORDERS_CHAT_ID) {
            if (text.equalsIgnoreCase(AppProperties.PASSWORD)) {
                sendMessageWithKeyBoard(chatId, "Настройки",
                        Arrays.stream(ManagingSettingsButtons.values()).map(ManagingSettingsButtons::getExplanation).toList());
            }
            ManageSettings manageSettings = new ManageSettings();
            if (currant == null) {
                currant = manageSettings.manageSettings(text);
            } else {
                currant = manageSettings.changeSetting(currant, buttonData);
            }
            return;
        }

        if (chatId < 0) return;
        if (botUsers.size() == 0 &&
                !(text.equals(ActButton.START.getCommand()) || text.equals(ActButton.ORDER.getCommand()))) {
            sendMessage(chatId, "Произошел сбой в работе. Приносим свои извинения");
            text = ActButton.START.getCommand();
        }
        FileLoader.readFirstDay();
        FileLoader.readLastDay();

        usersIdCollector(chatId, message);
        BotUser botUser = botUsers.get(chatId);
        if ((botUser.getCurrantState() == State.START
                || botUser.getCurrantState() == State.SELECT_QUESTION
                || botUser.getCurrantState() == State.INSTRUCTION) &&
                message.hasVideoNote() || message.hasVideo() || message.hasPhoto()) {
            photoVideoCollector(chatId, message, botUser);
        }
        MessageHandler messageHandler = HandlerResolver(botUser);
        if (text.equals(ActButton.START.getCommand())) {
            sendHiMessage(chatId, message, botUser);
            botUser.setCurrantState(State.START);
        } else if (text.equals(ActButton.NEW_ORDER.getCommand()) || buttonData.equals(ActButton.ORDER.getName())) {
            messageHandler.showFlower();
        } else if (text.equals(ActButton.CANCEL.getCommand()) || buttonData.equals(ActButton.CANCEL.getName())) {
            deleteMessage(chatId, message.getMessageId());
            messageHandler.cancel();
        } else {
            messageHandler.handle(message, buttonData);
        }
        if (botUser.getCurrantState() == State.START
                || botUser.getCurrantState() == State.SELECT_QUESTION
                || botUser.getCurrantState() == State.INSTRUCTION) photoVideoSender(botUser);
    }

    private MessageHandler HandlerResolver(BotUser botUser) {
        State currantState = botUser.getCurrantState();
        return switch (currantState) {
            case SELECT_QUESTION -> new QuestionMessageHandler(botUser);
            case INPUT_QUESTION -> new UserQuestionMessageHandler(botUser);
            case MADE_ORDER -> new MadeOrderMessageHandler(botUser);
            case SELECT_FLOWER -> new SelectFlowerMessageHandler(botUser);
            case INPUT_NUMBER_FLOWERS -> new InputNumberFlowersMessageHandler(botUser);
            case INPUT_NUMBER_SET -> new InputNumberSetMessageHandler(botUser);
            case SELECT_ACT -> new SelectActMessageHandler(botUser);
            case REGISTRATION -> new RegistrationMessageHandler(botUser);
            case INPUT_DATE -> new DateMessageHandler(botUser);
            case SELECT_TIME -> new TimeMessageHandler(botUser);
            case INPUT_ADDRESS -> new AddressMessageHandler(botUser);
            case INPUT_TELEPHONE -> new TelephoneMessageHandler(botUser);
            case INPUT_WRAPPER -> new WrapperMessageHandler(botUser);
            case SUBMIT -> new SubmitMessageHandler(botUser);
            case INSTRUCTION -> new InstructionMessageHandler(botUser);
            default -> new StartMessageHandler(botUser);
        };
    }

    private void usersIdCollector(Long chatId, Message message) {
        BotUser botUser = botUsers.get(chatId);
        User user = message.getFrom();
        if (botUser == null) {
            if (user.getId() != AppProperties.BOT_USER_ID) {
                botUser = new BotUser(user.getId(), user.getFirstName());
                botUser.setUserName(user.getUserName());
                System.out.println(botUser);
            } else {
                botUser = new BotUser(chatId);
            }
            botUsers.put(chatId, botUser);
            System.out.println(botUser);
            sendMessage(AppProperties.COLLECTOR_CHAT_ID, "@" + botUser.getUserName() +
                    " Id: " + chatId + " " + botUser.getFirstName() + " " + getBotUsername());
        } else if (chatId != AppProperties.BOT_USER_ID && user.getFirstName().equals("")) {
            botUser.setUserName(user.getUserName());
            botUser.setFirstName(user.getFirstName());
            botUser.setUserName(user.getUserName());
            sendMessage(AppProperties.COLLECTOR_CHAT_ID, "@" + botUser.getUserName() +
                    " Id: " + chatId + " " + botUser.getFirstName() + " " + getBotUsername());
        }
    }

    void photoVideoCollector(Long chatId, Message message, BotUser botUser) {
        try {
            if (message.hasPhoto()) {
                String fileId = getPhotoId(message);
                sendMessage(chatId, FileLoader.upLoadFile(chatId, fileId, AppProperties.PHOTO));
            }
            if (message.hasVideo()) {
                String fileId = message.getVideo().getFileId();
                sendMessage(chatId, FileLoader.upLoadFile(chatId, fileId, AppProperties.VIDEO));
            }
            if (message.hasVideoNote()) {
                String fileId = message.getVideoNote().getFileId();
                sendMessage(chatId, FileLoader.upLoadFile(chatId, fileId, AppProperties.VIDEO));
            }
            if (botUser.getTelephone().equals("")) {
                sendMessage(chatId, "Напишите номер телефона для обратной связи в формате YY XXX XX XX:");
                botUser.setCurrantState(State.INPUT_TELEPHONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void photoVideoSender(BotUser botUser) {
        System.out.println("photoVideoSender");
        try {
            if (FileLoader.getFilesNames(AppProperties.PHOTO) != null && !botUser.getTelephone().equals("")) {
                for (String fileName : Objects.requireNonNull(FileLoader.getFilesNames(AppProperties.PHOTO))) {
                    String id = fileName.substring(fileName.lastIndexOf("\\") + 1, fileName.indexOf("_"));
                    sendPhoto(AppProperties.PRESENT_CHAR_ID, fileName,
                            "@" + botUser.getUserName() + " тел: " + botUser.getTelephone() +
                                    " Id: " + id + "(" + botUser.getFirstName() + ")");
                    System.out.println("Файл" + fileName + "отправлен");
                }
                FileLoader.clearFolder(AppProperties.PHOTO);
            }

            if (FileLoader.getFilesNames(AppProperties.VIDEO) != null && !botUser.getTelephone().equals("")) {
                Objects.requireNonNull(FileLoader.getFilesNames(AppProperties.VIDEO)).forEach(fileName -> {
                    String id = fileName.substring(fileName.lastIndexOf("\\") + 1, fileName.indexOf("_"));
                    sendVideo(AppProperties.PRESENT_CHAR_ID.toString(), fileName,
                            "@" + botUser.getUserName() + " тел: " + botUser.getTelephone() +
                                    " Id: " + id + "(" + botUser.getFirstName() + ")");
                    System.out.println("Файл" + fileName + "отправлен");
                });
                FileLoader.clearFolder(AppProperties.VIDEO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("ошибка отправки файла");
        }
    }

    private void sendHiMessage(long chatId, Message message, BotUser botUser) {
        String firstMessage =
                ", здравствуйте 👋. Мы занимаемся выращиванием и продажей тюльпанов 💐 уже много лет🧘‍♀️. " +
                        "И поэтому предлагаем Вам цветок 🌼 отличного качества, различных видов, и сортов." +
                        " А  для удобства и, конечно же, экономии вашего времени🤾‍♂️," +
                        "а так же денег💰,  мы создали для вас этот бот🤖. " +
                        "Да, возможно есть недоработки и недостатки🛠, но просим:  не судите строго, " +
                        "а лучше напишите🧑‍🎓✏️👨‍🎓нам об этом. Мы ведь стараемся для Вас и Вашего удобства. " +
                        "Весеннего настроения и  удачного выбора цветочков.";
        sendMessageWithRowButton(chatId,
                message.getChat().getFirstName() + firstMessage,
                ActButton.getStartButton(),
                2, false);

        botUser.cleanOrder();
        botUser.cleanFlowersSet();
        botUser.setCurrantState(State.START);
    }

}

