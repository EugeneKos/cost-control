package org.eugene.cost.data;

import java.io.Serializable;

public class Buy implements Serializable {
    private static final long serialVersionUID = -432701262098123832L;

    private String price;
    private String shopOrPlaceBuy;
    private String descriptionBuy;
    private boolean limited;
    private BuyCategories buyCategories;
    private String paymentIdentify;

    public Buy(String price,
               String shopOrPlaceBuy,
               String descriptionBuy,
               boolean limited,
               BuyCategories buyCategories,
               String paymentIdentify) {

        this.price = price;
        this.shopOrPlaceBuy = shopOrPlaceBuy;
        this.descriptionBuy = descriptionBuy;
        this.limited = limited;
        this.buyCategories = buyCategories;
        this.paymentIdentify = paymentIdentify;
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
        return limited;
    }

    public BuyCategories getBuyCategories() {
        return buyCategories;
    }

    public String getPaymentIdentify() {
        return paymentIdentify;
    }

    @Override
    public String toString() {
        return (limited ? "[L] " : "[NL] ")
                + "[" + buyCategories.getShortName() + "] "
                + price + " Руб. "
                + shopOrPlaceBuy;
    }
}
