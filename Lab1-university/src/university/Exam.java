package university;

public class Exam {
    
    private int grade;


    public Exam(Student student, Course course, int grade){
        this.grade = grade;
    }

    public int getGrade(){
        return grade;
    }

}
