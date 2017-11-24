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
        specialPrerequisite.get(0).add(new Pair(0, 6));
        specialPrerequisite.get(1).add(new Pair(0, 8));
        specialPrerequisite.get(2).add(new Pair(0, 2));
        specialPrerequisite.get(3).add(new Pair(0, 0));
        specialPrerequisite.get(4).add(new Pair(0, 4));
        specialPrerequisite.get(5).add(new Pair(0, 1));
        specialPrerequisite.get(6).add(new Pair(0, 3));
        specialPrerequisite.get(7).add(new Pair(0, 9));
        specialPrerequisite.get(8).add(new Pair(0, 7));
        specialPrerequisite.get(9).add(new Pair(0, 5));


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

        int nStudent = 10;
        int nProfessor = 10;

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

        List<Integer> tempList = new ArrayList<Integer>();

//        for(int i = 0; i < 10; i++) {
//            tempList.add(i);
//        }
//
//        int[][] studentPreference = new int[10][10];
//
//        for(int i = 0; i < 10; i++) {
//            Collections.shuffle(tempList);
//            for(int j = 0; j < 10; j++) {
//                studentPreference[i][j] = tempList.get(j);
//            }
//        }
//
//        for(int i = 0; i < 10; i++) {
//            for(int j = 0; j < 10; j++) {
//                System.out.print("" + studentPreference[i][j] + ", ");
//            }
//            System.out.println("");
//
//        }
//
//        System.out.println("");
//
//        int[][] professorPreference = new int[10][10];
//
//        for(int i = 0; i < 10; i++) {
//            Collections.shuffle(tempList);
//            for(int j = 0; j < 10; j++) {
//                professorPreference[i][j] = tempList.get(j);
//            }
//        }
//
//        for(int i = 0; i < 10; i++) {
//            for(int j = 0; j < 10; j++) {
//                System.out.print("" + professorPreference[i][j] + ", ");
//            }
//            System.out.println("");
//        }


        int[][] studentPreference = {
                {6, 8, 2, 0, 4, 1, 3, 9, 7, 5},
                {7, 9, 1, 6, 2, 0, 4, 5, 3, 8},
                {5, 0, 6, 3, 8, 2, 9, 4, 1, 7},
                {4, 0, 7, 9, 5, 2, 8, 1, 6, 3},
                {9, 0, 3, 2, 5, 8, 1, 4, 7, 6},
                {0, 4, 8, 3, 6, 7, 1, 9, 2, 5},
                {9, 3, 8, 2, 5, 6, 0, 7, 1, 4},
                {2, 3, 7, 1, 5, 4, 8, 9, 0, 6},
                {3, 1, 4, 7, 2, 5, 9, 8, 0, 6},
                {0, 8, 7, 3, 6, 4, 9, 5, 2, 1}
        };

        int[][] professorPreference = {
                {1, 7, 4, 3, 0, 8, 9, 6, 5, 2},
                {1, 3, 0, 7, 9, 5, 4, 2, 8, 6},
                {4, 5, 6, 9, 8, 1, 0, 2, 7, 3},
                {6, 9, 4, 7, 3, 5, 0, 8, 2, 1},
                {3, 8, 1, 2, 4, 7, 5, 6, 0, 9},
                {8, 9, 2, 5, 0, 4, 1, 6, 7, 3},
                {5, 9, 0, 1, 6, 4, 2, 7, 3, 8},
                {4, 2, 0, 8, 7, 9, 6, 5, 1, 3},
                {2, 4, 0, 1, 6, 5, 8, 3, 7, 9},
                {6, 0, 8, 9, 7, 1, 2, 3, 5, 4}
        };

        Student[] students = initStudents(nStudent, EmtPeer, EmtPort,
                                          StuPeers, StuPorts, ProPeers,
                                          ProPorts, prerequisites, studentPreference);

        Professor[] professors = initProfessor(nProfessor, StuPeers, ProPeers, StuPorts, ProPorts, professorPreference);

        Environment environment = new Environment(EmtPeer, EmtPort, nStudent, StuPorts);

        environment.startComputation();
    }
}
