/*
    COP3330 Final Project
    Eduardo Vila
 */

import java.util.Scanner;

abstract class Person {
    private String name;
    private String id;

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getId() {return id;}
    public void setId(String id) {this.id = id;}
}

class Faculty extends Person{
    private String rank;
    //NEED TO INCLUDE LIST OF LECTURES TAUGHT BY THIS FACULTY (ARRAY OR INTERFACE? IDK YET)

}

class TA extends Person {
    private String advisor;
    private String degree;
    //NEED TO INCLUDE LIST OF LABS SUPERVISED BY THIS TA (ARRAY OR INTERFACE? IDK YET)

}

class Student extends Person {
    private String studentType;
    //NEED TO INCLUDE POSSIBLE LIST OF LABS TAKEN BY THIS STUDENT (ARRAY OR INTERFACE? IDK YET)
}

public class FinalProject {
    public static void main(String[] args) {
        System.out.println("chicken butt");
    }
}