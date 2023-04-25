package model;

import etu2028.framework.ModelView;
import etu2028.framework.annotation.Url;

public class Test {
    String id;
    String nom;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    @Url(name = "test-show")
    public ModelView show(){
        ModelView modelview = new ModelView("employe.jsp");
        String[] list = new String[3];
        list[0] = "A";
        list[1] = "B";
        list[2] = "3";
        modelview.addItem("ls", list);
        return modelview;
    }
    @Url(name = "test-insert")
    public ModelView insert() {
        ModelView modelView = new ModelView("employe.jsp");
        System.out.println("Nom: "+this.getNom()+"  Id: "+this.getId());
        return modelView;
    }
}
