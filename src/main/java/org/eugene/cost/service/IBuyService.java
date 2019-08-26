package org.eugene.cost.service;

import org.eugene.cost.data.Buy;
import org.eugene.cost.data.BuyCategories;
import org.eugene.cost.data.Day;
import org.eugene.cost.data.Session;

import java.util.List;

public interface IBuyService {
    String getRateByNonLimitBuys(Day day);
    String getRateByNonLimitBuys(List<Day> days);

    List<Buy> getAllBuysByDay(Day day);

    List<Buy> getAllLimitedBuysByDay(Day day);
    List<Buy> getAllNonLimitedBuysByDay(Day day);

    Buy addBuy(String price, String shopOrPlaceBuy, String descriptionBuy, boolean limited,
               BuyCategories buyCategories, Day day, Session session);

    void removeBuy(Buy buy, Day day, Session session);
}
