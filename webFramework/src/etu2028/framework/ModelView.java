package etu2028.framework;

import java.util.HashMap;

public class ModelView {
    String view;
    HashMap<String, Object> data;
    HashMap<String, Object> session;

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }

    public HashMap<String, Object> getData() {
        return data;
    }

    public void setSession(HashMap<String, Object> session) {
        this.session = session;
    }

    public HashMap<String, Object> getSession() {
        return session;
    }

    public void addItem(String key, Object value){
        if (data == null){
            setData(new HashMap<String, Object>());
        }
        data.put(key, value);
    }

    public void addSessionItem(String key, Object value){
        if (session == null){
            setSession(new HashMap<String, Object>());
        }
        session.put(key, value);
    }

    public ModelView(){};
    public ModelView(String view){
        setView(view);
    }
}
