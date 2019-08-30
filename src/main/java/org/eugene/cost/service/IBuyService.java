package org.eugene.cost.service;

import org.eugene.cost.data.Buy;
import org.eugene.cost.data.BuyCategories;
import org.eugene.cost.data.BuyFilter;
import org.eugene.cost.data.Day;
import org.eugene.cost.data.Session;

import java.util.List;

public interface IBuyService {
    String getCostsBuys(Day day, BuyFilter filter);
    String getCostsBuys(List<Day> days, BuyFilter filter);

    List<Buy> getAllBuysByDay(Day day, BuyFilter filter);

    Buy addBuy(String price, String shopOrPlaceBuy, String descriptionBuy, boolean limited,
               BuyCategories buyCategories, Day day, Session session);

    void removeBuy(Buy buy, Day day, Session session);
}
