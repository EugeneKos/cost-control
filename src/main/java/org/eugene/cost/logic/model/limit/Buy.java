package org.eugene.cost.logic.model.limit;

import org.eugene.cost.logic.model.card.bank.Bank;

import java.io.Serializable;

public class Buy implements Serializable {
    private String price;
    private String shopOrPlaceBuy;
    private String descriptionBuy;
    private boolean isLimited;
    private Bank payment;

    public Buy(String price, String shopOrPlaceBuy, String descriptionBuy, boolean isLimited, Bank payment) {
        this.price = price;
        this.shopOrPlaceBuy = shopOrPlaceBuy;
        this.descriptionBuy = descriptionBuy;
        this.isLimited = isLimited;
        this.payment = payment;
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

    public Bank getPayment() {
        return payment;
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
