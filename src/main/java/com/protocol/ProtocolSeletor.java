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

    enum SerialEnum{
        HESSIAN(new HessianSerializer());
        private Serializer serializer;

        SerialEnum(Serializer serializer){
            this.serializer = serializer;
        }

        public Serializer getSerializer() {
            return serializer;
        }
    }

    private static Map<String, Serializer> serializerMap = new HashMap<>();
    static{
        Arrays.stream(SerialEnum.values()).forEach(serialEnum -> putSerializer(serialEnum.getSerializer()));
        putSerializer(new HessianSerializer());
    }

    private static void putSerializer(Serializer serializer){
        serializerMap.put(serializer.getName(), serializer);
    }


    public static Serializer getProtocol(String protocol) {
        if (protocol!=null){
            return serializerMap.get(protocol);
        }
        return null;
    }

}
