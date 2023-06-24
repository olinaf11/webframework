package etu2028.framework.servlet;

import etu2028.framework.ModelView;
import etu2028.framework.Utils.FileUpload;
import etu2028.framework.Utils.Util;
import etu2028.framework.annotation.Url;
import etu2028.framework.annotation.*;
import etu2028.framework.Mapping;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

@MultipartConfig
public class FrontServlet extends HttpServlet {

    private HashMap<String , Mapping> mappingUrls;
    private HashMap<String, Object> singleton;
    private String sessionName;
    private String sessionProfile;

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }
    public void setSessionProfile(String sessionProfile) {
        this.sessionProfile = sessionProfile;
    }
    public String getSessionName() {
        return sessionName;
    }
    public String getSessionProfile() {
        return sessionProfile;
    }

    public HashMap<String, Mapping> getMappingUrls() {
        return mappingUrls;
    }
    public HashMap<String, Object> getSingleton(){
        return singleton;
    }


    @Override
    public void init() {
        ArrayList<Class> classes = null;
        setMappingUrls(new HashMap<String,Mapping>());
        setSingleton(new HashMap<String, Object>());
        this.setSessionName(getInitParameter("sessionName"));
        this.setSessionProfile(getInitParameter("sessionProfil"));

        try {
            String packageName = getInitParameter("packages").trim();
            classes = new ArrayList<>(Util.searchClassBypackage(packageName));
            for (int i = 0; i < classes.size(); i++) {
                Method[] methods = classes.get(i).getMethods();
                if (classes.get(i).isAnnotationPresent(Scope.class)) {
                    if (((Scope) classes.get(i).getAnnotation(Scope.class)).value().trim().compareTo("singleton") == 0 ) {
                        singleton.put(classes.get(i).getName(), null);
                    }
                }
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
    public void setSingleton(HashMap<String, Object> singleton) {
        this.singleton = singleton;
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
            Object object = null;
            if (t.isAnnotationPresent(Scope.class)) {
                if (getSingleton().get(mapping.getClassName()) == null) {
                    getSingleton().put(mapping.getClassName(), t.newInstance());
                }
                object = getSingleton().get(mapping.getClassName());
                toDefault(object);
            }else{
                object = t.newInstance();
            }

            out.println("okok");

            //Maka anle parametre value anle requete
            Map<String, String[]> inputName = request.getParameterMap();
            out.println(inputName.size());

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
            HttpSession session = request.getSession();
            if (checkParams(method, valueParams, request)) {
                if (method.isAnnotationPresent(Session.class)) {
                    Field sessionField = object.getClass().getDeclaredField("session");
                    if (sessionField!=null) {
                        Method sessMethod = stringMatching(object.getClass().getDeclaredMethods(), "set"+Util.toUpperFirstChar(sessionField.getName()));
                        HashMap<String, Object> sessionMap = new HashMap<>();
                        ArrayList<String> listSession = Collections.list(session.getAttributeNames());
                        out.println(listSession.size()+" taille session");
                        for (String string : listSession) {

                            out.println(session.getAttribute(string)+"  et"+string);
                            sessionMap.put(string, session.getAttribute(string));
                        }
                        out.println(sessMethod.getName());

                        sessMethod.invoke(object, sessionMap);
                    }
                }
                Object resp = method.invoke(object, valueParams.toArray());
                castModelview(request, response, method, object, resp);
            }else{
                Field[] fields = object.getClass().getDeclaredFields();
                Method[] methods = object.getClass().getDeclaredMethods();

                out.println(fields.length);
                for (Field field : fields) {
                    String[] parameter = inputName.get(field.getName());
                    System.out.println(request.getParameter(field.getName()));
                    try {
                        if (field.getType() == FileUpload.class) {
                            Method meth = stringMatching(methods, "set"+Util.toUpperFirstChar(field.getName()));
                            meth.invoke(object, createFileUpload(field.getName(), request));
                        }
                        if (parameter!=null) {
                            out.println(methods.length);
                            Method meth = stringMatching(methods, "set"+Util.toUpperFirstChar(field.getName()));
                            out.println("set"+Util.toUpperFirstChar(field.getName()));
                            out.println(meth);
                            Class<?>[] parameterType = parameterType(meth);
                            meth.invoke(object, dynamicCast(parameterType, parameter));
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        if (parameter!=null) {
                            out.println(methods.length);
                            Method meth = stringMatching(methods, "set"+Util.toUpperFirstChar(field.getName()));
                            out.println("set"+Util.toUpperFirstChar(field.getName()));
                            out.println(meth);
                            Class<?>[] parameterType = parameterType(meth);
                            meth.invoke(object, dynamicCast(parameterType, parameter));
                        }
                    }
                }
                if (method.isAnnotationPresent(Session.class)) {
                    Field sessionField = object.getClass().getDeclaredField("session");
                    if (sessionField!=null) {
                        Method sessMethod = stringMatching(object.getClass().getDeclaredMethods(), "set"+Util.toUpperFirstChar(sessionField.getName()));
                        HashMap<String, Object> sessionMap = new HashMap<>();
                        ArrayList<String> listSession = Collections.list(session.getAttributeNames());
                        out.println(listSession.size()+" taille session");
                        for (String string : listSession) {

                            out.println(session.getAttribute(string)+"  et  " +string);
                            sessionMap.put(string, session.getAttribute(string));
                        }
                        out.println(sessMethod.getName());

                        sessMethod.invoke(object, sessionMap);
                    }
                }
                Object resp = method.invoke(object, (Object[])null);
                castModelview(request, response, method, object, resp);
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

    private void toDefault(Object object) throws Exception{
        Field[] fields = object.getClass().getDeclaredFields();
        Method[] methods = object.getClass().getDeclaredMethods();
        for (Field field : fields) {
            if (field.getName().compareTo("count")==0) {
                Method method = stringMatching(methods, "set"+Util.toUpperFirstChar(field.getName()));
                Method get = stringMatching(methods, "get"+Util.toUpperFirstChar(field.getName()));
                method.invoke(object, ((int)get.invoke(object))+1);
            }else{
                Method method = stringMatching(methods, "set"+Util.toUpperFirstChar(field.getName()));
                method.invoke(object, (Object)null);
            }
        }
    }

    public void checkMethod(Method method, ModelView modelView, HttpServletRequest request, HttpSession session, String sessionName, String sessionProfil) throws Exception {
        if (modelView.getSession()!=null) {
            if (modelView.getSession().get(sessionName)!=null && modelView.getSession().get(sessionProfil)!=null) {
                sessionAdd(session, sessionProfil, sessionName, modelView);
            }else{
                sessionAdd(session, sessionProfil, sessionName, modelView);
            }
        }else{
            throw new Exception("La session n'existe pas");
        }
    }
    public void sessionAdd(HttpSession session, String sessionProfil, String sessionName, ModelView modelView){
        if (session.getAttribute(sessionName) != null && session.getAttribute(sessionProfil)!=null) {
            modelView.getSession().put(sessionName, session.getAttribute(sessionName));
            modelView.getSession().put(sessionProfil, session.getAttribute(sessionProfil));
        }else{
            session.setAttribute(sessionName, modelView.getSession().get(sessionName));
            session.setAttribute(sessionProfil, modelView.getSession().get(sessionProfil));
        }
    }
    private void dispatcher(HttpServletRequest request, HttpServletResponse response, ModelView modelView) throws Exception{
        if (modelView.getData()!=null){
            modelView.getData().forEach((key, value) -> {
                request.setAttribute(key, value);
            });
            if (modelView.getView() != null) {
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("/"+modelView.getView().trim());
                requestDispatcher.forward(request, response);
            }
        }else{
            if (modelView.getView() != null) {
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("/"+modelView.getView().trim());
                requestDispatcher.forward(request, response);
            }
        }
    }
    private void castModelview(HttpServletRequest request, HttpServletResponse response, Method method, Object object, Object resp) throws Exception, IOException{
        if (resp instanceof ModelView){
            ModelView modelView = (ModelView) resp;
            if (method.isAnnotationPresent(Session.class)) {
                Field sessionField = object.getClass().getDeclaredField("session");
                if (sessionField!=null) {
                    Method sessMethod = stringMatching(object.getClass().getDeclaredMethods(), "get"+Util.toUpperFirstChar(sessionField.getName()));
                    Object sessMap = sessMethod.invoke(object);
                    if (sessMap instanceof HashMap) {
                        response.getWriter().println(sessMethod.getName()+"anarana");
                        HashMap<String, Object> map = (HashMap)sessMap;
                        PrintWriter out = response.getWriter();
                        map.forEach((key, value) -> {
                            out.println(key+"  "+value.getClass().getName());
                            request.getSession().setAttribute(key, value);
                        });
                    }
                }

            }
            if (method.isAnnotationPresent(Authentification.class)) {
                if (((Authentification)method.getAnnotation(Authentification.class)).user().trim().compareTo((String)request.getSession().getAttribute(getSessionProfile()))== 0) {
                    dispatcher(request, response, modelView);
                }
                else{
                    throw new Exception("Vous n'avez pas le droit d'acceder a cette page");
                }
            }else{
                checkMethod(method, modelView, request, request.getSession(), getSessionName(), getSessionProfile());
                dispatcher(request, response, modelView);
            }
        }else{
            throw new Exception("La method "+method.getName()+" doit avoir comme type de retour Model View");
        }
    }
}
