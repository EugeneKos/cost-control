package org.eugene.cost.service;

import org.eugene.cost.data.Buy;
import org.eugene.cost.data.BuyCategories;
import org.eugene.cost.data.BuyFilter;
import org.eugene.cost.data.Day;
import org.eugene.cost.data.Session;

import java.util.List;
import java.util.Map;

public interface IBuyService {
    String getCostsBuys(Day day, BuyFilter filter);
    String getCostsBuys(List<Day> days, BuyFilter filter);

    List<Buy> getAllBuysByDay(Day day, BuyFilter filter);
    List<Buy> getAllBuysBySession(Session session, BuyFilter filter);

    Buy addBuy(String price, String shopOrPlaceBuy, String descriptionBuy, boolean limited,
               BuyCategories buyCategories, String paymentIdentify, Day day, Session session);

    void removeBuy(Buy buy, Day day, Session session);

    Map<String, Double> getLimitBuyCostsByCategories(Session session);
    Map<String, Double> getNonLimitBuyCostsByCategories(Session session);
}
