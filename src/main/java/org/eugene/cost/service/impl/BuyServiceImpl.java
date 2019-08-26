package org.eugene.cost.service.impl;

import org.eugene.cost.data.Buy;
import org.eugene.cost.data.BuyCategories;
import org.eugene.cost.data.Day;
import org.eugene.cost.data.Session;
import org.eugene.cost.service.IBuyService;
import org.eugene.cost.util.Calculate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BuyServiceImpl implements IBuyService {
    @Override
    public String getRateByNonLimitBuys(Day day) {
        String rateOnDay = "0";
        if(!day.isClose()){
            return rateOnDay;
        }
        List<Buy> buys = day.getBuys();
        if(buys == null){
            return rateOnDay;
        }
        for (Buy buy : buys){
            if(!buy.isLimited()){
                rateOnDay = Calculate.plus(rateOnDay, buy.getPrice());
            }
        }
        return rateOnDay;
    }

    @Override
    public String getRateByNonLimitBuys(List<Day> days) {
        String rateOnDays = "0";
        for (Day day : days){
            rateOnDays = Calculate.plus(rateOnDays, getRateByNonLimitBuys(day));
        }
        return rateOnDays;
    }

    @Override
    public List<Buy> getAllBuysByDay(Day day) {
        return day.getBuys();
    }

    @Override
    public List<Buy> getAllLimitedBuysByDay(Day day) {
        return day.getBuys().stream().filter(Buy::isLimited).collect(Collectors.toList());
    }

    @Override
    public List<Buy> getAllNonLimitedBuysByDay(Day day) {
        return day.getBuys().stream().filter(buy -> !buy.isLimited()).collect(Collectors.toList());
    }

    @Override
    public Buy addBuy(String price, String shopOrPlaceBuy, String descriptionBuy, boolean limited,
                      BuyCategories buyCategories, Day day, Session session) {

        Buy buy = new Buy(price, shopOrPlaceBuy, descriptionBuy, limited, buyCategories);
        List<Buy> buys = day.getBuys();
        buys.add(buy);
        day.setBuys(buys);
        day.setRate(Calculate.plus(day.getRate(), price));

        if(limited){
            day.setLimit(Calculate.minus(day.getLimit(), price));
            session.setBalance(Calculate.minus(session.getBalance(), price));
        }

        return buy;
    }

    @Override
    public void removeBuy(Buy buy, Day day, Session session) {
        List<Buy> buys = day.getBuys();
        buys.remove(buy);
        day.setBuys(buys);
        day.setRate(Calculate.plus(day.getRate(), buy.getPrice()));

        if(buy.isLimited()){
            day.setLimit(Calculate.plus(day.getLimit(), buy.getPrice()));
            session.setBalance(Calculate.plus(session.getBalance(), buy.getPrice()));
        }
    }
}
