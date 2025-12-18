package com.nextepisode.tmdb_service.service.utll;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DateHelper {
    private static final DateTimeFormatter MM_DD_YYYY = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    private static final DateTimeFormatter YYYY_MM_DD = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final int MINIMUM_YEAR = 1920;

    private LocalDate now = LocalDate.now();

    public List<Integer> getYearsSequence() {
        int currentYear = this.now.getYear();
        int startYear = MINIMUM_YEAR;
        List<Integer> years = new ArrayList<>();
        while (startYear <= currentYear) {
            years.add(startYear);
            startYear++;
        }
        return years;
    }
}
