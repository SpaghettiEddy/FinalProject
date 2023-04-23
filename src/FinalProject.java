/*
    COP3330 Final Project
    Ashley Fram
    Eduardo Vila
    John LASTNAME
 */

import java.io.BufferedWriter;
//import java.util.Arrays;
//import java.util.List;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Stream;
import java.util.Random;

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
            return getCrn() + "," + prefix + "," + title + "," + gradLevel + "," + modality;
        else {
            String hasLabString = hasLab ? "Yes" : "No"; // check value of hasLab
            return getCrn() + "," + prefix + "," + title + "," + gradLevel + "," + modality + "," + getLocation() + "," + hasLabString;
        }
    }
}


class Lab extends Course {
    private String TAId;

    public String getTAId() {return TAId;}
    public void setTAId(String TAId) {this.TAId = TAId;}

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


class Student extends Person {
    private String advisor;
    private String degree;
    private String studentType;
    private boolean isTA;
    private ArrayList<Course> coursesTaking = new ArrayList<>();
    private ArrayList<Lab> labsAssisting = new ArrayList<>();

    public String getAdvisor() {return advisor;}
    public void setAdvisor(String advisor) {this.advisor = advisor;}
    public String getDegree() {return degree;}
    public void setDegree(String degree) {this.degree = degree;}
    public String getStudentType() {return studentType;}
    public void setStudentType(String studentType) {this.studentType = studentType;}
    public boolean isTA() {return isTA;}
    public void setTA(boolean TA) {isTA = TA;}
    public ArrayList<Course> getCoursesTaking() {return coursesTaking;}
    public void setCoursesTaking(ArrayList<Course> coursesTaking) {this.coursesTaking = coursesTaking;}
    public ArrayList<Lab> getLabsAssisting() {return labsAssisting;}
    public void setLabsAssisting(ArrayList<Lab> labsAssisting) {this.labsAssisting = labsAssisting;}

    public void addTAData(String advisor, String degree, Lab labAssisting) {
        setAdvisor(advisor);
        setDegree(degree);
        labsAssisting.add(labAssisting);
        setTA(true);
    }

    public void addTALab(Lab labAssisting) {
        labsAssisting.add(labAssisting);
    }

    public void addCourse(Course course) {
        coursesTaking.add(course);
    }

    public Student(String id, String name) {
        setId(id);
        setName(name);
        setTA(false);
    }
}


class IdException extends Exception {
    public IdException() {
        super("Sorry, incorrect format. IDs must be 7 digits.");
    }

