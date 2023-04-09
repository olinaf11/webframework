package etu2028.framework.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Util {
    public static List<Class> searchClassBypackage(String packageName) throws URISyntaxException, ClassNotFoundException {
        String path = packageName.replaceAll("[.]", "/").trim();
        URL packageUrl = Thread.currentThread().getContextClassLoader().getResource(path);
        File packDir =new File(packageUrl.toURI());
        File[] inside = packDir.listFiles(file->file.getName().endsWith(".class"));
        List<Class> lists = new ArrayList<>();
        for(File f : inside){
               String c = packageName+"."+f.getName().substring(0,f.getName().lastIndexOf("."));
               lists.add(Class.forName(c.trim()));
        }
        return lists;
    }

    private static Class getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "."
                    + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            // handle the exception
        }
        return null;
    }
}
