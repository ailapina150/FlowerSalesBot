package addition;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public enum QuestionButton {
    PRICE("Сколько стоит один тюльпан?"),
    DOOR("Имеется ли доставка к двери?"),
    MIN("Какой минимальный заказ?"),
    CASH("Возможен ли безналичный расчет?"),
    REGION("Какой регион доставки?"),
    DATE("На какие числа можно сделать заказ?"),
    TIME("В какое время возможна доставка?"),
    PREPAYMENT("Требуется ли предоплата?"),
    OTHER_TOWN("Доставка в другой город? "),
    HIGH("Высота цветка?"),
    SIZE("Размер бутона?"),
    DISCOUNTER("Возможна ли скидка?"),
    AVAILABILITY("Что есть в наличии?"),
    WRAPPER("Какая упаковка?"),
    CORRECT("Как откорректировать заказ?"),
    CONNECTION("Как с Вами связаться?"),
    FOR_FREE("Как получить бесплатный букет?"),
    ASSORTMENT("Какой ассортимент цветов?");

    private final String name;

    QuestionButton(String name) {
        this.name = name;
    }

    public String getReplay() {
        return getReplay(AppProperties.get().goods, AppProperties.get().getFirstDay(), AppProperties.get().getLastDay());
    }

    public String getReplay(Goods goods) {
        return getReplay(goods, AppProperties.get().getFirstDay(), AppProperties.get().getLastDay());
    }

    public String getReplay(Goods goods, String firstData, String lastData) {
        return switch (Objects.requireNonNull(getButtonOnName(name))) {
            case PRICE -> "Цена обычного тюльпана - " + goods.getPriceTulip() + AppProperties.MONEY +
                    ", а пионовидного - " + goods.getPricePeony() + AppProperties.MONEY;
            case DOOR -> "Доставка прямо к двери и с улыбкой доставщика имеется, " +
                    "требуется указание точного адреса и номера для подтверждения заказа.";
            case MIN -> "Минимальный заказ - " + goods.getMinFlowerCount() + " цветов";
            case CASH -> "Оплата заказа наличными курьеру. Другие способы оплаты к сожалению не предусмотрены";
            case REGION -> "Регион доставки " + AppProperties.REGION;
            case DATE -> "Доставка заказов с " + firstData + " по " + lastData;
            case TIME -> "Доставка осуществляется с 8:00 утра до 2:00 ночи.";
            case PREPAYMENT -> "Предоплата - это лучшая гарантия того, что Вы не поменяете своё решение. Но мы Вам доверяем. " +
                    "Никакой предоплаты не требуется.";
            case OTHER_TOWN -> "К сожалению, доставка только в " + AppProperties.REGION +
                    " Мы постоянно что-то улучшаем, и надеемся скоро решить и этот вопрос.";
            case HIGH -> "Высота цветка от 45 - 60 см, в зависимости от сорта. Для тюльпана это шикарный размер.";
            case SIZE -> "Мы торгуем качественным цветком. Бутон большой, размером с кулак) высота 7 - 10 см, ширина 5 - 7 см.";
            case DISCOUNTER -> "Уступить хорошему клиенту пару рублей приятно. Но заказ должен быть не менее "
                    + goods.getDiscountSize() + " шт, при этом скидка будет составлять "
                    + goods.getDiscount() + "% от общей стоимости.";
            case AVAILABILITY -> "Мы заботимся о Вас и о том, чтобы Ваши цветы были доставлены в срок и в соответствии с заказом. " +
                    "За наличие не стоит переживать. В крайнем случае с Вами свяжется наш специалист";
            case WRAPPER -> " Имеется два вида упаковки: плёнка и крафт.";
            case CORRECT -> "Откорректировать сформированный и отправленный на упаковку заказ возможно только через обратную связь. " +
                    "Перед доставкой с Вами обязательно свяжутся. Будьте на связи.";
            case CONNECTION -> "Если у Вас появились дополнительные вопросы, напишите их пожалуйста в чат сразу после этого сообщения. " +
                    "Наш специалист свяжется с Вами в течении 24 часов.";
            case FOR_FREE -> "Перешлите этот бот минимум 50 абонентам telegram, отправьте сюда подтверждение рассылки " +
                    "(скриншоты или видео) и получите букет в подарок.\nНаш агент свяжется с вами в течении 24 часов.\nАкция действует по " +
                    lastData + " включительно и получить подарок можно только в " + AppProperties.REGION;
            case ASSORTMENT -> "Каждый год мы пробуем и добавляем новые сорта, которые Вас порадуют. " +
                    "Сегодня в приложение представлены более десятка видов тюльпанов.";
        };
    }

    public String getName() {
        return name;
    }

    public static List<String> getNames() {
        return Arrays.stream(QuestionButton.values()).map(QuestionButton::getName).toList();
    }

    public static QuestionButton getButtonOnName(String cmd) {
        for (QuestionButton button : QuestionButton.values()) {
            if (button.name.equals(cmd)) {
                return button;
            }
        }
        return null;
    }

    public boolean equals(QuestionButton button) {
        return (this.name.equals(button.getName()));
    }

}
