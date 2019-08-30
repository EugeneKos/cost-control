package org.eugene.cost.data;

import java.time.LocalDate;
import java.util.Objects;

public class SessionDetail {
    private final String limit;
    private final LocalDate beginDate;
    private final LocalDate finalDate;

    public SessionDetail(String limit, LocalDate beginDate, LocalDate finalDate) {
        this.limit = limit;
        this.beginDate = beginDate;
        this.finalDate = finalDate;
    }

    public String getLimit() {
        return limit;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public LocalDate getFinalDate() {
        return finalDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SessionDetail detail = (SessionDetail) o;
        return Objects.equals(limit, detail.limit) &&
                Objects.equals(beginDate, detail.beginDate) &&
                Objects.equals(finalDate, detail.finalDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(limit, beginDate, finalDate);
    }
}
