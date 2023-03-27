package Model;

import etu2028.framework.ModelView;
import etu2028.framework.annotation.Url;

public class Test {
    @Url(name = "/test-insert")
    public ModelView insert(){
        return new ModelView("employe.jsp");
    }
}
