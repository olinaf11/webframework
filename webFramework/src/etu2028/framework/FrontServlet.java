package etu2028.framework.servlet;

import etu2028.framework.ModelView;
import etu2028.framework.Utils.Util;
import etu2028.framework.annotation.Url;
import etu2028.framework.Mapping;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class FrontServlet extends HttpServlet {

    private HashMap<String , Mapping> mappingUrls;

    public HashMap<String, Mapping> getMappingUrls() {
        return mappingUrls;
    }


    @Override
    public void init() {
        ArrayList<Class> classes = null;
        setMappingUrls(new HashMap<String,Mapping>());
        try {
            String packageName = getInitParameter("packages").trim();
            classes = new ArrayList<>(Util.searchClassBypackage(packageName));
            for (int i = 0; i < classes.size(); i++) {
                Method[] methods = classes.get(i).getMethods();
                for (Method method:methods) {
                    if (method.isAnnotationPresent(Url.class)){
                        Mapping mapping = new Mapping(classes.get(i).getName().trim(), method.getName().trim());
                        getMappingUrls().put("/"+method.getAnnotation(Url.class).name().trim(), mapping);
                    }
                }
            }
        } catch (URISyntaxException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public void setMappingUrls(HashMap<String, Mapping> mappingUrls) {
        this.mappingUrls = mappingUrls;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.processRequest(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.processRequest(req, resp);
    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        getMappingUrls().forEach((key, value) -> {
            out.println("Url ->"+ key+" : value="+value.getClassName()+"/"+value.getMethod());
        });
        String uri = request.getRequestURI().trim();
        uri = uri.substring(request.getContextPath().length()).trim();
        out.println(uri);
        try {
            Mapping mapping = getMappingUrls().get(uri);
            Class<?> t = Class.forName(mapping.getClassName());
            Object object = t.newInstance();
            out.println("okok");

            //Maka anle parametre value anle requete
            Map<String, String[]> inputName = request.getParameterMap();

            Field[] fields = object.getClass().getDeclaredFields();
            Method[] methods = object.getClass().getDeclaredMethods();


            out.println(fields.length);
            for (Field field : fields) {
                String[] parameter = inputName.get(field.getName());
                if (parameter!=null) {
                    out.println(methods.length);
                    Method meth = stringMatching(methods, "set"+Util.toUpperFirstChar(field.getName()));
                    out.println("set"+Util.toUpperFirstChar(field.getName()));
                    out.println(meth);
                    Class<?>[] parameterType = parameterType(meth);
                    meth.invoke(object, dynamicCast(parameterType, parameter));
                }
            }

            out.println(object.getClass().getName());
            Method method = stringMatching(object.getClass().getDeclaredMethods(), mapping.getMethod());
            executeModelView(object, method, inputName);

            Object resp = method.invoke(object, (Object[]) null);
            if (resp instanceof ModelView){
                ModelView modelView = (ModelView) resp;
                if (modelView.getData()!=null){
                    out.println("mam1");
                    modelView.getData().forEach((key, value) -> {
                        request.setAttribute(key, value);
                    });
                    if (modelView.getView() != null) {
                        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/"+modelView.getView().trim());
                        requestDispatcher.forward(request, response);
                    }
                }
                out.println("mam2");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace(out);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException |
                 ServletException e) {
            e.printStackTrace(out);
        }catch (Exception e){
            e.printStackTrace(out);
        }
    }

    public Class<?>[] parameterType(Method method) throws Exception {
        Parameter[] parameterTypes = method.getParameters();
        Class<?>[] parameterClass = new Class[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            parameterClass[i] = parameterTypes[i].getType();
        }
        return parameterClass;
    }

    public Method stringMatching(Method[] methods, String methodName) {
        Method matching = null;
        for (Method method : methods) {

            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        return null;
    }

    public Object[] dynamicCast(Class<?>[] classes, Object[] args){
        Object[] array = new Object[classes.length];
        int i = 0;
        for (Class<?> classe : classes) {
            array[i] = classe.cast(args[i]);
            i++;
        }
        return array;
    }
    public void executeModelView(Object object, Method method, Map<String, String[]> inputName){
        Parameter[] parameters = method.getParameters();
        for (Parameter parameter : parameters) {
            if (parameter.isAnnotationPresent(RequestParameter.class)) {
                String[] params = inputName.get(parameter.getAnnotation(RequestParameter.class).name().trim());
                if (params != null) {
                    Class<?>[] parameterClass = parameterType(method);
                    method.invoke(object, dynamicCast(parameterClass, params));
                }
            }
        }
    }
}
