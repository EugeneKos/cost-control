package org.eugene.cost.logic.model.limit;

import org.eugene.cost.logic.model.card.bank.Bank;

import java.io.Serializable;

public class Buy implements Serializable {
    private String price;
    private String shopOrPlaceBuy;
    private String descriptionBuy;
    private boolean isLimited;
    private Bank payment;
    private BuyCategories buyCategories;

    public Buy(String price, String shopOrPlaceBuy, String descriptionBuy, boolean isLimited, Bank payment, BuyCategories buyCategories) {
        this.price = price;
        this.shopOrPlaceBuy = shopOrPlaceBuy;
        this.descriptionBuy = descriptionBuy;
        this.isLimited = isLimited;
        this.payment = payment;
        this.buyCategories = buyCategories;
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

    public BuyCategories getBuyCategories() {
        return buyCategories;
    }

    private String getLimitedInfo(){
        if(isLimited) return "[L] ";
        return "[NL] ";
    }

    private String getShortNameCategoryBuy(){
        return "["+buyCategories.getShortName()+"] ";
    }

    @Override
    public String toString() {
        return getLimitedInfo() + getShortNameCategoryBuy() + price +" Руб. "+shopOrPlaceBuy;
    }
}
