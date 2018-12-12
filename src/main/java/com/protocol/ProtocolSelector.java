package com.protocol;
import com.protocol.hessian.HessianSerializer;
import com.protocol.jackson.JacksonSerializer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/11/30
 */
public class ProtocolSelector {
    public static final int DEFAULT_PROTOCOL = 1;

    private static Map<Integer, Serializer> serializerMap = new HashMap<>();
    private static Map<String, Integer> protocolIds = new HashMap<>();
    static{
        putSerializer(new HessianSerializer());
        putSerializer(new JacksonSerializer());
        protocolIds.put("HESSIAN", new HessianSerializer().getId());
        protocolIds.put("JACKSON", new JacksonSerializer().getId());
    }

    private static void putSerializer(Serializer serializer){
        serializerMap.put(serializer.getId(), serializer);
    }

    public static Serializer getProtocol(int protocol){
            return serializerMap.get(protocol);
    }

    public static Integer getProtocolId(String protocol){
        return protocolIds.get(protocol);
    }

}
