package com.shawncheng.termtracker.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Term implements Serializable {

    private int termId;
    private String termName;
    private String startDate;
    private String endDate;

    public Term() {
    }

    public Term(int termId, String termName, String startDate, String endDate) {
        this.termId = termId;
        this.termName = termName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getTermId() {
        return termId;
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) { this.termName = termName; }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() { return endDate; }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return termName;
    }


    public static boolean checkDate(String value) {

        boolean isValid = false;
        Date date;

        if (value.trim().isEmpty()) {
            return isValid;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = dateFormat.parse(value);
            if (value.equals(dateFormat.format(date))) {
                isValid = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isValid;
    }
}