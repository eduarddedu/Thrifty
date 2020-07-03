package org.codecritique.thrifty.jackson.databind;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDate;

/**
 * @author Eduard Dedu
 */

public class LocalDateSerializer extends JsonSerializer<LocalDate> {

    @Override
    public void serialize(LocalDate localDate, JsonGenerator jg, SerializerProvider provider) throws IOException {
        jg.writeStartObject();
        jg.writeNumberField("year", localDate.getYear());
        jg.writeNumberField("month", localDate.getMonthValue());
        jg.writeNumberField("day", localDate.getDayOfMonth());
        jg.writeEndObject();
    }
}