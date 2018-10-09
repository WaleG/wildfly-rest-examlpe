package org.example.config.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author Valentyn.Moliakov
 */
public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {

    @Override
    public LocalDate unmarshal(String dateInput) {
        return LocalDate.parse(dateInput, DateTimeFormatter.ISO_DATE);
    }

    @Override
    public String marshal(LocalDate localDate) {
        return DateTimeFormatter.ISO_DATE.format(localDate);
    }
}