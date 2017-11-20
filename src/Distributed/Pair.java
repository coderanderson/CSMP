package Distributed;

public class Pair {
    private int student;
    private int professor;

    public Pair(int student, int professor) {
        this.student = student;
        this.professor = professor;
    }

    public int getStudent(){
        return student;
    }

    public int getProfessor() {
        return professor;
    }
}
