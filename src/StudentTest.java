import java.util.*;

public class StudentTest {
    private static Student[] initStudents(int nStudent){
        String host = "127.0.0.1";
        String[] peers = new String[nStudent];
        int[] ports = new int[nStudent];
        Student[] students = new Student[nStudent];
        ArrayList<ArrayList<Pair>> prerequisites = new ArrayList<ArrayList<Pair>>();
        int[] pref = new int[]{0, 1};
        for(int i = 0 ; i < nStudent; i++){
            ports[i] = 1100+i;
            peers[i] = host;
        }
        for(int i = 0; i < nStudent; i++){
            students[i] = new Student(i, peers, ports, prerequisites, pref);
        }
        return students;
    }

    public static void main(String[] args) {
        Student[] students = initStudents(2);
        students[0].CallStudent(new Message(0), 1);
    }
}
