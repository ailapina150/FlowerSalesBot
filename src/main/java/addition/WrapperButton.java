package addition;

import java.util.Arrays;
import java.util.List;

public enum WrapperButton {
    CRAFT("крафт"),
    FILM("пленка");

    private final String name;

    WrapperButton(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public  static  List<String> getNames(){
        return Arrays.stream(WrapperButton.values()).map(WrapperButton::getName).toList();
    }

    public static WrapperButton getButtonOnNames(String cmd){
        for(WrapperButton button : WrapperButton.values()){
            if(button.name.equals(cmd)){
                return button;
            }
        }
        return null;
    }
}
