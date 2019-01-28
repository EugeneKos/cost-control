package org.eugene.cost.logic.model;

import org.eugene.cost.logic.util.Calculate;
import org.eugene.cost.logic.exeption.IncorrectDateException;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Session implements Serializable {
    private final String limit;
    private String balance;
    private final LocalDate beginDate;
    private final LocalDate finalDate;
    private final List<Day> dayList = new ArrayList<>();

    public Session(String limit, LocalDate beginDate, LocalDate finalDate) throws IncorrectDateException {
        this.limit = limit;
        this.balance = limit;
        this.beginDate = beginDate;
        this.finalDate = finalDate;
        if(checkDates()){
            initSessionDays();
        } else {
            throw new IncorrectDateException("error date");
        }
    }

    private boolean checkDates(){
        if(!beginDate.isEqual(LocalDate.now())){
            return false;
        }
        return beginDate.isBefore(finalDate);
    }

    private void initSessionDays(){
        dayList.add(new Day(beginDate));
        int i = 1;
        LocalDate current = beginDate.plusDays(i);
        while (!current.isEqual(finalDate)){
            dayList.add(new Day(current));
            i++;
            current = beginDate.plusDays(i);
        }
    }

    public boolean isActiveSession(){
        return finalDate.isAfter(LocalDate.now());
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public LocalDate getFinalDate() {
        return finalDate;
    }

    public String getLimit() {
        return limit;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getBalance() {
        return balance;
    }

    public Day getDay(LocalDate date){
        for (Day day : dayList){
            if(day.getDate().isEqual(date)){
                return day;
            }
        }
        return null;
    }

    public List<Day> getDayList() {
        return dayList;
    }

    private int getNumOpenDays(){
        int num = 0;
        for (Day day : dayList){
            if(!day.isClose()){
                num++;
            }
        }
        return num;
    }

    public void calculateMediumLimit(){
        int numOpenDays = getNumOpenDays();
        if(numOpenDays == 0) return;
        String mediumLimit = limit;
        for (Day day : dayList){
            if(day.isClose()){
                mediumLimit = Calculate.minus(mediumLimit,day.getRate());
            }
        }
        mediumLimit = Calculate.divide(mediumLimit,""+numOpenDays);
        for (Day day : dayList){
            if(!day.isClose()){
                day.setLimit(mediumLimit);
            }
        }
    }

    public void autoCloseDays(){
        for (Day day : dayList){
            if(day.getDate().isBefore(LocalDate.now())){
                day.setClose(true);
            }
        }
        calculateMediumLimit();
    }
}
