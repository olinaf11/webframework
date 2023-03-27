package etu2028.framework;

public class ModelView {
    String view;

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }
    public ModelView(){};
    public ModelView(String view){
        setView(view);
    }
}
