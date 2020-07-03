package org.codecritique.thrifty.jackson.databind;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.time.LocalDate;

/**
 * @author Eduard Dedu
 */

public class LocalDateDeserializer extends JsonDeserializer<LocalDate>  {

    @Override
    public LocalDate deserialize(JsonParser jp, DeserializationContext context) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        return LocalDate.of(node.get("year").asInt(), node.get("month").asInt(), node.get("day").asInt());
    }
}