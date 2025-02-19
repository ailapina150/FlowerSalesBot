import addition.*;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;
import java.util.List;

public class ManageSettings extends TelegramSender{
    @Override
    public void onUpdateReceived(Update update) {}

    public ManagingSettingsButtons manageSettings(String command) {
        System.out.println("manageSettings");
        long chatId = AppProperties.ORDERS_CHAT_ID;
        ManagingSettingsButtons setting = ManagingSettingsButtons.getButtonOnExplanation(command);
        if (setting == null) return null;
        switch (setting) {
            case PRICE_PEONY -> {
                sendMessage(chatId, "Текущая цена пионовидного тюльпана " + AppProperties.get().goods.getPricePeony()
                        + "\nВведите новую цену:");
                return setting;
            }
            case PRICE_TULIP -> {
                sendMessage(chatId, "Текущая цена тюльпана " + AppProperties.get().goods.getPriceTulip()
                        + "\nВведите новую цену:");
                return setting;
            }
            case MIN_FLOWER_COUNT -> {
                sendMessage(chatId, "Текущее количество "
                        + AppProperties.get().goods.getMinFlowerCount() + "\nВведите новое количество цветов:");
                return setting;
            }
            case DISCOUNT -> {
                sendMessage(chatId, "Текущий размер скидки " + AppProperties.get().goods.getDiscount()
                        + "\nВведите новый размер скидки в процентах:");
                return setting;
            }
            case DISCOUNT_SIZE -> {
                sendMessage(chatId, "Текущее количество цветов для сскидки " +
                        AppProperties.get().goods.getDiscountSize() + "\nВведите новое количество цветов для скидки: ");
                return setting;
            }
            case REMOVE_FLOWER -> {
                List<List<String>> flowers = FlowerLoader.get()
                        .stream()
                        .map(flowerList-> flowerList.stream()
                                .map(FileLoader::getSingleName)
                                .toList())
                        .toList();
                sendMessageWithKeyboardButton(chatId, "Выберите название цветка: ", flowers);
                return setting;
            }
            case ADD_FLOWER -> {
                if (AppProperties.get().goods.getFinished().size() > 0) {
                    sendMessageWithRowButton(chatId, "Выберите название цветка: ", AppProperties.get().goods.getFinished());
                    return setting;
                } else {
                    sendMessage(chatId, "Нет удаленных цветов");
                    return null;
                }
            }
            case FIRST_DATE -> {
                sendMessage(chatId,"Дата начала доставки заказов " + AppProperties.get().getFirstDay() +
                        "\nНапишити новое значение даты:");
                return setting;
            }
            case LAST_DATE -> {
                sendMessage(chatId,"Дата окончания доставки заказов " + AppProperties.get().getLastDay() +
                        "\nНапишити новое значение даты:");
                return setting;
            }
        }
        return null;
    }

