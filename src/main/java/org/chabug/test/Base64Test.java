package org.chabug.test;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.IOException;

public class Base64Test {
    public static void main(String[] args) throws IOException {

        BASE64Decoder b64Decoder = new sun.misc.BASE64Decoder();
        BASE64Encoder base64Encoder = new BASE64Encoder();
        File file = new File("E:\\code\\java\\weblogic_exp\\src\\main\\java\\com\\chabug\\payloads\\MyFilter.java");

    }
}
