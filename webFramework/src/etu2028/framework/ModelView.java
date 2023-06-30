package etu2028.framework;

import java.util.HashMap;
import java.util.List;

public class ModelView {
    private String view;
    private HashMap<String, Object> data;
    private HashMap<String, Object> session;
    private boolean isJson = false;
    private boolean invalidateSession = false;
    private List<String> removeSession;

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
    public void setJson(boolean isJson) {
        this.isJson = isJson;
    }
    public boolean isJson() {
        return isJson;
    }
    public void setInvalidateSession(boolean invalidateSession) {
        this.invalidateSession = invalidateSession;
    }
    public boolean isInvalidateSession() {
        return invalidateSession;
    }
    public void setRemoveSession(List<String> removeSession) {
        this.removeSession = removeSession;
    }
    public List<String> getRemoveSession() {
        return removeSession;
    }

    public ModelView(){};
    public ModelView(String view){
        setView(view);
    }
}
