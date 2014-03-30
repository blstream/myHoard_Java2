package com.blstream.myhoard.biz.model;

import java.io.IOException;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class CustomMediaSerializer extends JsonSerializer<Set<MediaDTO>> {

    public final String host;

    public CustomMediaSerializer() {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
        host = "http://" + request.getServerName() + ":" + request.getServerPort() + "/media/";
    }

    @Override
    public void serialize(Set<MediaDTO> t, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
        jg.writeStartArray();
        for (MediaDTO i : t) {
            jg.writeStartObject();
            jg.writeStringField("id", i.getId());
            jg.writeStringField("url", host + i.getId());
            jg.writeEndObject();
        }
        jg.writeEndArray();
    }

}
