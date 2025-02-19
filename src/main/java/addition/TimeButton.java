package addition;

import java.util.Arrays;
import java.util.List;

public enum TimeButton {
    TIME_08_11("08-11"),
    TIME_11_14("11-14"),
    TIME_14_17("14-17"),
    TIME_17_20("17-20"),
    TIME_20_23("20-23"),
    TIME_23_02("23-02");

    public String getName() {
        return name;
    }

    private final String name;

    TimeButton(String name) {
        this.name = name;
    }

    public  static  List<String> getNames(){
        return Arrays.stream(TimeButton.values()).map(TimeButton::getName).toList();
    }

    public static TimeButton getButtonOnName(String name){
        for(TimeButton button : TimeButton.values()){
            if(button.getName().equals(name)){
                return button;
            }
        }
        return null;
    }
    public boolean equals(TimeButton button){
        return (this.name.equals(button.getName().toLowerCase()));
    }
}