    public IdException(String message) {
        super(message);
    }
}


public class FinalProject {
    private static String menu() {
        Scanner myScan = new Scanner(System.in);
        String option;
        System.out.println("\n*****************************************");
        System.out.println("Choose one of these options:");
        System.out.println("\t1 - Add a new Faculty to the schedule");
        System.out.println("\t2 - Enroll a Student to a lecture");
        System.out.println("\t3 - Print the schedule of a Faculty");
        System.out.println("\t4 - Print the schedule of an TA");
        System.out.println("\t5 - Print the schedule of a Student");
        System.out.println("\t6 - Delete a scheduled lecture");
        System.out.println("\t7 - Exit Program");
        System.out.print("\t\t\tEnter your choice: ");
        option = myScan.nextLine();
        return option;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        Scanner fileScan;

        // // In method 7, user is prompted to print lec.txt ONLY IF deleteLecture() is run, else "Bye!"
        boolean lectureDeleted = false;

        String line;
        String arr[];


        ArrayList<Course> courseList = new ArrayList<>();
        ArrayList<Person> people = new ArrayList<>();

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
                    enrollStudent(scanner, courseList, people);
                    break;
                case "3":
                    printFacultySchedule(scanner, courseList, people);
                    break;
                case "4":
                    printTASchedule(scanner, courseList, people);
                    break;
                case "5":
                    printStudentSchedule(scanner, courseList, people);
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

        String tempId, tempName = null, tempRank = null, tempOffice = null;
        int numCourses;
        int lecturesTaught[], tempLab[];
        tempLab = new int[3];

        while (true) {
            try {
                System.out.print("Enter UCF id: ");
                tempId = scanner.next();

                if (tempId.length() != 7 || !tempId.matches("\\d+")) {
                    throw new IdException();
                } else {
                    break;
                }
            } catch (IdException e) {
                System.out.println(e.getMessage());
            }
        }
        scanner.nextLine();

// Check if the UCF ID already exists
        boolean facultyExists = false;
        for (Person person : people) {
            if (person.getId().equals(tempId)) {
                System.out.println("Faculty with this UCF ID already exists. Skipping name, rank, and office input.");
                facultyExists = true;
                break;
            }
        }



        // //   Check if the UCF ID already exists
        // for (Person person : people) {
        //     if (person.getId().equals(tempId)) {
        //         System.out.println("Faculty with this UCF ID already exists. Skipping name, rank, and office input.");
        //         return;
        //     }
        // }


        if (!facultyExists) {

            System.out.print("Enter name: ");
            tempName = scanner.nextLine();
            while (true) {
                System.out.print("Enter rank: ");
                tempRank = scanner.next();
                if (!tempRank.equalsIgnoreCase("Professor")) {
                    System.out.println("Invalid rank. Please enter 'Professor'.");
                } else {
                    break;
                }
            }
            scanner.nextLine();

            System.out.print("Enter office location: ");
            tempOffice = scanner.next();

        }

        System.out.print("Enter how many lectures: ");
        numCourses = scanner.nextInt();


        lecturesTaught = new int[numCourses];
        System.out.print("Enter the CRNs of the lectures: ");
        for (int i = 0; i < numCourses; i++) {
            int crn = scanner.nextInt();
            boolean invalidCRN = false;

            // Check if the CRN is already assigned to a faculty
            for (Person person : people) {
                if (person instanceof Faculty) {
                    Faculty faculty = (Faculty) person;
                    for (int j = 0; j < faculty.getLecturesTaught().length; j++) {
                        if (crn == faculty.getLecturesTaught()[j]) {
                            System.out.println("CRN " + crn + " is already assigned to " + faculty.getName() + ". Please choose another CRN.");
                            invalidCRN = true;
                            break;
                        }
                    }
                    if (invalidCRN) {
                        break;
                    }
                }
            }

            if (invalidCRN) {
                i--;
                continue;
            }

            lecturesTaught[i] = crn;
        }

        temp = new Faculty(tempId, tempName, tempRank, tempOffice, lecturesTaught);
        people.add(temp);

        // lecturesTaught = new int[numCourses];
        // System.out.print("Enter the crns of the lectures: ");
        // for (int i = 0; i < numCourses; i++)
        //     lecturesTaught[i] = scanner.nextInt();


        // temp = new Faculty(tempId, tempName, tempRank, tempOffice, lecturesTaught);
        // people.add(temp);

        for (int i = 0; i < numCourses; i++) {
            for (int j = 0; j < courseList.size(); j++) {
                if (courseList.get(j).getCrn() == lecturesTaught[i]) {
                    if (((Lecture) courseList.get(j)).getModality().equalsIgnoreCase("Online")) //
                        System.out.println("[" + courseList.get(j).getCrn() + "/" + ((Lecture) courseList.get(j)).getPrefix() +
                                "/" + ((Lecture) courseList.get(j)).getTitle() + "]" + " Added!");
                    else if (((Lecture) courseList.get(j)).isHasLab()) {
                        System.out.println("[" + courseList.get(j).getCrn() + "/" + ((Lecture) courseList.get(j)).getPrefix() +
                                "/" + ((Lecture) courseList.get(j)).getTitle() + "]" + " has these labs:");
                        for (int k = 0; k < 3; k++) {
                            System.out.println("\t\t\t" + courseList.get(j + k + 1).getCrn() + "," + courseList.get(j + k + 1).getLocation());
                            tempLab[k] = courseList.get(j + k + 1).getCrn();
                        }
                        for (int k = 0; k < 3; k++) {
                            String tempTAId;
                            while (true) {
                                try {
                                    System.out.print("\nEnter the TA's id for " + tempLab[k] + ": ");
                                    tempTAId = scanner.next();

                                    if (tempTAId.length() != 7 || !tempTAId.matches("\\d+")) {
                                        throw new IdException();
                                    } else {
                                        break;
                                    }
                                } catch (IdException e) {
                                    System.out.println(e.getMessage());
                                }
                            }
                            scanner.nextLine();

                            boolean foundMatch = false; // add a flag to keep track if a match was found

                            for (Person person: people) {
                                if (person.getId() != null && person.getId().equals(tempTAId)) {
                                    ((Student) person).addTALab((Lab) courseList.get(j + k + 1));
                                    foundMatch = true; // set flag to true if a match is found
                                }
                            }
                            if (!foundMatch) {



                                // Remember to check later if ta exists. For now ta does not
                                System.out.print("Name of TA: ");
                                String tempTAName = scanner.nextLine();
                                System.out.print("TA’s supervisor’s name: ");
                                String tempAdvisor = scanner.nextLine();
                                System.out.print("Degree Seeking: ");
                                String tempDegree = scanner.next();
                                Student tempStudent = new Student(tempTAId, tempTAName);
                                tempStudent.addTAData(tempAdvisor, tempDegree, (Lab) courseList.get(j + k + 1));
                                people.add(tempStudent);
                            }
                        }
                    }
                }
            }
        }
    }





