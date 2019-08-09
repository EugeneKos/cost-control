package org.eugene.cost.service;

import org.eugene.cost.data.Day;

import java.util.List;

public interface IBuyService {
    String getRateByNonLimitBuys(Day day);
    String getRateByNonLimitBuys(List<Day> days);
}
