package etu2028.framework.servlet;

import etu2028.framework.ModelView;
import etu2028.framework.Utils.FileUpload;
import etu2028.framework.Utils.Util;
import etu2028.framework.annotation.Url;
import etu2028.framework.annotation.*;
import etu2028.framework.Mapping;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

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

    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
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

            Vector<Object> valueParams = new Vector<>();

            out.println(object.getClass().getName());
            Method method = stringMatching(object.getClass().getDeclaredMethods(), mapping.getMethod());
            out.println(method);

            Map<String, String[]> parameterMap = request.getParameterMap();
            out.println(parameterMap.size() + " parameters in the map");

            // Iterate over the parameter map to access each parameter
            for (String paramName : parameterMap.keySet()) {
                String[] paramValues = parameterMap.get(paramName);

                // Print parameter name and values
                out.println("Parameter: " + paramName);
                for (String paramValue : paramValues) {
                    out.println("Value: " + paramValue);
                }
            }

            if (checkParams(method, valueParams, request)) {
                System.out.println("content type :"+request.getContentType());
                out.println(valueParams.toArray()[0].getClass());
                Object resp = method.invoke(object, valueParams.toArray());
                if (resp instanceof ModelView){
                    ModelView modelView = (ModelView) resp;
                    if (modelView.getData()!=null){
                        out.println("mam3");
                        modelView.getData().forEach((key, value) -> {
                            System.out.println(key+" ,value"+value.getClass());
                            request.setAttribute(key, value);
                        });
                        if (modelView.getView() != null) {
                            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/"+modelView.getView().trim());
                            requestDispatcher.forward(request, response);
                        }
                    }
                    out.println("mam4");
                }
            }else{
                Field[] fields = object.getClass().getDeclaredFields();
                Method[] methods = object.getClass().getDeclaredMethods();

                out.println(fields.length);
                for (Field field : fields) {
                    String[] parameter = inputName.get(field.getName());
                    if (parameter != null && field.getType() == FileUpload.class) {
                        out.println(methods.length);
                        Method meth = stringMatching(methods, "set"+Util.toUpperFirstChar(field.getName()));
                        meth.invoke(object, createFileUpload(parameter[0], request));
                    }
                    if (parameter!=null) {
                        out.println(methods.length);
                        Method meth = stringMatching(methods, "set"+Util.toUpperFirstChar(field.getName()));
                        out.println("set"+Util.toUpperFirstChar(field.getName()));
                        out.println(meth);
                        Class<?>[] parameterType = parameterType(meth);
                        meth.invoke(object, dynamicCast(parameterType, parameter));
                    }
                }

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

    public Object[] dynamicCast(Class<?>[] classes, String[] args) throws Exception{
        Object[] array = new Object[classes.length];
        int i = 0;
        for (Class<?> classe : classes) {
            array[i] = castSimple(classe, args[i]);
            i++;
        }
        return array;
    }

    public boolean checkParams(Method method,Vector<Object> value , HttpServletRequest request) throws Exception,ServletException{
        Parameter[] parameters = method.getParameters();
        int count = 0;
        for (Parameter parameter : parameters) {
            String params = request.getParameter(parameter.getName());
            System.out.println(request.getParameter(parameter.getName()) +" okok");
            if (params != null) {
                count++;
                value.add(castSimple(parameter.getType(), params));
            }
        }
        System.out.println(count);
        System.out.println(parameters.length);
        if (parameters.length == count && parameters.length>0) {
            return true;
        }
        return false;
    }

    public Object castSimple(Class<?> type, String params) throws Exception{
        return type.getDeclaredConstructor(String.class).newInstance(params);
    }

    // method to create a new instance of FileUpload
    private FileUpload createFileUpload(String params, HttpServletRequest request) throws ServletException, IOException {
        return new FileUpload(getFileName(request.getPart(params)), request.getPart(params).getInputStream().readAllBytes());
    }

    private String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        String[] tokens = contentDisposition.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf("=") + 2, token.length() - 1);
            }
        }
        return "";
    }
}
