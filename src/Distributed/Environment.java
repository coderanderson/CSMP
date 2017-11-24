package Distributed;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class Environment implements EnvironmentRMI {
    String EmtPeer;
    int EmtPort;
    int numOfStudent;
    int[] StuPorts;
    int studentCounter;
    boolean isDone;

    Registry registry;
    EnvironmentRMI myStub;

    public Environment(String EmtPeer, int EmtPort, int numOfStudent, int[] StuPorts) {
        this.EmtPeer = EmtPeer;
        this.EmtPort = EmtPort;
        this.numOfStudent = numOfStudent;
        this.StuPorts = StuPorts;
        this.studentCounter = 0;
        this.isDone = false;

        try {
            System.setProperty("java.rmi.server.hostname", this.EmtPeer);
            registry = LocateRegistry.createRegistry(this.EmtPort);
            myStub = (EnvironmentRMI) UnicastRemoteObject.exportObject(this, this.EmtPort);
            registry.rebind("Environment", myStub);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void CallStudent(String rmi, Message msg, int id) {
        StudentRMI stub;
        try{
            Registry registry = LocateRegistry.getRegistry(this.StuPorts[id]);
            stub = (StudentRMI) registry.lookup("Student");
            if(rmi.equals("initiate"))
                stub.initiate(msg);
            else if(rmi.equals("decide"))
                stub.decide(msg);
        } catch(Exception e){
            System.out.println("fail");
            e.printStackTrace();
        }
    }

    public void done(Message msg) {
        System.out.println("" + msg.getIndex() + " is done");
        ++studentCounter;
        System.out.println("sum of done is" + studentCounter);
//        try {
//            Thread.sleep(50000);
//        }
//        catch(InterruptedException ex) {
//            Thread.currentThread().interrupt();
//        }
        if(studentCounter == numOfStudent) {
            isDone = true;
            System.out.println("Stable marriage found.");
            for(int i = 0; i < numOfStudent; i++) {
                CallStudent("decide", new Message(0), i);
            }
        }
    }

    public void undone(Message msg) {
        //System.out.println("" + msg.getIndex() + " send undone");
        studentCounter--;
    }

    public void startComputation() {
        for(int id = 0; id < numOfStudent; id++) {
            final int i = id;
            (new Thread("Student " + id + " awake.") {
                public void run() {
                    CallStudent("initiate", new Message(0), i);
                }
            }).start();
        }
    }
}