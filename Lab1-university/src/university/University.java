package university;
import java.util.logging.Logger;

/**
 * This class represents a university education system.
 * 
 * It manages students and courses.
 *
 */
public class University {

	private static final int MAX_STUDENTS = 1000;
	private static final int START_ID_STUDENT = 10000;
	private static final int MAX_COURSES = 50;
	private static final int START_ID_COURSE = 10;
	
	private String name;
	private String rector;

	private int nextStudentId = START_ID_STUDENT;
	private Student[] students = new Student[MAX_STUDENTS];

	private int nextCourseCode = START_ID_COURSE;
	private Course[] courses = new Course[MAX_COURSES];

// 
	/**
	 * Constructor
	 * @param name name of the universitys
	 */
	public University(String name){
		// Example of logging
		// logger.info("Creating extended university object");
		//
		this.name = name;
	}
	
	/**
	 * Getter for the name of the university
	 * 
	 * @return name of university
	 */
	public String getName(){
		//
		return name;
	}
	
	/**
	 * Defines the rector for the university
	 * 
	 * @param first first name of the rector
	 * @param last	last name of the rector
	 */
	public void setRector(String first, String last){
		//
		this.rector = first + " " + last;
	}
	
	/**
	 * Retrieves the rector of the university with the format "First Last"
	 * 
	 * @return name of the rector
	 */
	public String getRector(){
		return rector;
    }
	
	
// R2
	/**
	 * Enrol a student in the university
	 * The university assigns ID numbers 
	 * progressively from number 10000.
	 * 
	 * @param first first name of the student
	 * @param last last name of the student
	 * 
	 * @return unique ID of the newly enrolled student
	 */
	public int enroll(String first, String last){
		//
		students[nextStudentId-START_ID_STUDENT] = new Student(first, last, nextStudentId);
		logger.info("New student enrolled: "+nextStudentId+", "+first+" "+last);
		return nextStudentId++;
	}
	
	/**
	 * Retrieves the information for a given student.
	 * The university assigns IDs progressively starting from 10000
	 * 
	 * @param id the ID of the student
	 * 
	 * @return information about the student
	 */
	public String student(int id){
		// 
		return students[id-START_ID_STUDENT].toString();
	}
	
// R3
	/**
	 * Activates a new course with the given teacher
	 * Course codes are assigned progressively starting from 10.
	 * 
	 * @param title title of the course
	 * @param teacher name of the teacher
	 * 
	 * @return the unique code assigned to the course
	 */
	public int activate(String title, String teacher){
		//
		courses[nextCourseCode - START_ID_COURSE] = new Course(title,teacher,nextCourseCode);
		logger.info("New course activated: "+nextCourseCode+", "+title + " " + teacher);
		return nextCourseCode++;
	}
	
	/**
	 * Retrieve the information for a given course.
	 * 
	 * The course information is formatted as a string containing 
	 * code, title, and teacher separated by commas, 
	 * e.g., {@code "10,Object Oriented Programming,James Gosling"}.
	 * 
	 * @param code unique code of the course
	 * 
	 * @return information about the course
	 */
	public String course(int code){
		//
		return courses[code - START_ID_COURSE].toString();
	}
	
// R4
	/**
	 * Register a student to attend a course
	 * @param studentID id of the student
	 * @param courseCode id of the course
	 */
	public void register(int studentID, int courseCode){
		//
		students[studentID - START_ID_STUDENT].addStudyplan(courses[courseCode - START_ID_COURSE]);
		courses[courseCode-START_ID_COURSE].enrollStudent(students[studentID - START_ID_STUDENT]);
		logger.info("Student "+studentID+ " signed up for course "+courseCode);

	}
	
	/**
	 * Retrieve a list of attendees.
	 * 
	 * The students appear one per row (rows end with `'\n'`) 
	 * and each row is formatted as describe in in method {@link #student}
	 * 
	 * @param courseCode unique id of the course
	 * @return list of attendees separated by "\n"
	 */
	public String listAttendees(int courseCode){
		//
		return courses[courseCode - START_ID_COURSE].getEnrolledStudents();
	}

