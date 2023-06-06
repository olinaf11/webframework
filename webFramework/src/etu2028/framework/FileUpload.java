package etu2028.framework.Utils;

public class FileUpload {
    String name;
    String path;    // tsy rarahina aloha
    byte[] content;

    public FileUpload(String name, byte[] contents) {
        this.name = name;
        this.content = contents;
    }

    public FileUpload() {
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setContent(byte[] content) {
        this.content = content;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public byte[] getContent() {
        return content;
    }
    public String getName() {
        return name;
    }
    public String getPath() {
        return path;
    }


}
