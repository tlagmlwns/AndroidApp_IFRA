package com.example.myapplication;

public class Test_Alarm {
    private String filename;
    private String filedate;
    public Test_Alarm(String filename, String filedate){
        this.filename = filename;
        this.filedate = filedate;
    }
    public String getFilename(){return filename;}
    public void setFilename(String filename) {this.filename = filename;}
    public String getFiledate() {return filedate;}
    public void setFiledate(String filedate) {this.filedate = filedate;}
}
