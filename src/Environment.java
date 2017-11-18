import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class Environment implements EnvironmentRMI {
    int me;
    String[] peers;
    int[] ports;
    int numOfStudent;
    int studentCounter;
    boolean isDone;

    Registry registry;
    EnvironmentRMI myStub;

    public Environment(int me, String[] peers, int[] ports, int numOfStudent) {
        this.me = me;
        this.peers = peers;
        this.ports = ports;
        this.numOfStudent = numOfStudent;
        this.studentCounter = 0;
        this.isDone = false;

        try {
            System.setProperty("java.rmi.server.hostname", this.peers[this.me]);
            registry = LocateRegistry.createRegistry(this.ports[this.me]);
            myStub = (EnvironmentRMI) UnicastRemoteObject.exportObject(this, this.ports[this.me]);
            registry.rebind("Environment", myStub);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void CallStudent(Message msg, int id) {
        StudentRMI stub;
        try{
            Registry registry = LocateRegistry.getRegistry(this.ports[id]);
            stub = (StudentRMI) registry.lookup("Student");
            stub.initiate(msg);
        } catch(Exception e){
            System.out.println("fail");
            e.printStackTrace();
        }
    }

    public void done(Message msg) {
        if(++studentCounter == numOfStudent) {
            isDone = true;
            System.out.println("Stable marriage found.");
        }
    }

    public void undone(Message msg) {
        studentCounter--;
    }

    public void startComputation() {
        for(int id = 0; id < numOfStudent; id++) {
            (new Thread("Student " + id + " awake.") {
                public void run() {
                    CallStudent(new Message(me), id);
                }
            }).start();
        }
    }
}