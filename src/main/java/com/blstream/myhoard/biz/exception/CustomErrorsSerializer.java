package com.blstream.myhoard.biz.exception;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

public class CustomErrorsSerializer extends JsonSerializer<Map<String, String>> {

    @Override
    public void serialize(Map<String, String> t, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
        if (!t.isEmpty()) {
            jg.writeStartObject();
            for (Entry<String, String> e : t.entrySet())
                jg.writeStringField(e.getKey(), e.getValue());
            jg.writeEndObject();
        }
    }
    
}
