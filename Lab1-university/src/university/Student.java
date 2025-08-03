package university;

public class Student {
    
    private static final int MAX_COURSE_FOR_STUDENT = 25;
    private static final int MAX_EXAMS = 25;

    private String first;
    private String last;
    private int id;

    private Course[] studyPlan = new Course[MAX_COURSE_FOR_STUDENT];
    private int indexCourse = 0;

    private Exam[] exams = new Exam[MAX_EXAMS];
    private int indexExam = 0; 



    public Student(String first, String last, int id){
        this.first = first;
        this.last = last;
        this.id = id;
    }

    public void addStudyplan(Course c){
        studyPlan[indexCourse++] = c;
    } 

    public String getStudyPlan(){
        StringBuffer bf = new StringBuffer();

        for(Course c : studyPlan){
            if(c!=null){
                bf.append(c.toString()).append('\n');
            }
        }
        return bf.toString().trim();
    }

    public void registerExam(Student student, Course course, int grade){
		exams[indexExam++] = new Exam(student, course, grade);
	}

    public String getAvg() {
        if(indexExam == 0) return "Student " + id + " hasn't taken any exams";
		float sum = (float) 0.0;
		for(int i=0; i<indexExam; i++) {
			sum = sum + exams[i].getGrade();
		}
		float avg = (float) sum/indexExam;
		return "Student " + id + " : " + avg;
	}

    public float getScore(){
        if(indexExam == 0) return -1;
        float sum = (float) 0.0;
        for(int i=0; i<indexExam; i++){
            sum = sum + exams[i].getGrade();
        }
        float avg = (float) sum/indexExam;
        float score = avg + (float) 10*(indexExam/indexCourse);
        return score;
    }

 
    public String printScore(){
        return first + " " + last + " : " + this.getScore() + "\n";
    }

    @Override
    public String toString(){
        return id + " " + first + " " + last;
    }



}
