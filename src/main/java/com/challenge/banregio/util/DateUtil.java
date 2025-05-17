package com.challenge.banregio.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtil {

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static LocalDate toLocalDate(String date) {
        return LocalDate.parse(date, dtf);
    }

    public static String toStringDate(LocalDate date) {
        return dtf.format(date);
    }
}
