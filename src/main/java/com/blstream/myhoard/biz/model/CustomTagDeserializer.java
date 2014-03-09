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

public class CustomTagDeserializer extends JsonDeserializer<Set<TagDTO>> {

    @Override
    public Set<TagDTO> deserialize(JsonParser jp, DeserializationContext dc) throws IOException, JsonProcessingException {
        Set<TagDTO> set = new HashSet<>();
        ObjectCodec oc = jp.getCodec();
        Iterator<String> i =  oc.readValues(jp, String.class);
        while (i.hasNext()) {
            TagDTO m = new TagDTO();
            m.setTag(i.next());
            set.add(m);
        }
        return set;
    }
    
}
