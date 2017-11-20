package Sequential;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StudentOptimal {
    //int[][] studentPreference;
    //int[][] professorPreference;
    List<List<Integer>> studentPreference = new ArrayList<>();
    List<List<Integer>> professorPreference = new ArrayList<>();
    int numOfStudent;
    int numOfProfessor;
    //int numOfProfessorProposedTo;
    StudentState[] globalState;

    public StudentOptimal(int[][] studentPreference, int[][] professorPreference/*, int numOfProfessorProposedTo*/) {
        for(int[] preference : studentPreference) {
            this.studentPreference.add(new ArrayList<>(Arrays.stream(preference).boxed().collect(Collectors.toList())));
        }

        for(int[] preference : professorPreference) {
            this.professorPreference.add(new ArrayList<>(Arrays.stream(preference).boxed().collect(Collectors.toList())));
        }

        this.numOfStudent = studentPreference.length;
        this.numOfProfessor = professorPreference.length;
        //this.numOfProfessorProposedTo = numOfProfessorProposedTo;

        globalState = new StudentState[numOfStudent];
        for(int id = 0; id < numOfStudent; id++) {
            globalState[id] = new StudentState(id, this.studentPreference.get(id));
        }
    }

    public void importPrerequisites(int student, List<PrerequisitePair>[] preference) {
        globalState[student].updatePrerequisite(preference);
    }

    public void moveToFirstConsistentCut() {
        for(StudentState student : globalState) {
            if(student.currentPreferenceIndex == -1) {
                student.moveToNext(globalState);
            }
        }
    }

    /*public static boolean isProfessorAssigned(int professorIndex) {
        return globalState[professorIndex].currentPreferenceIndex == -1;
    }*/

    public /*int*/ boolean isNotAMatching(int studentIndex) {
        for(StudentState otherStudent : globalState) {
            boolean sameProfessor = globalState[studentIndex].pairIndex == otherStudent.pairIndex;
            boolean preferOtherStudent = (professorPreference.get(otherStudent.pairIndex).indexOf(otherStudent.id) <
                    professorPreference.get(otherStudent.pairIndex).indexOf(globalState[studentIndex].id));
            if(sameProfessor && preferOtherStudent) return true;//otherStudent.id;
        }
        return false;
    }

    public boolean isNotStable(int studentIndex) {
        int professorIndex = globalState[studentIndex].pairIndex;
        int studentRankByProfessor = professorPreference.get(professorIndex).indexOf(studentIndex);
        for(int i = 0; i < studentRankByProfessor; i++) {
            StudentState otherStudent = globalState[professorPreference.get(professorIndex).get(i)];
            if(studentPreference.get(otherStudent.id).indexOf(professorIndex) < otherStudent.currentPreferenceIndex)
                return true;
        }
        return false;
    }

    public boolean isForbidden(int studentIndex) {
        return (isNotAMatching(studentIndex) || isNotStable(studentIndex));
    }

    public void getStudentOptimal() {
        moveToFirstConsistentCut();
        while(true) {
            int id = 0;
            for(; id < numOfStudent; id++) {
                if(isForbidden(id)) break;
            }
            if(id == numOfStudent) break;
            if(globalState[id].isFinal()) {
                System.out.println("No stable marriage found.");
                return;
            }
            else globalState[id].moveToNext(globalState);
        }
        System.out.println("Stable marriage found.");
        for(int id = 0; id < numOfStudent; id++){
            System.out.println("student " + id + ": professor: " + globalState[id].pairIndex);
        }
    }


}

class StudentState {
    int id;
    int pairIndex;
    List<Integer> preference;
    int currentPreferenceIndex;
    int lastPreferenceIndex;
    List<PrerequisitePair>[] prerequisite;
    boolean prerequisiteNull;


    public StudentState(int id, List<Integer> preference) {
        this.id = id;
        //this.pairIndex = preference.get(0);
        this.preference = preference;
        this.currentPreferenceIndex = -1;
        this.lastPreferenceIndex = -1;
        this.prerequisiteNull = true;
    }

    public void updatePrerequisite(List<PrerequisitePair>[] prerequisite) {
        this.prerequisite = prerequisite;
        this.prerequisiteNull = false;
    }

    public void moveToNext(StudentState[] globalState) {
        lastPreferenceIndex = currentPreferenceIndex;
        pairIndex = preference.get(++currentPreferenceIndex);
        moveByPrerequisite(globalState);
    }

    public void advance(StudentState[] globalState, int preferenceIndex) {
        if(preferenceIndex > currentPreferenceIndex) {
            lastPreferenceIndex = currentPreferenceIndex;
            currentPreferenceIndex = preferenceIndex;
            pairIndex = preference.get(preferenceIndex);
            moveByPrerequisite(globalState);
        }
    }

    public void moveByPrerequisite(StudentState[] globalState) {
        if(prerequisiteNull) return;

        int sizeOfPrerequisiteConcerned = currentPreferenceIndex - lastPreferenceIndex;
        List<PrerequisitePair>[] CopiedPrerequisite = new ArrayList[sizeOfPrerequisiteConcerned];
        for(int i = 0; i < sizeOfPrerequisiteConcerned; i++) {
            if(prerequisite[lastPreferenceIndex + i + 1] != null)
                CopiedPrerequisite[i] = new ArrayList<>(prerequisite[lastPreferenceIndex + i + 1]);
        }

        for(int i = 0; i < sizeOfPrerequisiteConcerned; i++) {
            if(CopiedPrerequisite[i] != null)
                for(PrerequisitePair pair : CopiedPrerequisite[i]) {
                    globalState[pair.student].advance(globalState, pair.preferenceIndex);
            }
        }
    }

    public boolean isFinal() {
        return (currentPreferenceIndex == preference.size() - 1);
    }
}

class PrerequisitePair {
    int student;
    int preferenceIndex;

    public PrerequisitePair(int student, int preferenceIndex) {
        this.student = student;
        this.preferenceIndex = preferenceIndex;
    }
}

