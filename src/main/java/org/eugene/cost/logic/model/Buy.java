package org.eugene.cost.logic.model;

import java.io.Serializable;

public class Buy implements Serializable {
    private String price;
    private String shopOrPlaceBuy;
    private String descriptionBuy;
    private boolean isLimited;

    public Buy(String price, String shopOrPlaceBuy, String descriptionBuy, boolean isLimited) {
        this.price = price;
        this.shopOrPlaceBuy = shopOrPlaceBuy;
        this.descriptionBuy = descriptionBuy;
        this.isLimited = isLimited;
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

    public void setLimited(boolean limited) {
        isLimited = limited;
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

    public boolean isLimited() {
        return isLimited;
    }

    private String getLimitedInfo(){
        if(isLimited) return "[L] ";
        return "[NL] ";
    }

    @Override
    public String toString() {
        return getLimitedInfo() + price +" Руб. "+shopOrPlaceBuy;
    }
}
