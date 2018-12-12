package com.protocol.jackson;

import java.io.IOException;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.protocol.Serializer;

/**
 * @author 17070680
 */
public class JacksonSerializer implements Serializer {
    private static final int id = 1;

    private final ObjectMapper objectMapper;

    public JacksonSerializer() {
        this(false, false);
    }

    public JacksonSerializer(boolean jacksonSmile) {
        this(jacksonSmile, false);
    }

    public JacksonSerializer(boolean jacksonSmile, boolean pretty) {
        this("yyyyMMddHHmmss", jacksonSmile, pretty);
    }

    public JacksonSerializer(String datePattern, boolean jacksonSmile, boolean pretty) {
        super();
        if (jacksonSmile) {
            objectMapper = new ObjectMapper(new SmileFactory());
        } else {
            objectMapper = new ObjectMapper();
        }
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.setSerializationInclusion(Include.NON_NULL);
        objectMapper.setDateFormat(new SimpleDateFormat(datePattern));
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, pretty);
        Hibernate4Module module = new Hibernate4Module();
        module.enable(Hibernate4Module.Feature.USE_TRANSIENT_ANNOTATION);
        objectMapper.registerModule(module);
    }

    @Override
    public <T> T transToObject(Class<T> clazz, byte[] serializeBytes) throws IOException {
        try {
            return (T) objectMapper.readValue(serializeBytes, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public byte[] transToByte(Object object) throws IOException {
        try {
            return objectMapper.writeValueAsBytes(object);
        } catch (Throwable e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Integer getId() {
        return id;
    }
}
