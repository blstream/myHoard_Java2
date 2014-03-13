package com.blstream.myhoard.biz.model;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

public class CustomMediaDeserializer extends JsonDeserializer<Set<MediaDTO>> {

    @Override
    public Set<MediaDTO> deserialize(JsonParser jp, DeserializationContext dc) throws IOException, JsonProcessingException {
        Set<MediaDTO> set = new HashSet<>();
        ObjectCodec oc = jp.getCodec();
        Iterator<String> i =  oc.readValues(jp, String.class);
        while (i.hasNext()) {
            MediaDTO m = new MediaDTO();
            m.setId(i.next());
            set.add(m);
        }
        return set;
    }
    
}
