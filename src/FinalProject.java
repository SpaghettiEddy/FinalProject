/*
    COP3330 Final Project
    Ashley Fram
    Eduardo Vila
    John LASTNAME
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Stream;

abstract class Course {
    private int crn;
    private String location;

    public int getCrn() {return crn;}
    public void setCrn(int crn) {this.crn = crn;}
    public String getLocation() {return location;}
    public void setLocation(String location) {this.location = location;}

    @Override
    public String toString(){
        return crn + "," + location;
    }
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
    public String toString(){
        if (modality.equalsIgnoreCase("Online"))
            return getCrn() + "," + title + "," + gradLevel + "," + modality;
        else
            return getCrn() + "," + title + "," + gradLevel + "," + modality + "," + getLocation() + "," + hasLab;
    }
}

class Lab extends Course {
    public Lab(int crn, String location){
        setCrn(crn);
        setLocation(location);
    }
}

abstract class Person {
    private String name;
    private String id;

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    @Override
    public String toString() {
        return id + "," + name;
    }
}

class Faculty extends Person{
    private String rank;
    private String office;
    private int[] lecturesTaught;

    public String getRank() {return rank;}
    public void setRank(String rank) {this.rank = rank;}
    public String getOffice() {return office;}
    public void setOffice(String office) {this.office = office;}
    public int[] getLecturesTaught() {return lecturesTaught;}
    public void setLecturesTaught(int[] lecturesTaught) {this.lecturesTaught = lecturesTaught;}

    public Faculty(String id, String name, String rank, String office, int[] lecturesTaught) {
        setId(id);
        setName(name);
        setRank(rank);
        setOffice(office);
        setLecturesTaught(lecturesTaught);
    }

    @Override
    public String toString() {
        return super.toString() + "," + rank + "," + office + "," + lecturesTaught;
    }
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

        Scanner scanner = new Scanner(System.in);
        Scanner fileScan;

        String line;
        String arr[];

        boolean lectureDeleted = false;

        ArrayList<Course> courseList = new ArrayList<Course>();
        ArrayList<Person> people = new ArrayList<Person>();

        System.out.print("Enter the absolute path of the file: ");
        do {                                                // Run until file is successfully loaded
            String fileName = scanner.next();
            try {                                           // Attempts to take input from fileName
                fileScan = new Scanner(new File(fileName)); // If an exception is caught at this line...
                break;                                      // This line won't be reached (i.e. loop will never break)
            } catch (Exception e) {
                System.out.println("Sorry no such file.");  // On failed input, prompts for another input
                System.out.print("Try again: ");
            }
        } while (true);

        while (fileScan.hasNextLine()) {                     // Fill courseList with the courses in the lec.tct file
            line = fileScan.nextLine();
            arr = line.split(",");
            if (arr.length == 2) {  // If the array only has 2 elements, then it must be a lab
                Lab temp = new Lab(Integer.parseInt(arr[0]), arr[1]);
                courseList.add(temp);
            } else {                                  // If the array length is greater than 2, it must be a lecture
                if (arr[4].equalsIgnoreCase("Online")) {    // We call the constructor for an online lecture
                    //public Lecture(int crn, String prefix, String title, String gradLevel, String modality) {  // Online lecture
                    Lecture temp = new Lecture(Integer.parseInt(arr[0]), arr[1], arr[2], arr[3], arr[4]);
                    courseList.add(temp);
                } else {      // We call the constructor for a lecture that has a physical location
                    boolean hasLab = arr[6].equalsIgnoreCase("yes") ? true : false;
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
                    addFaculty(scanner, courseList, people);
                    break;
                case "2":
                    break;
                case "3":
                    printFacultySchedule(scanner, courseList, people);
                    break;
                case "4":
                    break;
                case "5":
                    break;
                case "6":
                    deleteLecture(scanner, courseList, people);
                    lectureDeleted = true;
                    break;
                case "7":
                    goodBye(scanner, courseList, lectureDeleted);
                    break;
                default:
                    System.out.println("Invalid selection...");
            }
            option = menu();
        }
    }


    // Option 1 - Add a new Faculty to the schedule
    public static void addFaculty(Scanner scanner, ArrayList<Course> courseList, ArrayList<Person> people) {
        Faculty temp;

        String tempId, tempName, tempRank, tempOffice;
        int numCourses;
        int lecturesTaught[], tempLab[];
        tempLab = new int[3];

        System.out.print("Enter UCF id: ");
        tempId = scanner.next();
        scanner.nextLine();
        System.out.print("Enter name: ");
        tempName = scanner.nextLine();
        System.out.print("Enter rank: ");
        tempRank = scanner.next();
        System.out.print("Enter office location: ");
        tempOffice = scanner.next();
        System.out.print("Enter how many lectures: ");
        numCourses = scanner.nextInt();

        lecturesTaught = new int[numCourses];
        System.out.print("Enter the crns of the lectures: ");
        for (int i = 0; i < numCourses; i++)
            lecturesTaught[i] = scanner.nextInt();

        temp = new Faculty(tempId, tempName, tempRank, tempOffice, lecturesTaught);
        people.add(temp);

//        for (int i = 0; i < numCourses; i++) {
//            for (int j = 0; j < courseList.size(); j++) {
//                if (courseList.get(j).getCrn() == lecturesTaught[i]) {
//                    if (((Lecture) courseList.get(j)).getModality().equalsIgnoreCase("Online")) //
//                        System.out.println("[" + courseList.get(j).getCrn() + "/" + ((Lecture) courseList.get(j)).getPrefix() +
//                                "/" + ((Lecture) courseList.get(j)).getTitle() + "]" + " Added!");
//                    else if (((Lecture) courseList.get(j)).isHasLab()) {
//                        System.out.println("[" + courseList.get(j).getCrn() + "/" + ((Lecture) courseList.get(j)).getPrefix() +
//                                "/" + ((Lecture) courseList.get(j)).getTitle() + "]" + " has these labs:");
//                        for (int k = 0; k < 3; k++) {
//                            System.out.println("\t\t\t" + courseList.get(j + k + 1).getCrn() + "," + courseList.get(j + k + 1).getLocation());
//                            tempLab[k] = courseList.get(j + k + 1).getCrn();
//                        }
//                        for (int k = 0; k < 3; k++) {
//                            System.out.print("\nEnter the TA's id for " + tempLab[k] + ": ");
//                            scanner.nextLine();
//                            String tempTAId = scanner.nextLine();
//                            // Remember to check later if ta exists. For now ta does not
//                            System.out.print("Name of TA: ");
//                            String tempTAName = scanner.nextLine();
//                            System.out.print("TA’s supervisor’s name: ");
//                            String tempSupervisor = scanner.nextLine();
//                            System.out.print("Degree Seeking: ");
//                            String tempDegree = scanner.next();
//                        }
//                    }
//                }
//            }
//        }

        for (Person person: people) {
            System.out.println(person);
        }
    }


    // Option 2 - Enroll a Student to a Lecture
    public static void enrollStudent(Scanner scanner, ArrayList<Course> courseList, ArrayList<Person> people) {

    }

    // Option 3 - Print the schedule of a Faculty
    private static void printFacultySchedule(Scanner scanner, ArrayList<Course> courseList, ArrayList<Person> people) {

        String tempId = getUCFId(scanner);
        Faculty professor = null;
        for (Person person : people) {
            if (person.getId().equals(tempId)) {
                professor = (Faculty) person;
                System.out.println("Kyle Johnson is teaching the following lectures:");
            } else
                System.out.println("No Faculty with this id.");
        }

        for (int i = 0; i < professor.getLecturesTaught().length; i++) {
            for (int j = 0; j < courseList.size(); j++) {
                if (professor.getLecturesTaught()[i] == courseList.get(j).getCrn()){
                    if (((Lecture) courseList.get(j)).isHasLab()) {
                        System.out.println("\t[" + courseList.get(j).getCrn() + "/" + ((Lecture) courseList.get(j)).getPrefix() + "/" + ((Lecture) courseList.get(j)).getTitle() + "] with Labs:" );
                        for (int k = 1; k <= 3; k++)
                            System.out.println("\t\t[" + courseList.get(j + k).getCrn() + "/" + courseList.get(j + k).getLocation() + "]");
                    }
                    //System.out.println(courseList.get());
                }
            }
        }
    }

    // Option 4 - Print the schedule of an TA
    private static void printTASchedule(Scanner scanner, ArrayList<Course> courseList, ArrayList<Person> people) {

    }

    // Option 5 - Print the schedule of a Student
    private static void printStudentSchedule(Scanner scanner, ArrayList<Course> courseList, ArrayList<Person> people) {

    }

    // Option 6
    private static void deleteLecture(Scanner scanner, ArrayList<Course> courseList, ArrayList<Person> people) {

        int crnToDelete;
        int courseIndex = 0;
        Lecture lectureToDelete = null;
        System.out.print("Enter the crn of the lecture to delete: ");
        crnToDelete = scanner.nextInt();

        for (Course course : courseList) {
            if (course.getCrn() == crnToDelete) {
                lectureToDelete = (Lecture) courseList.get(courseIndex);
                if (((Lecture) course).isHasLab())
                    for (int i = 0; i < 3; i++)
                        courseList.remove(courseIndex);
                courseList.remove(courseIndex);
                break;
            }
            courseIndex++;
        }
        if (lectureToDelete == null) {
            System.out.println("Lecture not found with CRN " + crnToDelete);
            return;
        }
        // courseList.remove(lectureToDelete);
        System.out.println("[" + crnToDelete + "/" + lectureToDelete.getPrefix() + "/" + lectureToDelete.getTitle() + "] Deleted");
    }

    // Option 7 - Exit
    private static void goodBye(Scanner scanner, ArrayList<Course> courseList, boolean lectureDeleted) {
        if(lectureDeleted){
            System.out.println("You have made a deletion of at least one lecture. Would you like to print the copy of lec.txt?");
            System.out.print("Enter y/Y for Yes or n/N for No: ");

            while (true) {
                String response = scanner.next().trim().toLowerCase();
                if (response.equals("y")) {
                    System.out.println("Printing copy of lec.txt...");
                    for (Course course : courseList)
                        System.out.println(course);
                    break;
                } else if (response.equals("n")) {
                    System.out.println("Okay, lec.txt will not be printed.");
                    break;
                } else
                    System.out.print("Is that a yes or no? Enter y/Y for Yes or n/N for No: ");
            }
        }
        System.out.println("Bye!");
        System.exit(0);
    }



    //NEEEEEEDS WORK
    private static String getUCFId(Scanner scanner) {
        String UCFId;
        while (true) {
            System.out.print("Enter UCF id: ");
            UCFId = scanner.next();
            if (UCFId.length() != 7 || !UCFId.matches("\\d+"))
                System.out.println("Invalid UCF ID. Please enter a 7-digit number.");
            else
                return UCFId;
        }
    }
}