    // for (Course course: courseList)
    //     if (course.getCrn() == tempCrn) {
    //         tempLecture = (Lecture) course;
    //         if (tempStudent.getCoursesTaking().contains(course)) {
    //             System.out.println(tempStudent.getName() + " is already taking " + "[" + tempLecture.getPrefix() + "/" + tempLecture.getTitle() + "]");
    //             break;
    //         }




    // Option 2 - Enroll a Student to a Lecture
    public static void enrollStudent(Scanner scanner, ArrayList<Course> courseList, ArrayList<Person> people) {
        String tempId;
        int tempCrn;
        Student tempStudent = null;
        Lecture tempLecture = null;

        while (true)
            try {
                System.out.print("Enter UCF id: ");
                tempId = scanner.next();
                if (tempId.length() != 7 || !tempId.matches("\\d+"))
                    throw new IdException();
                else
                    break;
            } catch (IdException e) {
                System.out.println(e.getMessage());
            }
        scanner.nextLine();

        boolean foundMatch = false; // add a flag to keep track if a match was found

        for (Person person: people) {
            // if (person.getTAId().equals(tempStuId)) {
            if (person.getId() != null && person.getId().equals(tempId)) {  //If person is already in ArrayList people
                System.out.println("Record found/Name: " + person.getName());
                tempStudent = (Student) person;
                foundMatch = true; // set flag to true if a match is found
            }
        }

        if (!foundMatch) {
            System.out.print("Name of Student: ");
            String tempName = scanner.nextLine();
            tempStudent = new Student(tempId, tempName);
            people.add(tempStudent);
        }
        System.out.print("Which lecture to enroll [" + tempStudent.getName() + "] in? ");
        tempCrn = scanner.nextInt();        //Scan in the crn of the lecture to be enrolled in

        for (Course course: courseList)     //Search courselist for a corresponding Lecture
            if (course.getCrn() == tempCrn) {
                tempLecture = (Lecture) course;         //Assign it to tempLecture to be able to work with it more easily
                if (tempStudent.getCoursesTaking().contains(course)) {      //If they're already taking the lecture
                    System.out.println(tempStudent.getName() + " is already taking " + "[" + tempLecture.getPrefix() + "/" + tempLecture.getTitle() + "]");
                    break;
                }


                boolean canEnroll = true;
                for (Course enrolledCourse : tempStudent.getCoursesTaking()) {
                    if (enrolledCourse instanceof Lecture && ((Lecture) enrolledCourse).getPrefix().equals(tempLecture.getPrefix())) {
                        System.out.println(tempStudent.getName() + " is already enrolled in a lecture with prefix " + tempLecture.getPrefix());
                        canEnroll = false;
                        break;
                    }
                }
                if (!canEnroll) {
                    break;
                }

                tempStudent.addCourse(course);

                if (tempLecture.isHasLab()) {
                    Random random = new Random();
                    int randomLab = random.nextInt(3) + 1;
                    System.out.println("[" + tempLecture.getPrefix() + "/" + tempLecture.getTitle() + "]" + " has these labs:");
                    for (int k = 1; k <= 3; k++)
                        System.out.println("\t" + courseList.get(courseList.indexOf(tempLecture) + k));

                    Lab labToAdd = (Lab) courseList.get(courseList.indexOf(tempLecture) + randomLab);
                    tempStudent.addCourse(labToAdd);
                    System.out.println("[" + tempStudent.getName() + "] is added to lab: " + labToAdd.getCrn());

                }
                System.out.println("Student enrolled!");
                scanner.nextLine();

            }


        //
        System.out.println("\n\njust to test\n\n");
        for (Course course: tempStudent.getCoursesTaking())
            System.out.println(course);
        //
    }



    // Option 3 - Print the schedule of a Faculty
    private static void printFacultySchedule(Scanner scanner, ArrayList<Course> courseList, ArrayList<Person> people) {
        String tempId;

        while (true) {
            try {
                System.out.print("Enter UCF id: ");
                tempId = scanner.next();

                if (tempId.length() != 7 || !tempId.matches("\\d+")) {
                    throw new IdException();
                } else {
                    break;
                }
            } catch (IdException e) {
                System.out.println(e.getMessage());
            }
        }
        scanner.nextLine();
        Faculty professor = null;

        boolean facultyFound = false;

        for (Person person : people)
            if (person.getId().equals(tempId)) {
                professor = (Faculty) person;
                facultyFound = true;
                System.out.println(person.getName() + " is teaching the following lectures:");
            }

        if (!facultyFound)
            System.out.println("No Faculty with this id.");

        for (int i = 0; i < professor.getLecturesTaught().length; i++) {
            for (int j = 0; j < courseList.size(); j++) {
                if (professor.getLecturesTaught()[i] == courseList.get(j).getCrn()){
                    if (((Lecture) courseList.get(j)).isHasLab()) {
                        System.out.println("\t[" + courseList.get(j).getCrn() + "/" + ((Lecture) courseList.get(j)).getPrefix() + "/" + ((Lecture) courseList.get(j)).getTitle() + "] with Labs:" );
                        for (int k = 1; k <= 3; k++)
                            System.out.println("\t\t[" + courseList.get(j + k).getCrn() + "/" + courseList.get(j + k).getLocation() + "]");
                    } else
                        System.out.println("\t[" + ((Lecture) courseList.get(j)).getPrefix() + "/" + ((Lecture) courseList.get(j)).getTitle() + "] [" + ((Lecture) courseList.get(j)).getModality() + "]");
                }
            }
        }
    }

