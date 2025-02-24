package addition;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AppProperties {

    //public static final String PROXY_HOST = "39.102.211.162";
    public static final String PROXY_HOST = "167.172.86.46";
    //public static final int PROXY_PORT = 8080;

    public static final int PROXY_PORT = 10471;

    public static final long BOT_USER_ID = 7860021560L;
    public static final String BOT_USER_NAME = "Vlad3456bot";
    public static final String BOT_TOKEN = System.getenv("TELEGRAM_TOKEN");

    public static final Long ORDERS_CHAT_ID = -1002321853789L;
    public static final Long COLLECTOR_CHAT_ID = -1002353117943L;
    public static final Long PRESENT_CHAR_ID = -1002305542505L;
    public static final String PASSWORD = "Настройки";

    public static final String PEONY = "peony";
    public static final String TULIP = "tulip";
    public static final String PHOTO = "photo";
    public static final String VIDEO = "video";
    public static final String INSTRUCTION = "instruction";

    public static final String REGION = "г. Брест";
    public static final int NUMBER_PHOTO_IN_GROUP = 2;
    public static final int NUMBER_BUTTON_IN_ROW = 2;
    public static final String MONEY = "p";

    public LocalDate firstDay = LocalDate.of(2025, 2, 13);
    public LocalDate lastDay = LocalDate.of(2025, 3, 16);
    public Goods goods = new Goods();

    public static AppProperties appProperties;

    private AppProperties() {
    }

    public static synchronized AppProperties get() {
        if (appProperties == null) {
            appProperties = new AppProperties();
        }
        return appProperties;
    }

    public String getFirstDay() {
        LocalDate nowDay = LocalDate.now().plusDays(1);
        if (nowDay.isAfter(firstDay)) return nowDay.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        return firstDay.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public String getLastDay() {
        return lastDay.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public void setFirstDay(LocalDate date) {
        firstDay = date;
    }

    public void setLastDay(LocalDate date) {
        lastDay = date;
    }
}
