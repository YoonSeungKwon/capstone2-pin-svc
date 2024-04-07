package yoon.capstone2.svc.pinService.enums;

import lombok.Getter;

@Getter
public enum Category {

    GROCERIES("식료품"),

    HOUSEHOLD_ITEMS("생활용품"),

    CLOTHING("의류 및 액세서리"),

    HOUSING("주거 및 생활비"),

    TRANSPORTATION("교통"),

    LEISURE_AND_CULTURE("여가 및 문화"),

    HEALTHCARE("의료 및 건강"),

    FINANCIAL_AND_TAXES("금융 및 세금"),

    OTHERS("기타");

    private final String category;

    Category(String category){
        this.category = category;
    }

}
