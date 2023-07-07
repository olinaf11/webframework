package model;

import etu2028.framework.ModelView;
import etu2028.framework.Utils.FileUpload;
import etu2028.framework.annotation.RequestParameter;
import etu2028.framework.annotation.Url;
import java.sql.Date;

public class Test {
    Integer id;
    String nom;
    FileUpload fileUpload;
    Date date;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public void setFileUpload(FileUpload fileUpload) {
        this.fileUpload = fileUpload;
    }

    public FileUpload getFileUpload() {
        return fileUpload;
    }
    public void setDate(java.sql.Date date) {
        this.date = date;
    }
    public Date getDate() {
        return date;
    }

    @Url(name = "test-show.do")
    public ModelView show(){
        ModelView modelview = new ModelView("employe.jsp");
        String[] list = new String[3];
        list[0] = "A";
        list[1] = "B";
        list[2] = "3";
        modelview.addItem("ls", list);
        return modelview;
    }
    @Url(name = "test-insert.do") // bla/insert?a=4&&b=5
    public ModelView insert(Integer id, String nom) {
        ModelView modelView = new ModelView("test.jsp");
        this.setId(id);
        setNom(nom);
        modelView.addItem("test", this);
        System.out.println(modelView.getData().get("test").getClass());
        System.out.println("Nom: "+nom+"  Id: "+id);
        return modelView;
    }
    @Url(name = "test-upload.do") // bla/insert?a=4&&b=5
    public ModelView upload() {
        ModelView modelView = new ModelView("test.jsp");
        modelView.addItem("test", this);
        System.out.println(modelView.getData().get("test").getClass());
        System.out.println("Nom: "+nom+"  Id: "+id+" File: "+fileUpload.getName());
        return modelView;
    }
}
