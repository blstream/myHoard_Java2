package com.blstream.myhoard.biz.model;

import java.io.IOException;
import java.util.Set;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

public class CustomTagSerializer extends JsonSerializer<Set<TagDTO>> {

    @Override
    public void serialize(Set<TagDTO> t, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
        jg.writeStartArray();
        for (TagDTO i : t) {
            jg.writeStartObject();
            jg.writeString(i.getTag());
            jg.writeEndObject();
        }
        jg.writeEndArray();
    }

}