	/**
	 * Retrieves the study plan for a student.
	 * 
	 * The study plan is reported as a string having
	 * one course per line (i.e. separated by '\n').
	 * The courses are formatted as describe in method {@link #course}
	 * 
	 * @param studentID id of the student
	 * 
	 * @return the list of courses the student is registered for
	 */
	public String studyPlan(int studentID){
		//
		return students[studentID - START_ID_STUDENT].getStudyPlan();
	}

// R5
	/**
	 * records the grade (integer 0-30) for an exam can 
	 * 
	 * @param studentId the ID of the student
	 * @param courseID	course code 
	 * @param grade		grade ( 0-30)
	 */
	public void exam(int studentId, int courseID, int grade) {
		students[studentId - START_ID_STUDENT].registerExam(students[studentId - START_ID_STUDENT], courses[courseID - START_ID_COURSE], grade);
		courses[courseID - START_ID_COURSE].registerExam(students[studentId - START_ID_STUDENT], courses[courseID - START_ID_COURSE], grade);
		logger.info("Student "+studentId+" took an exam in course "+courseID+" with grade "+grade);
	}

	/**
	 * Computes the average grade for a student and formats it as a string
	 * using the following format 
	 * 
	 * {@code "Student STUDENT_ID : AVG_GRADE"}. 
	 * 
	 * If the student has no exam recorded the method
	 * returns {@code "Student STUDENT_ID hasn't taken any exams"}.
	 * 
	 * @param studentId the ID of the student
	 * @return the average grade formatted as a string.
	 */
	public String studentAvg(int studentId) {
		return students[studentId - START_ID_STUDENT].getAvg();
	}
	
	/**
	 * Computes the average grades of all students that took the exam for a given course.
	 * 
	 * The format is the following: 
	 * {@code "The average for the course COURSE_TITLE is: COURSE_AVG"}.
	 * 
	 * If no student took the exam for that course it returns {@code "No student has taken the exam in COURSE_TITLE"}.
	 * 
	 * @param courseId	course code 
	 * @return the course average formatted as a string
	 */
	public String courseAvg(int courseId) {
		return courses[courseId - START_ID_COURSE].getAvg();
	}
	

// R6
	/**
	 * Retrieve information for the best students to award a price.
	 * 
	 * The students' score is evaluated as the average grade of the exams they've taken. 
	 * To take into account the number of exams taken and not only the grades, 
	 * a special bonus is assigned on top of the average grade: 
	 * the number of taken exams divided by the number of courses the student is enrolled to, multiplied by 10.
	 * The bonus is added to the exam average to compute the student score.
	 * 
	 * The method returns a string with the information about the three students with the highest score. 
	 * The students appear one per row (rows are terminated by a new-line character {@code '\n'}) 
	 * and each one of them is formatted as: {@code "STUDENT_FIRSTNAME STUDENT_LASTNAME : SCORE"}.
	 * 
	 * @return info on the best three students.
	 */
	public String topThreeStudents() {
		// Array per i migliori 3 punteggi
		float[] topScores = {0f, 0f, 0f};
		// Array per gli indici degli studenti corrispondenti
		int[] topIndexes = {-1, -1, -1};
	
		for (int i = 0; i < (nextStudentId - START_ID_STUDENT); i++) {
			float currentScore = students[i].getScore();
	
			if (currentScore > topScores[0]) {
				// Scorri tutti in basso
				topScores[2] = topScores[1];
				topIndexes[2] = topIndexes[1];
	
				topScores[1] = topScores[0];
				topIndexes[1] = topIndexes[0];
	
				topScores[0] = currentScore;
				topIndexes[0] = i;
			} else if (currentScore > topScores[1]) {
				topScores[2] = topScores[1];
				topIndexes[2] = topIndexes[1];
	
				topScores[1] = currentScore;
				topIndexes[1] = i;
			} else if (currentScore > topScores[2]) {
				topScores[2] = currentScore;
				topIndexes[2] = i;
			}
		}
	
		String res = "";
		for (int j = 0; j < 3; j++) {
			if (topIndexes[j] != -1) {
				res += students[topIndexes[j]].printScore();
			}
		}
	
		return res;
	}

// R7
    /**
     * This field points to the logger for the class that can be used
     * throughout the methods to log the activities.
     */
    public static final Logger logger = Logger.getLogger("University");

}
