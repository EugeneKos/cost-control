package org.eugene.cost.service;

import org.eugene.cost.data.Buy;
import org.eugene.cost.data.BuyCategories;
import org.eugene.cost.data.Day;
import org.eugene.cost.data.Session;

import java.util.List;

public interface IBuyService {
    String getCostsBuys(Day day);

    String getCostsBuys(Day day, boolean limited);
    String getCostsBuys(List<Day> days, boolean limited);

    List<Buy> getAllBuysByDay(Day day);

    List<Buy> getAllLimitedBuysByDay(Day day);
    List<Buy> getAllNonLimitedBuysByDay(Day day);

    Buy addBuy(String price, String shopOrPlaceBuy, String descriptionBuy, boolean limited,
               BuyCategories buyCategories, Day day, Session session);

    void removeBuy(Buy buy, Day day, Session session);
}
