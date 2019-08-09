package org.eugene.cost.data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// todo: delete comment code
public class Day implements Serializable {
    private final LocalDate date;
    private String limit;
    private String rate;
    private boolean close;
    private List<Buy> buys = new ArrayList<>();

    public Day(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    /*public void setLimit(String limit) {
        String limitedRate = "0";
        for (Buy buy : buys){
            if(buy.isLimited()){
                limitedRate = Calculate.plus(limitedRate,buy.getPrice());
            }
        }
        this.limit = Calculate.minus(limit, limitedRate);
    }*/

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public boolean isClose() {
        return close;
    }

    public void setClose(boolean close) {
        this.close = close;
    }

    /*public void addBuy(Buy buy, Session session) {
        if (buys.add(buy)) {
            rate = Calculate.plus(rate, buy.getPrice());
            if(buy.isLimited()){
                limit = Calculate.minus(limit, buy.getPrice());
                session.setBalance(Calculate.minus(session.getBalance(), buy.getPrice()));
            }
        }
    }

    public void addBuy(int index, Buy buy, Session session) {
        buys.add(index, buy);
        rate = Calculate.plus(rate, buy.getPrice());
        if(buy.isLimited()){
            limit = Calculate.minus(limit, buy.getPrice());
            session.setBalance(Calculate.minus(session.getBalance(), buy.getPrice()));
        }

    }

    public void removeBuy(Buy buy, Session session) {
        if (buys.remove(buy)) {
            rate = Calculate.minus(rate, buy.getPrice());
            if(buy.isLimited()){
                limit = Calculate.plus(limit, buy.getPrice());
                session.setBalance(Calculate.plus(session.getBalance(), buy.getPrice()));
            }
        }
    }

    public Buy getBuy(int index) {
        return buys.get(index);
    }*/

    public List<Buy> getBuys() {
        return buys;
    }

    public void setBuys(List<Buy> buys) {
        this.buys = buys;
    }
}
