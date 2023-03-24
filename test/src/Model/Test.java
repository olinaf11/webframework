package Model;

import etu2028.framework.annotation.Url;

public class Test {
    @Url(name = "test-insert")
    public String insert(){
        return "insert";
    }
}
