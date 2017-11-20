package Distributed;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class Student implements StudentRMI{
    int me;
    String EmtPeer;
    String[] StuPeers;
    String[] ProPeers;
    int EmtPort;
    int[] StuPorts;
    int[] ProPorts;
    ArrayList<ArrayList<Pair>> prerequisites;
    int[] pref;
    int current;
    boolean existCSM;
    int[] rank;
    boolean coupled;

    Registry registry;
    StudentRMI stub;

    public Student(int me, String EmtPeer, String[] StuPeers, String[] ProPeers,
                   int EmtPort, int[] StuPorts, int[] ProPorts,
                   ArrayList<ArrayList<Pair>> prerequisites, int[] pref) {
        this.me = me;

        this.EmtPeer = EmtPeer;
        this.StuPeers = StuPeers;
        this.ProPeers = ProPeers;

        this.EmtPort = EmtPort;
        this.StuPorts = StuPorts;
        this.ProPorts = ProPorts;

        this.prerequisites = prerequisites;
        this.pref = pref;
        this.current = 0;
        this.existCSM = false;
        this.coupled = false;

        rank = new int[pref.length];
        for(int i = 0; i < pref.length; i++) {
            rank[pref[i]] = i;
        }

        try{
            System.setProperty("java.rmi.server.hostname", this.StuPeers[this.me]);
            registry = LocateRegistry.createRegistry(this.StuPorts[this.me]);
            stub = (StudentRMI) UnicastRemoteObject.exportObject(this, this.StuPorts[this.me]);
            registry.rebind("Student", stub);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void CallStudent(String rmi, Message msg, int id){
        StudentRMI stub;
        try{
            Registry registry = LocateRegistry.getRegistry(this.StuPorts[id]);
            stub = (StudentRMI) registry.lookup("Student");
            if(rmi.equals("advance"))
                stub.advance(msg);
            else if(rmi.equals("decide"))
                stub.decide(msg);
        } catch(Exception e){
            System.out.println("fail");
            e.printStackTrace();
        }
    }


    public void CallProfessor(String rmi, Message msg, int id) {
        ProfessorRMI stub;
        try{
            Registry registry=LocateRegistry.getRegistry(this.ProPorts[id]);
            stub=(ProfessorRMI) registry.lookup("Professor");
            if(rmi.equals("reject"))
                stub.propose(msg);
            else if(rmi.equals("decide"))
                stub.decide(msg);
        } catch(Exception e) {
            System.out.println("fail");
        }
    }

    public void CallEnvironment(String rmi, Message msg) {
        EnvironmentRMI stub;
        try{
            Registry registry=LocateRegistry.getRegistry(this.EmtPort);
            stub=(EnvironmentRMI) registry.lookup("Environment");
            if(rmi.equals("done"))
                stub.done(msg);
            else if(rmi.equals("undone"))
                stub.undone(msg);
        } catch(Exception e) {
            System.out.println("fail");
        }
    }

    public void reject(Message msg) {
        if(pref[current] == msg.getIndex()) {
            if(coupled)
                CallEnvironment("undone", new Message(0));
            if(current == pref.length - 1) {
                System.out.println("no constrained stable marriage possible");
            } else {
                current++;
                for(Pair pair: prerequisites.get(current)) {
                    CallStudent("advance", new Message(pair.getProfessor()), pair.getStudent());
                }
                CallProfessor("reject", new Message(me), pref[current]);
            }
        }
    }

    public void accept(Message msg) {
        System.out.println("receive accept");
        if(msg.getIndex() == pref[current]) {
            CallEnvironment("done", new Message(me));
            this.coupled = true;
        }
    }

    public void advance(Message msg) {
        if(rank[msg.getIndex()] > current && coupled) {
            CallEnvironment("undone", new Message(me));
            coupled = false;
        }
        while(rank[msg.getIndex()] > current) {
            current++;
            for(Pair pair: prerequisites.get(current)) {
                CallStudent("advance", new Message(pair.getProfessor()), pair.getStudent());
            }
        }
        CallProfessor("reject", new Message(me), pref[current]);
    }

    public void initiate(Message msg) {
        System.out.println(me);
        for(Pair pair: prerequisites.get(current)) {
            CallStudent("advance", new Message(pair.getProfessor()), pair.getStudent());
        }
        CallProfessor("reject", new Message(me), pref[current]);
    }

    public void decide(Message msg) {
        System.out.println("Student " + me + " is decided, matched with Professor " + pref[current]);
        existCSM = true;
        CallProfessor("decide", new Message(me), pref[current]);
    }
}
