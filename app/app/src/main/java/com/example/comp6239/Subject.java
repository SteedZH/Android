package com.example.comp6239;

public class Subject {
    private int subject_id;
    private String name;

    public Subject(int subject_id, String name) {
        this.subject_id = subject_id;
        this.name = name;
    }

    public void setSubjectId(int subject_id) {  this.subject_id = subject_id;   }
    public void setName(String name) {          this.name = name;               }
    public int getSubjectId() {                 return this.subject_id;         }
    public String getName() {                   return this.name;               }

}
