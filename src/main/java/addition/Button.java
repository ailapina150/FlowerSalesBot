package addition;

import java.util.Arrays;
import java.util.List;

public enum Button {
    YES("да"),
    NO("нет");

    public String getName() {
        return name;
    }

    private final String name;

    Button(String name) {
        this.name = name;
    }

    public static List<String> getNames(){
        return Arrays.stream(Button.values()).map(Button::getName).toList();
    }

    public static Button getButtonOnNames(String cmd){
        for(Button button : Button.values()){
            if(button.getName().equals(cmd)){
                return button;
            }
        }
        return  null;
    }
    public boolean equals(Button button){
        return (this.name.equals(button.getName().toLowerCase()));
    }
}
