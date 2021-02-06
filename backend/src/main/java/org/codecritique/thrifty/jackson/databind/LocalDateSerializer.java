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
        gen.writeString(String.valueOf(localDate));
    }
}