package org.eugene.cost.service.impl;

import org.eugene.cost.data.Buy;
import org.eugene.cost.data.BuyCategories;
import org.eugene.cost.data.BuyFilter;
import org.eugene.cost.data.Day;
import org.eugene.cost.data.Session;
import org.eugene.cost.service.IBuyService;
import org.eugene.cost.service.util.Calculate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BuyServiceImpl implements IBuyService {
    @Override
    public String getCostsBuys(Day day, BuyFilter filter) {
        String rateOnDay = "0";
        List<Buy> buys = getAllBuysByDay(day, filter);
        for (Buy buy : buys){
            rateOnDay = Calculate.plus(rateOnDay, buy.getPrice());
        }
        return rateOnDay;
    }

    @Override
    public String getCostsBuys(List<Day> days, BuyFilter filter) {
        String rateOnDays = "0";
        for (Day day : days){
            rateOnDays = Calculate.plus(rateOnDays, getCostsBuys(day, filter));
        }
        return rateOnDays;
    }

    @Override
    public List<Buy> getAllBuysByDay(Day day, BuyFilter filter) {
        return day.getBuys().stream()
                .filter(buy -> filtering(buy, filter))
                .collect(Collectors.toList());
    }

    @Override
    public List<Buy> getAllBuysBySession(Session session, BuyFilter filter) {
        List<Buy> buys = new LinkedList<>();
        for (Day day : session.getDays()){
            buys.addAll(getAllBuysByDay(day, filter));
        }
        return buys;
    }

    private boolean filtering(Buy buy, BuyFilter filter){
        boolean match = true;
        if(filter.getBuyCategory() != null){
            match = buy.getBuyCategories() == filter.getBuyCategory();
        }
        switch (filter.getLimit()){
            case YES:
                match &= buy.isLimited();
                break;
            case NO:
                match &= !buy.isLimited();
                break;
        }
        return match;
    }

    @Override
    public Buy addBuy(String price, String shopOrPlaceBuy, String descriptionBuy, boolean limited,
                      BuyCategories buyCategories, String paymentIdentify, Day day, Session session) {

        Buy buy = new Buy(price, shopOrPlaceBuy, descriptionBuy, limited, buyCategories, paymentIdentify);
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

    @Override
    public Map<String, Double> getLimitBuyCostsByCategories(Session session) {
        return getBuyCostsByCategories(session, BuyFilter.Limit.YES);
    }

    @Override
    public Map<String, Double> getNonLimitBuyCostsByCategories(Session session) {
        return getBuyCostsByCategories(session, BuyFilter.Limit.NO);
    }

    private Map<String, Double> getBuyCostsByCategories(Session session, BuyFilter.Limit limit){
        Map<String, Double> limitBuyCosts = new HashMap<>();
        List<Day> days = session.getDays();
        for (BuyCategories buyCategory : BuyCategories.values()){
            String costsBuys = getCostsBuys(days, new BuyFilter(buyCategory, limit));
            limitBuyCosts.put(buyCategory.getName(), Double.valueOf(costsBuys));
        }
        return limitBuyCosts;
    }
}