    // Option 4 - Print the schedule of a TA
    private static void printTASchedule(Scanner scanner, ArrayList<Course> courseList, ArrayList<Person> people) {


        String tempTAId;

        while (true) {
            try {
                System.out.print("Enter UCF id: ");
                tempTAId = scanner.next();

                if (tempTAId.length() != 7 || !tempTAId.matches("\\d+")) {
                    throw new IdException();
                } else {
                    break;
                }
            } catch (IdException e) {
                System.out.println(e.getMessage());
            }
        }

        scanner.nextLine();

        boolean foundMatch = false; // add a flag to keep track if a match was found




        for (Person person : people) {
            if (person.getId() != null && person.getId().equals(tempTAId)) {
                ArrayList<Lab> TALabs = ((Student) person).getLabsAssisting();
                boolean foundLab = false; // add a flag to keep track if a lab is found
                for (Lab lab: TALabs) {
                    boolean labFound = false; // add a flag to keep track if the lab is found
                    for (Course course: courseList) {
                        if (lab.getCrn() == course.getCrn()) {
                            if (!foundLab) {
                                System.out.println(person.getName() + " is assisting the following lectures: \n");
                                foundLab = true;
                            }
                            System.out.println(lab);
                            labFound = true; // set flag to true if the lab is found
                        }
                    }

                }
                if (!foundLab) {
                    System.out.println("No labs are currently assigned to this TA.");
                }
                foundMatch = true;
            }
        }

        if (!foundMatch) {
            System.out.println("No TA with this id.");
        }

    }




    // Option 5 - Print the schedule of a Student
    private static void printStudentSchedule(Scanner scanner, ArrayList<Course> courseList, ArrayList<Person> people) {
        String tempStuId;
        while (true) {
            try {
                System.out.print("Enter UCF id: ");
                tempStuId = scanner.next();

                if (tempStuId.length() != 7 || !tempStuId.matches("\\d+")) {
                    throw new IdException();
                } else {
                    break;
                }
            } catch (IdException e) {
                System.out.println(e.getMessage());
            }
        }

        scanner.nextLine();
        boolean foundMatch = false; // add a flag to keep track if a match was found

        for (Person person : people) {
            if (person.getId() != null && person.getId().equals(tempStuId)) {
                System.out.println("Record found: \n" + person.getName() + "\n");
                System.out.println("\tEnrolled in the following lectures: \n");
                ArrayList<Course> studentsCourses = ((Student) person).getCoursesTaking();
                for (int i = 0; i < studentsCourses.size(); i++) {
                    if (studentsCourses.get(i) instanceof Lecture) { //Is the current course a Lecture!
                        if (((Lecture) studentsCourses.get(i)).isHasLab()) { //If this lecture HAS labs
                            //
                        } else {    //If the lecture does NOT have labs

                        }
                    }
                }
                foundMatch = true; // set flag to true if a match is found
            }
        }

        // Check the flag and print error message if no matching UCF ID is found
        if (!foundMatch)
            System.out.println("No Student with this id.");
    }

    // Option 6 - Delete a Lecture
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

        if (lectureDeleted) {
            System.out.println("You have made a deletion of at least one lecture. Would you like to print the copy of lec.txt?");
            System.out.print("Enter y/Y for Yes or n/N for No: ");

            while (true) {
                String response = scanner.next().trim().toLowerCase();
                if (response.equals("y")) {
                    System.out.println("Printing copy of lec.txt...");


                    for (Course course : courseList)
                        System.out.println(course);
                    //break;


                    try {
                        BufferedWriter writer = new BufferedWriter(new FileWriter("lec.txt"));
                        for (Course course : courseList) {
                            writer.write(course.toString());
                            writer.newLine();
                        }
                        writer.close();
                        System.out.println("lec.txt has been updated.");
                    } catch (IOException e) {
                        System.out.println("Error updating lec.txt: " + e.getMessage());
                    }

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
}