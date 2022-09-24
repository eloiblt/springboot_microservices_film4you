package fr.cpe.filmforyou.filmcore.config.convert;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class MongoStringToLocalDateConverter implements Converter<String, LocalDate> {

    private final DateTimeFormatter formatter;

    public MongoStringToLocalDateConverter() {
        this.formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd").withLocale(Locale.FRANCE);
    }

    @Override
    public LocalDate convert(String source) {
        if (!source.isEmpty() && !source.isBlank()) {
            source = source.split("T")[0];
            return LocalDate.parse(source, formatter);
        }
        return null;
    }
}
