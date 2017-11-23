package Distributed;

import java.util.*;

public class StudentTest {
    private static Student[] initStudents(int nStudent, String EmtPeer, int EmtPort,
                                          String[] StuPeers, int[] StuPorts,
                                          String[] ProPeers, int[] ProPorts,
                                          ArrayList<ArrayList<Pair>> prerequisites,
                                          int[][] studentPreference) {
        Student[] students = new Student[nStudent];

        ArrayList<ArrayList<Pair>> specialPrerequisite = new ArrayList<ArrayList<Pair>>();
        for(int i = 0; i < ProPorts.length; i++) {
            specialPrerequisite.add(new ArrayList<Pair>());
        }
        specialPrerequisite.get(0).add(new Pair(0, 3));

        specialPrerequisite.get(1).add(new Pair(0, 0));

        specialPrerequisite.get(2).add(new Pair(0, 1));

        specialPrerequisite.get(3).add(new Pair(0, 2));


        for(int i = 0; i < nStudent; i++){
            if(i == 1) {
                students[i] = new Student(i, EmtPeer, StuPeers, ProPeers, EmtPort, StuPorts, ProPorts, specialPrerequisite, studentPreference[i]);
            } else
                students[i] = new Student(i, EmtPeer, StuPeers, ProPeers, EmtPort, StuPorts, ProPorts, prerequisites, studentPreference[i]);
        }
        return students;
    }

    private static Professor[] initProfessor(int nProfessor, String[] StuPeers, String[] ProPeers, int[] StuPorts, int[] ProPorts, int[][] pref) {
        Professor[] professors = new Professor[nProfessor];

        for(int i = 0; i < nProfessor; i++) {
            professors[i] = new Professor(i, StuPeers, ProPeers, StuPorts, ProPorts, pref[i]);
        }
        return professors;
    }

    public static void main(String[] args) {

        String host = "127.0.0.1";

        int nStudent = 4;
        int nProfessor = 4;

        String EmtPeer = host;
        int EmtPort = 1300;

        String[] StuPeers = new String[nStudent];
        int[] StuPorts = new int[nStudent];

        String[] ProPeers = new String[nProfessor];
        int[] ProPorts = new int[nProfessor];

        for(int i = 0; i < nStudent; i++) {
            StuPeers[i] = host;
            StuPorts[i] = 1100 + i;
        }

        for(int i = 0; i < nProfessor; i++) {
            ProPeers[i] = host;
            ProPorts[i] = 1200 + i;
        }

        ArrayList<ArrayList<Pair>> prerequisites = new ArrayList<ArrayList<Pair>>();
        for(int i = 0; i < nProfessor; i++) {
            prerequisites.add(new ArrayList<Pair>());
        }

//        List<Integer> tempList = new ArrayList<Integer>();
//
//        for(int i = 0; i < 40; i++) {
//            tempList.add(i);
//        }
//
//        int[][] studentPreference = new int[40][40];
//        for(int i = 0; i < 40; i++) {
//            Collections.shuffle(tempList);
//            studentPreference[i]
//        }


        int[][] studentPreference = {
                {3, 0, 1, 2},
                {1, 2, 0, 3},
                {2, 0, 3, 1},
                {1, 3, 2, 0}
        };

        int[][] professorPreference = {
                {3, 0, 2, 1},
                {0, 3, 1, 2},
                {0, 1, 3, 2},
                {2, 0, 3, 1}
        };

        Student[] students = initStudents(nStudent, EmtPeer, EmtPort,
                                          StuPeers, StuPorts, ProPeers,
                                          ProPorts, prerequisites, studentPreference);

        Professor[] professors = initProfessor(nProfessor, StuPeers, ProPeers, StuPorts, ProPorts, professorPreference);

        Environment environment = new Environment(EmtPeer, EmtPort, nStudent, StuPorts);

        environment.startComputation();
    }
}
