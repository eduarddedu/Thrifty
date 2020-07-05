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
    public void serialize(LocalDate localDate, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("year", localDate.getYear());
        gen.writeNumberField("month", localDate.getMonthValue());
        gen.writeNumberField("day", localDate.getDayOfMonth());
        gen.writeEndObject();
    }
}