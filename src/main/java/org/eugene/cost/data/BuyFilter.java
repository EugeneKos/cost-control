package org.eugene.cost.data;

public class BuyFilter {
    private BuyCategories buyCategory;
    private Limit limit;

    public BuyFilter(BuyCategories buyCategory, Limit limit) {
        this.buyCategory = buyCategory;
        this.limit = limit;
    }

    public BuyCategories getBuyCategory() {
        return buyCategory;
    }

    public Limit getLimit() {
        return limit;
    }

    public enum Limit{
        YES, NO, ALL
    }
}
