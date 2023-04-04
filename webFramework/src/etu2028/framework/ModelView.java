package etu2028.framework;

import java.util.HashMap;

public class ModelView {
    String view;
    HashMap<String, Object> data;

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

    public void addItem(String key, Object value){
        if (data == null){
            setData(new HashMap<String, Object>());
        }
        data.put(key, value);
    }

    public ModelView(){};
    public ModelView(String view){
        setView(view);
    }
}
