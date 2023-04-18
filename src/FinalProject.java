/*
    COP3330 Final Project
    Eduardo Vila
 */

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

abstract class Course {
    private int crn;
    private String location;

    public int getCrn() {return crn;}
    public void setCrn(int crn) {this.crn = crn;}
    public String getLocation() {return location;}
    public void setLocation(String location) {this.location = location;}
}

class Lecture extends Course {
    private String prefix;
    private String title;
    private String gradLevel;
    private String modality;
    private boolean hasLab;

    public String getPrefix() {return prefix;}
    public void setPrefix(String prefix) {this.prefix = prefix;}
    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}
    public String getGradLevel() {return gradLevel;}
    public void setGradLevel(String gradLevel) {this.gradLevel = gradLevel;}
    public String getModality() {return modality;}
    public void setModality(String modality) {this.modality = modality;}
    public boolean isHasLab() {return hasLab;}
    public void setHasLab(boolean hasLab) {this.hasLab = hasLab;}

    public Lecture(int crn, String prefix, String title, String gradLevel, String modality) {  // Online lecture
        setCrn(crn);
        setPrefix(prefix);
        setTitle(title);
        setGradLevel(gradLevel);
        setModality(modality);
    }

    public Lecture(int crn, String prefix, String title, String gradLevel, String modality, String location, boolean hasLab) {
        setCrn(crn);
        setPrefix(prefix);
        setTitle(title);
        setGradLevel(gradLevel);
        setModality(modality);
        setLocation(location);
        setHasLab(hasLab);
    }

    @Override
    public String toString() {
        return '[' + prefix + '/' + title + '/' + gradLevel + '/' + modality + '/' + getLocation() + '/' + hasLab + "]\n";
    }
}

class Lab extends Course {
    public Lab(int crn, String location){
        setCrn(crn);
        setLocation(location);
    }

    @Override
    public String toString() {
        return getCrn() + ',' + getLocation() + "\n";
    }
}

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


}

class TA extends Person {
    private String advisor;
    private String degree;

}

class Student extends Person {
    private String studentType;

}

public class FinalProject {

    private static String menu() {
        Scanner myScan = new Scanner(System.in);
        String option;
        System.out.println("*****************************************");
        System.out.println("Choose one of these options:");
        System.out.println("\t1 - Add a new faculty to the schedule");
        System.out.println("\t2 - Enroll a student to a lecture");
        System.out.println("\t3 - Print the Schedule of a faculty");
        System.out.println("\t4 - Print the schedule of an TA");
        System.out.println("\t5 - Print the schedule of a student");
        System.out.println("\t6 - Delete a scheduled lecture");
        System.out.println("\t7 - Exit Program");
        System.out.print("\t\t\tEnter your choice: ");
        option = myScan.nextLine();
        return option;
    }

    public static void main(String[] args) {
        Scanner myScan = new Scanner(System.in);
        Scanner fileScan;

        String line;
        String arr[];

        ArrayList<Course> courseList = new ArrayList<Course>();

        ArrayList<Faculty> schedule = new ArrayList<Faculty>();

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

        while(fileScan.hasNextLine()) {                     // Fill courseList with the courses in the lec.tct file
            line = fileScan.nextLine();
            arr = line.split(",");
            if (arr.length == 2) {  // If the array only has 2 elements, then it must be a lab
                Lab temp = new Lab(Integer.parseInt(arr[0]), arr[1]);
                courseList.add(temp);
            }
            else {                                  // If the array length is greater than 2, it must be a lecture
                if (arr[4].equalsIgnoreCase("Online")) {    // We call the constructor for an online lecture
                    //public Lecture(int crn, String prefix, String title, String gradLevel, String modality) {  // Online lecture
                    Lecture temp = new Lecture(Integer.parseInt(arr[0]), arr[1], arr[2], arr[3], arr[4]);
                    courseList.add(temp);
                }
                else {      // We call the constructor for a lecture that has a physical location
                    boolean hasLab = arr[6].equalsIgnoreCase("yes")?true:false;
                    Lecture temp = new Lecture(Integer.parseInt(arr[0]), arr[1], arr[2], arr[3], arr[4], arr[5], hasLab);
                    courseList.add(temp);
                }
            }
        }

        System.out.println("File Found! Let’s proceed...");

        String option = menu();

        while (!option.equals("0")) {
            switch (option) {
                case "1":
                    Faculty temp = new Faculty();

                    String tempID, tempName, tempRank, tempOffice;
                    int numCourses;
                    int lecturesTaught[];

                    schedule.add(temp);
                    System.out.print("Enter UCF id: ");
                    tempID = myScan.next();
                    myScan.nextLine();
                    System.out.print("Enter name: ");
                    tempName = myScan.nextLine();
                    System.out.print("Enter rank: ");
                    tempRank = myScan.next();
                    System.out.print("Enter office location: ");
                    tempOffice = myScan.next();
                    System.out.print("Enter how many lectures: ");
                    numCourses = myScan.nextInt();

                    lecturesTaught = new int[numCourses];
                    System.out.print("Enter the crns of the lectures: ");
                    for (int i = 0; i < numCourses; i++)
                        lecturesTaught[i] = myScan.nextInt();

                    System.out.println("DONE ~ TESTING");

                    break;
                case "2":
                    break;
                case "3":
                    break;
                case "4":
                    break;
                case "5":
                    break;
                case "6":
                    break;
                case "7":
                    break;
                default:
                    System.out.println("Invalid selection...");
            }
            option = menu();
        }
    }
}