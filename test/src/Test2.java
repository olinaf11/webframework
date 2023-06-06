package model;

import etu2028.framework.ModelView;
import etu2028.framework.annotation.*;

@Scope(value = "singleton")
public class Test2 {
    String ok;
    Integer count = Integer.valueOf(0);
    public Integer getCount() {
        return count;
    }
    public void setCount(Integer count) {
        this.count = count;
    }

    public String getOk() {
        return ok;
    }
    public void setOk(String ok) {
        this.ok = ok;
    }

    @Url(name = "test2-insert")
    public ModelView insert(){
        ModelView modelView = new ModelView("singleton.jsp");
        modelView.addItem("test", this);
        return modelView;
    }
}
