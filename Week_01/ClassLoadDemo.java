package com.ditop.lease.api.config;

import javax.xml.bind.DatatypeConverter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class ClassLoadDemo extends ClassLoader{
    private String filePath;

    public static void main(String[] args) {
        String fileName = "Hello";
        String path = "D:/demo/";
        try {
            Object instance = new ClassLoadDemo(path).findClass(fileName).newInstance();
            Method[] methods = instance.getClass().getMethods();
            for(Method m : methods){
                m.invoke(instance);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public ClassLoadDemo(String filePath) {
        this.filePath = filePath;
    }

    @Override
    protected Class<?> findClass(String fileName) {
        try {
            byte[] bytes = decode(getFileBase64(fileName));
            for(int i=0;i<bytes.length;i++){
                int newByte = 255 - bytes[i];
                bytes[i] = (byte) newByte;
            }
            return defineClass(fileName,bytes,0,bytes.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getFileBase64(String name) throws Exception {
        String fullName = filePath + name + ".xlass";
        String base64 = DatatypeConverter.printBase64Binary(Files.readAllBytes(
                Paths.get(fullName)));
        return base64;
    }

    private byte[] decode(String base64){
        return Base64.getDecoder().decode(base64);
    }
}
