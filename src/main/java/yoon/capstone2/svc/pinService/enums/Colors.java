package yoon.capstone2.svc.pinService.enums;

import lombok.Getter;

@Getter
public enum Colors {

    BLUE("blue"),

    PURPLE("purple"),

    GREEN("green"),

    ORANGE("orange"),

//    OTHERS("others"),
    ;
    private final String color;

    Colors(String color){
        this.color = color;
    }

    public static Colors fromString(String color) {
        for (Colors c : Colors.values()) {
            if (c.color.equalsIgnoreCase(color)) {
                return c;
            }
        }
        return ORANGE;
    }

}
