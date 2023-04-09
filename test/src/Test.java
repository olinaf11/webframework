package model;

import etu2028.framework.ModelView;
import etu2028.framework.annotation.Url;

public class Test {

    @Url(name = "test-insert")
    public ModelView insert(){
        ModelView modelview = new ModelView("employe.jsp");
        String[] list = new String[3];
        list[0] = "A";
        list[1] = "B";
        list[2] = "3";
        modelview.addItem("ls", list);
        return modelview;
    }
}
