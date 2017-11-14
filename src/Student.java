import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class Student implements StudentRMI{
    int me;
    String[] peers;
    int[] ports;
    ArrayList<ArrayList<Pair>> prerequisites;
    int[] pref;
    int current;
    boolean existCSM;
    int[] rank;

    Registry registry;
    StudentRMI stub;

    public Student(int me, String[] peers, int[] ports, ArrayList<ArrayList<Pair>> prerequisites, int[] pref) {
        this.me = me;
        this.peers = peers;
        this.ports = ports;
        this.prerequisites = prerequisites;
        this.pref = pref;
        this.current = 0;
        this.existCSM = false;

        rank = new int[pref.length];
        for(int i = 0; i < pref.length; i++) {
            rank[pref[i]] = i;
        }

        try{
            System.setProperty("java.rmi.server.hostname", this.peers[this.me]);
            registry = LocateRegistry.createRegistry(this.ports[this.me]);
            stub = (StudentRMI) UnicastRemoteObject.exportObject(this, this.ports[this.me]);
            registry.rebind("Student", stub);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void CallStudent(Message msg, int id){
        StudentRMI stub;
        try{
            Registry registry = LocateRegistry.getRegistry(this.ports[id]);
            stub = (StudentRMI) registry.lookup("Student");
            stub.advance(msg);
        } catch(Exception e){
            System.out.println("fail");
            e.printStackTrace();
        }
    }


    public void CallProfessor(Message msg, int id) {
        ProfessorRMI stub;
        try{
            Registry registry=LocateRegistry.getRegistry(this.ports[100 + id]);
            stub=(ProfessorRMI) registry.lookup("Professor");
            stub.advance(msg);
        } catch(Exception e) {
            System.out.println("fail");
        }
    }

    public void reject(Message msg) {
        if(pref[current] == msg.getIndex()) {
            if(current == pref.length - 1) {
                System.out.println("no constrained stable marriage possible");
            } else {
                current++;
                for(Pair pair: prerequisites.get(current)) {
                    CallStudent(new Message(pair.getProfessor()), pair.getStudent());
                }
                CallProfessor(new Message(me), pref[current]);
            }
        }
    }

    public void advance(Message msg) {
        while(rank[msg.getIndex()] > current) {
            current++;
            for(Pair pair: prerequisites.get(current)) {
                CallStudent(new Message(pair.getProfessor()), pair.getStudent());
            }
        }
        CallProfessor(new Message(me), pref[current]);
//        //for testing communication
//        System.out.println("answer");
    }

    public void initiate(Message msg) {
        for(Pair pair: prerequisites.get(current)) {
            CallStudent(new Message(pair.getProfessor()), pair.getStudent());
        }
        CallProfessor(new Message(me), pref[current]);
    }
}
