package com.blstream.myhoard.biz.model;

import java.io.IOException;
import java.util.Set;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

public class CustomMediaSerializer extends JsonSerializer<Set<MediaDTO>> {

    @Override
    public void serialize(Set<MediaDTO> t, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
        jg.writeStartArray();
        for (MediaDTO i : t) {
            jg.writeStartObject();
            jg.writeStringField("id", i.getId());
            jg.writeStringField("url", "http://myhoard.host/media/" + i.getId());	// TODO: myhoard.host
            jg.writeEndObject();
        }
        jg.writeEndArray();
    }

}
