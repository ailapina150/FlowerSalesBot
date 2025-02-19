package addition;

import java.util.List;

public enum ActButton {
    START("Перезапустить", "Перезапустить", "/start"),
    QUESTION("Задать вопрос", "Выбирите вопрос","/question"),
    FOR_FREE("Хочу бесплатный букет",
            "Перешлите этот бот минимум 50 абонентам telegram, отправьте сюда подтверждение рассылки " +
            "(скриншоты или видео) и получите букет в подарок.\nНаш агент свяжется с вами в течении 24 часов.\nАкция действует по " +
            AppProperties.get().getLastDay() + " включительно и получить подарок можно только в " + AppProperties.REGION,
            "/for_free"),

    PRICE("Цена тюльпана", "Цена тюльпана изменена и составляет ","/price"),
    PRICE_PEONY( "Цена пионовидного тюльпана", "Цена тюльпана пионовидного изменена и составляет","/pricePeony"),

    ORDER( "Создать заказ", "Выберите цветок", "/order"),
    NEW_ORDER( "Создать новый заказ", "Выберите цветок. Для отмены выбора воспользуйтесь меню.", "/order"),
    ADD_FLOWER( "Добавить цветы в букет", "Прокрутите вверх и выберите еще один цветок. Для отмены выбора воспользуйтесь меню.", "/addFlower"),
    COUNT_OF_SET( "Задать количество букетов", "Сколько надо букетов?", "/countOfSet"),
    NEW_SET( "Создать новый букет", "Прокрутите вверх и выберите цветок. Для отмены выбора воспользуйтесь меню.", "/newSet"),
    SUBMIT("Подтвердить заказ", "Перейти к оформлению заказа?", "/submit"),
    SEND("Подтвердить и отправить", "Заказ принят. Спасибо, что сотрудничаете с нами.", "/send"),
    CANCEL_REGISTRATION( "Отменить оформление заказа", "Выберите действие:", "/cancelRegistration"),
    CANCEL("Отменить последнее действие", "Отменить?", "/cancel");

    private final String name;
    private final String replay;
    private final String command;

    ActButton(String name, String replay, String command) {
        this.name = name;
        this.replay = replay;
        this.command = command;
    }

    public String getReplay() {
        return replay;
    }

    public String getName() {
        return name;
    }

    public String getCommand() {
        return command;
    }

    public static ActButton getButtonOnName(String cmd){
        for(ActButton button : ActButton.values()){
            if(button.name.equals(cmd)){
                return button;
            }
        }
        return  null;
    }

    public static List<String> getStartButton() {
        return List.of(ActButton.QUESTION.getName(),
                ActButton.ORDER.getName(),
                ActButton.FOR_FREE.getName());
    }

    public static List<String> getFirstButton() {
        return List.of(ActButton.QUESTION.getName(),
                ActButton.NEW_ORDER.getName(),
                ActButton.FOR_FREE.getName());
    }


    public static List<String> getNextSetButtons(){
        return List.of(ActButton.NEW_SET.getName(),
                ActButton.SUBMIT.getName(),
                ActButton.CANCEL.getName());
    }

    public static List<String> getContinualButton() {
        return List.of(ActButton.ADD_FLOWER.getName(),
                ActButton.COUNT_OF_SET.getName(),
                ActButton.NEW_SET.getName(),
                ActButton.SUBMIT.getName(),
                ActButton.CANCEL.getName());
    }
}
