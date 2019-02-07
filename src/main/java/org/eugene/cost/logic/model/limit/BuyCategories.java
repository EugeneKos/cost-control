package org.eugene.cost.logic.model.limit;

public enum  BuyCategories {
    PRODUCT("Продукты"), ALCOHOL("Алкоголь"), BAR("Бар"),

    RESTAURANT("Ресторан"), CLOTHES("Одежда"), OTHER("Прочее"),

    APPLIANCES("Бытовая техника"), AUTOMOTIVE_TECHNOLOGY("Автомобильная техника");


    private String name;
    private String description;

    BuyCategories(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return name;
    }
}
