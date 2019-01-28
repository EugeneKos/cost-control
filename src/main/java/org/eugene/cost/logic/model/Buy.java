package org.eugene.cost.logic.model;

import java.io.Serializable;

public class Buy implements Serializable {
    private String price;
    private String shopOrPlaceBuy;
    private String descriptionBuy;

    public Buy(String price, String shopOrPlaceBuy, String descriptionBuy) {
        this.price = price;
        this.shopOrPlaceBuy = shopOrPlaceBuy;
        this.descriptionBuy = descriptionBuy;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setShopOrPlaceBuy(String shopOrPlaceBuy) {
        this.shopOrPlaceBuy = shopOrPlaceBuy;
    }

    public void setDescriptionBuy(String descriptionBuy) {
        this.descriptionBuy = descriptionBuy;
    }

    public String getPrice() {
        return price;
    }

    public String getShopOrPlaceBuy() {
        return shopOrPlaceBuy;
    }

    public String getDescriptionBuy() {
        return descriptionBuy;
    }

    @Override
    public String toString() {
        return price +" Руб. "+shopOrPlaceBuy;
    }
}
