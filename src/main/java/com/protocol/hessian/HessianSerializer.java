package com.protocol.hessian;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.protocol.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * say some thing
 *
 * @version v1.0
 * @author angyang
 * @date 2018/12/1
 */
public class HessianSerializer implements Serializer {

    private Integer id = 1;

    public <T> T transToObject(Class<T> clazz, byte[] bytes) throws IOException {
        System.out.println(new String(bytes));
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        Hessian2Input input = new Hessian2Input(in);
        try {
            //返回读取到的对象序列化
            return (T)input.readObject();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public byte[] transToByte(Object object) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Hessian2Output hessian2Output = new Hessian2Output(out);
        try {
            hessian2Output.writeObject(object);
            hessian2Output.close();
            return out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public Integer getId() {
        return id;
    }

}
