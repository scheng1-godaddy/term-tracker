package com.shawncheng.termtracker.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

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
