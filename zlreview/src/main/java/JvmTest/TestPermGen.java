package JvmTest;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hl on 2019/4/10.
 */
public class TestPermGen {
    private static List<Object> insList = new ArrayList<Object>();

    public static void permLeak() throws MalformedURLException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        for (int i = 0; i < 1000; i++) {
            URL[] urls = getURLs();
            URLClassLoader urlClassloader = new URLClassLoader(urls, null);
            Class<?> logfCLass = Class.forName("org.apache.commons.logging.LogFactory", true, urlClassloader);
            Method getLog = logfCLass.getMethod("getLog", String.class);
            Object result = getLog.invoke(logfCLass, "TestPermqGen");
            insList.add(result);
            System.out.println(i + ": " + result);

        }
    }


    public static URL[] getURLs() throws MalformedURLException {
        File libDir = new File("/Users/hl/.m2/repository/commons-logging/commons-logging/1.1.1");
        File[] subFiles = libDir.listFiles();
        int count = subFiles.length;
        URL[] urls = new URL[count];
        for (int i = 0; i < count; i++) {
            urls[i] = subFiles[i].toURI().toURL();
        }
        return urls;
    }

    public static void main(String[] args) throws ClassNotFoundException, MalformedURLException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        permLeak();
    }
}
