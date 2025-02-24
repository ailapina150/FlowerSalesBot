package addition;

public enum ManagingSettingsButtons {
    PRICE_PEONY("изменить цену пионовиднго тюльпана"),
    PRICE_TULIP("изменить цену обычного тюльпана"),
    MIN_FLOWER_COUNT("изменить цену минимальное количество цветов для доставки"),
    DISCOUNT("изменить скидку"),
    DISCOUNT_SIZE("изменить минимальное количество цветов для скидки"),
    REMOVE_FLOWER("удалить цветок"),
    ADD_FLOWER("добавить ранее удаленные цветы"),
    FIRST_DATE("начало доствки заказов"),
    LAST_DATE("окончание доставки заказов");

    String name;
    String explanation;

    ManagingSettingsButtons(String explanation) {
        this.explanation = explanation;

    }

    public String getName() {
        return name;
    }

    public String getExplanation() {
        return explanation;
    }

    public static ManagingSettingsButtons getButtonOnExplanation(String cmd) {
        for (ManagingSettingsButtons button : ManagingSettingsButtons.values()) {
            if (button.getExplanation().equals(cmd)) {
                return button;
            }
        }
        return null;
    }
}
