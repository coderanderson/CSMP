package Sequential;

import java.util.ArrayList;
import java.util.List;

public class SequentialCSMP {
    public static void main(String[] args) {
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

        List<PrerequisitePair>[] prerequisite = new ArrayList[10];

        for(int i = 0; i < 10; i++) {
            prerequisite[i] = new ArrayList<>();
            prerequisite[i].add(new PrerequisitePair(0, i));
        }

        StudentOptimal studentOptimal = new StudentOptimal(studentPreference, professorPreference);
        studentOptimal.globalState[1].updatePrerequisite(prerequisite);

        studentOptimal.getStudentOptimal();
    }


}
