package org.eugene.cost.data.model;

import org.eugene.cost.util.Calculate;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Day implements Serializable {
    private final LocalDate date;
    private String limit;
    private String rate = "0";
    private boolean isClose;
    private List<Buy> buyList = new ArrayList<>();

    public Day(LocalDate date) {
        this.date = date;
    }

    public void setLimit(String limit) {
        String limitedRate = "0";
        for (Buy buy : buyList){
            if(buy.isLimited()){
                limitedRate = Calculate.plus(limitedRate,buy.getPrice());
            }
        }
        this.limit = Calculate.minus(limit, limitedRate);
    }

    public void setClose(boolean close) {
        isClose = close;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getLimit() {
        return limit;
    }

    public String getRate() {
        return rate;
    }

    public boolean isClose() {
        return isClose;
    }

    public void addBuy(Buy buy, Session session) {
        if (buyList.add(buy)) {
            rate = Calculate.plus(rate, buy.getPrice());
            if(buy.isLimited()){
                limit = Calculate.minus(limit, buy.getPrice());
                session.setBalance(Calculate.minus(session.getBalance(), buy.getPrice()));
            }
        }
    }

    public void addBuy(int index, Buy buy, Session session) {
        buyList.add(index, buy);
        rate = Calculate.plus(rate, buy.getPrice());
        if(buy.isLimited()){
            limit = Calculate.minus(limit, buy.getPrice());
            session.setBalance(Calculate.minus(session.getBalance(), buy.getPrice()));
        }

    }

    public void removeBuy(Buy buy, Session session) {
        if (buyList.remove(buy)) {
            rate = Calculate.minus(rate, buy.getPrice());
            if(buy.isLimited()){
                limit = Calculate.plus(limit, buy.getPrice());
                session.setBalance(Calculate.plus(session.getBalance(), buy.getPrice()));
            }
        }
    }

    public Buy getBuy(int index) {
        return buyList.get(index);
    }

    public List<Buy> getBuyList() {
        return buyList;
    }
}
