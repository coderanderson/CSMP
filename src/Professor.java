import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;


public class Professor implements ProfessorRMI{
    int me;
    String[] peers;
    int[] ports;
    int[] pref;
    int[] rank;
    int partner;

    Registry registry;
    ProfessorRMI myStub;

    public Professor(int me, String[] peers, int[] ports, int[] pref) {
        this.me = me;
        this.peers = peers;
        this.ports = ports;
        this.pref = pref;
        this.partner = -1;

        rank = new int[pref.length];
        for(int i = 0; i < pref.length; i++) {
            rank[pref[i]] = i;
        }

        try{
            System.setProperty("java.rmi.server.hostname", this.peers[this.me]);
            registry = LocateRegistry.createRegistry(this.ports[this.me + 100]);
            myStub = (ProfessorRMI) UnicastRemoteObject.exportObject(this, this.ports[this.me + 100]);
            registry.rebind("Professor", myStub);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void CallStudent(Message msg, int id) {
        StudentRMI stub;
        try{
            Registry registry = LocateRegistry.getRegistry(this.ports[id]);
            stub = (StudentRMI) registry.lookup("Student");

            String type = msg.getType();
            if(type == "reject")
                stub.reject(msg);
            else if(type == "accept")
                stub.accept(msg);
        } catch(Exception e){
            System.out.println("fail");
            e.printStackTrace();
        }
    }

    public void propose(Message msg) {
        int studentId = msg.getIndex();
        if(partner == -1) {
            partner = studentId;
            CallStudent(new Message(me, "accept"), studentId);
        }
        else if(rank[studentId] < rank[partner]) {
            CallStudent(new Message(me, "reject"), partner);
            partner = studentId;
            CallStudent(new Message(me, "accept"), studentId);
        }
        else if(rank[studentId] > rank[partner]) {
            CallStudent(new Message(me, "reject"), studentId);
        }
    }

    public void decide(Message msg) {

    }
}
