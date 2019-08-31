package org.eugene.cost.data;

public enum  BuyCategories {
    PRODUCT("Продукты", ShortName.P), ALCOHOL("Алкоголь", ShortName.AL), BAR("Бар", ShortName.B),

    RESTAURANT("Ресторан", ShortName.R), CLOTHES("Одежда", ShortName.C), OTHER("Прочее", ShortName.O),

    APPLIANCES("Бытовая техника", ShortName.AP), AUTOMOTIVE_TECHNOLOGY("Автомобильная техника", ShortName.AT),

    ENTERTAINMENT("Развлечение", ShortName.E), TOBACCO("Табачные изделия", ShortName.T), HEALTH("Здоровье",ShortName.H);


    private String name;
    private ShortName shortName;

    BuyCategories(String name, ShortName shortName) {
        this.name = name;
        this.shortName = shortName;
    }

    public String getName() {
        return name;
    }

    public ShortName getShortName() {
        return shortName;
    }

    public static BuyCategories getBuyCategoriesByName(String name){
        for (BuyCategories buyCategories : BuyCategories.values()){
            if(buyCategories.name.equals(name)){
                return buyCategories;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return name;
    }

    private enum ShortName{
        P, AL, B, R, C, O, AP, AT, E, T, H
    }
}
