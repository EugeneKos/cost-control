package org.eugene.cost.service.impl;

import org.eugene.cost.data.Buy;
import org.eugene.cost.data.Day;
import org.eugene.cost.service.IBuyService;
import org.eugene.cost.util.Calculate;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