    public ManagingSettingsButtons changeSetting(ManagingSettingsButtons currant, String text) {
        System.out.println("changeSetting");
        long chatId = AppProperties.ORDERS_CHAT_ID;
        if(currant == null) return null;
        System.out.println(currant);
        switch (currant) {
            case PRICE_PEONY -> {
                try {
                    double x = Double.parseDouble(text);
                    AppProperties.get().goods.setPricePeony(x);
                    FileLoader.writeGoods();
                    sendMessage(chatId, "Цена пионовидного тюльпана составляет "
                            + AppProperties.get().goods.getPricePeony() + AppProperties.MONEY);
                } catch (Exception e) {
                    sendMessage(chatId, "Ошибка: " + e.getClass().getSimpleName() + " " + e.getMessage());
                }
                currant = null;
            }
            case PRICE_TULIP -> {
                try {
                    double x = Double.parseDouble(text);
                    AppProperties.get().goods.setPriceTulip(x);
                    FileLoader.writeGoods();
                    sendMessage(chatId, "Цена тюльпана составляет "
                            + AppProperties.get().goods.getPriceTulip() + AppProperties.MONEY);
                } catch (Exception e) {
                    sendMessage(chatId, "Ошибка: " + e.getClass().getSimpleName() + " " + e.getMessage());
                }
                currant = null;
            }
            case MIN_FLOWER_COUNT -> {
                try {
                    int x = Integer.parseInt(text);
                    AppProperties.get().goods.setMinFlowerCount(x);
                    FileLoader.writeGoods();
                    sendMessage(chatId, "Минимальное количество цветов для доставки "
                            + AppProperties.get().goods.getMinFlowerCount());
                } catch (Exception e) {
                    sendMessage(chatId, "Ошибка: " + e.getClass().getSimpleName() + " " + e.getMessage());
                }
                currant = null;
            }
            case DISCOUNT -> {
                try {
                    int x = Integer.parseInt(text);
                    AppProperties.get().goods.setDiscount(x);
                    FileLoader.writeGoods();
                    sendMessage(chatId, "Размер скидки составляет "
                            + AppProperties.get().goods.getDiscount() + "%");
                } catch (Exception e) {
                    sendMessage(chatId, "Ошибка: " + e.getClass().getSimpleName() + " " + e.getMessage());
                }
                currant = null;
            }
            case DISCOUNT_SIZE ->{
                try {
                    int x = Integer.parseInt(text);
                    AppProperties.get().goods.setDiscountSize(x);
                    FileLoader.writeGoods();
                    sendMessage(chatId, "Минимальное количество цветов для скидки "
                            + AppProperties.get().goods.getDiscountSize());
                } catch (Exception e) {
                    sendMessage(chatId, "Ошибка: " + e.getClass().getSimpleName() + " " + e.getMessage());
                }
                currant = null;
            }
            case REMOVE_FLOWER -> {
                boolean find = false;
                for (var names : FlowerLoader.get()) {
                    if (names.stream().anyMatch(fileName -> FileLoader.getSingleName(fileName).equals(text))) {
                        find = true;
                        break;
                    }
                }
                if (find) {
                    AppProperties.get().goods.getFinished().add(text);
                    sendMessage(chatId, "Цветок " + text + " удален");
                    FileLoader.writeGoods();
                } else {
                    sendMessage(chatId, "Цветок c именем " + text + " не найден");
                }
                currant = null;
            }
            case ADD_FLOWER -> {
                if (AppProperties.get().goods.getFinished().stream()
                        .anyMatch(fileName -> fileName.equals(text))) {
                    AppProperties.get().goods.getFinished().remove(text);
                    FileLoader.writeGoods();
                    sendMessage(chatId, "Цветок " + text + " добавлен");
                } else {
                    sendMessage(chatId, "Цветок c именем " + text + " не найден среди удаленных");
                }
                currant = null;
            }
            case FIRST_DATE -> {
                try {
                    int year = Integer.parseInt(text.substring(6));
                    int month = Integer.parseInt(text.substring(3, 5));
                    int date = Integer.parseInt(text.substring(0, 2));
                    LocalDate firstDate = LocalDate.of(year, month, date);
                    AppProperties.get().setFirstDay(firstDate);
                    sendMessage(chatId, "Дата начало доставки заказов " + AppProperties.get().getFirstDay());
                    FileLoader.writeFirstDay();
                }catch(Exception e){
                    sendMessage(chatId, "Не верный формат или введенное значение не является датой");
                }
                currant = null;
            }
            case LAST_DATE -> {
                try {
                    int year = Integer.parseInt(text.substring(6));
                    int month = Integer.parseInt(text.substring(3, 5));
                    int date = Integer.parseInt(text.substring(0, 2));
                    LocalDate lastDate = LocalDate.of(year, month, date);
                    AppProperties.get().setLastDay(lastDate);
                    sendMessage(chatId, "Дата окончания доставки заказов " + AppProperties.get().getLastDay());
                    FileLoader.writeLastDay();
                }catch(Exception e){
                    sendMessage(chatId, "Не верный формат или введенное значение не является датой");
                }
                currant = null;
            }
        }
        return currant;
    }

}
