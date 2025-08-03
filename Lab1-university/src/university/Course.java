package university;

public class Course {

	private final static int MAX_STUDENT_FOR_COURSE = 100;
	private final static int MAX_STUDENT_FOR_EXAM = 100;
    
    private String title;
    private String teacher;
    private int code;

    
	private Student[] enrolled = new Student[MAX_STUDENT_FOR_COURSE];
	private int indexStudentForCourse = 0;
	
	
	private Exam[] exams = new Exam[MAX_STUDENT_FOR_EXAM];
	private int indexExam = 0;

    public Course(String title,String teacher,int code) {
		this.title = title;
		this.teacher = teacher;
		this.code = code;
	}

	public void enrollStudent(Student student) {
		enrolled[indexStudentForCourse++] = student;
	}

	public String getEnrolledStudents(){
		String result = "";

		for(int i=0; i<indexStudentForCourse; i++ ){
			Student s = enrolled[i];
			result += s.toString() + "\n";
		}
		return result;
	}

	public void registerExam(Student student, Course course, int grade){
		exams[indexExam++] = new Exam(student, course, grade);
	}

	public String getAvg() {
		if(indexExam == 0) return "No student has taken the exam in " + title;
		float sum = (float) 0.0;
		for(int i=0;i<indexExam;i++) {
			sum = sum + exams[i].getGrade();
		}
		float avg = (float) sum/indexExam;
		return "The average for the course " + title + " is: " + avg;
	}

	@Override
    public String toString() {
		return code + "," + title + ","+teacher;
	}
}
