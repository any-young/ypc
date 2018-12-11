package com.protocol;
import com.protocol.hessian.HessianSerializer;

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
public class ProtocolSeletor {


    private static Map<Integer, Serializer> serializerMap = new HashMap<>();
    static{
        putSerializer(new HessianSerializer());
    }

    private static void putSerializer(Serializer serializer){
        serializerMap.put(serializer.getId(), serializer);
    }

    public static Serializer getProtocol(int protocol){
            return serializerMap.get(protocol);
    }

}
