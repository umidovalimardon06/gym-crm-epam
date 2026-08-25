package com.epam.workload.domain;

import java.util.ArrayList;
import java.util.List;

public class YearSummary {

    private int year;

    private List<MonthSummary> months = new ArrayList<>();

    public YearSummary() {
    }

    public YearSummary(int year) {
        this.year = year;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<MonthSummary> getMonths() {
        return months;
    }

    public void setMonths(List<MonthSummary> months) {
        this.months = months;
    }
}