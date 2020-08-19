package org.chabug.utils;

// 通过class文件读取字节码
public class Hello {

    public static void main(String[] args) throws Exception {

        byte[] bytes = Serializables.getBytesByFile("E:\\code\\java\\weblogic_exp\\target\\classes\\MyFilter.class");
        String s = "";
        for (byte b : bytes) {
            s += String.format("%s%s", b, ",");
        }
        System.out.println(s);
    }
}
