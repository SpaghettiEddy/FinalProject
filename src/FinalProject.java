/*
    COP3330 Final Project
    Eduardo Vila
 */

import java.io.File;
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
//
//    private static Scanner myScan = new Scanner(System.in);
//    private static String menu() {
//        String option;
//        System.out.println("Choose one of these options:");
//        System.out.println("1 - Add a new faculty to the schedule");
//        System.out.println("2 - Enroll a student to a lecture");
//        System.out.println("3 - Print the Schedule of a faculty");
//        System.out.println("4 - Print the schedule of an TA");
//        System.out.println("5 - Print the schedule of a student");
//        System.out.println("6 - Delete a scheduled lecture");
//        System.out.println("7 - Exit Program");
//        System.out.print("Enter your choice: ");
//        option = myScan.nextLine();
//        return option;
//    }

    public static void main(String[] args) {
        Scanner myScan = new Scanner(System.in);
        Scanner fileScan;

        System.out.print("Enter the absolute path of the file: ");
        do {                                                // Run until file is successfully loaded
            String fileName = myScan.next();
            try {                                           // Attempts to take input from fileName
                fileScan = new Scanner(new File(fileName)); // If an exception is caught at this line...
                break;                                      // This line won't be reached (i.e. loop will never break)
            } catch (Exception e) {
                System.out.println("Sorry no such file.");  // On failed input, prompts for another input
                System.out.print("Try again: ");
            }
        } while (true);
        System.out.println("File Found! Let’s proceed...");

        String firstLine = fileScan.nextLine();
        System.out.println(firstLine);                      // Just testing that it's reading the file right

    }
}