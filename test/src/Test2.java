package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import etu2028.framework.ModelView;
import etu2028.framework.annotation.*;

@Scope(value = "singleton")
public class Test2 {
    String ok;
    String admin;
    HashMap<String, Object> session;

    public HashMap<String, Object> getSession() {
        return session;
    }
    public void setSession(HashMap<String, Object> session) {
        this.session = session;
    }
    public String getOk() {
        return ok;
    }
    public void setOk(String ok) {
        this.ok = ok;
    }
    public String getAdmin() {
        return admin;
    }
    public void setAdmin(String admin) {
        this.admin = admin;
    }

    @Url(name = "test2-insert.do")
    public ModelView insert(){
        ModelView modelView = new ModelView("singleton.jsp");
        modelView.addItem("test", this);
        return modelView;
    }

    @Session
    @Url(name = "test2-login")
    public ModelView Login() {
        ModelView modelView = new ModelView("employe.jsp");
        modelView.addItem("test", this);
        setSession(new HashMap<>());
        getSession().put("testSession", this.getAdmin());
        modelView.addSessionItem("profil", this.getAdmin());
        return modelView;
    }

    @Session
    @Authentification(user = "admin.do")
    @Url(name = "test2-list")
    public ModelView list(){
        ModelView modelView = new ModelView("list.jsp");
        String[] list = new String[3];
        list[0] = "A";
        list[1] = "B";
        list[2] = "3";
        modelView.setJson(true);
        String s = (String)getSession().get("testSession");
        modelView.addItem("ls", list);
        modelView.addItem("session", s);
        return modelView;
    }

    @RestAPI
    @Url(name = "test2-json.do")
    public Test2 ok() {
        Test2 test2 = new Test2();
        test2.setAdmin("admin");
        test2.setOk("ok");
        return test2;
    }
    @Url(name = "test2-logout.do")
    public ModelView logout(){
        ModelView modelView = new ModelView("employe.jsp");
        modelView.setInvalidateSession(true);
        return modelView;
    }
    @Url(name = "test2-logout2.do")
    public ModelView logout2(){
        ModelView modelView = new ModelView("employe.jsp");
        List<String> sessionName = new ArrayList<>();
        sessionName.add("profil");
        modelView.setRemoveSession(sessionName);
        return modelView;
    }
}
