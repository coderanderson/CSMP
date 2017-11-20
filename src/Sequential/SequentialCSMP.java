package Sequential;

import java.util.ArrayList;
import java.util.List;

public class SequentialCSMP {
    public static void main(String[] args) {
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

        List<PrerequisitePair>[] prerequisite = new ArrayList[4];

        for(int i = 0; i < 4; i++) {
            prerequisite[i] = new ArrayList<>();
            prerequisite[i].add(new PrerequisitePair(0, i));
        }
        StudentOptimal studentOptimal = new StudentOptimal(studentPreference, professorPreference);
        studentOptimal.globalState[1].updatePrerequisite(prerequisite);

        studentOptimal.getStudentOptimal();
    }


}
