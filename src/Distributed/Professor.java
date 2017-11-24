package Distributed;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;


public class Professor implements ProfessorRMI{
    int me;
    String[] StuPeers;
    String[] ProPeers;
    int[] StuPorts;
    int[] ProPorts;
    int[] pref;
    int[] rank;
    int partner;
    boolean existCSM;

    Registry registry;
    ProfessorRMI myStub;

    public Professor(int me, String[] StuPeers, String[] ProPeers, int[] StuPorts, int[] ProPorts, int[] pref) {
        this.me = me;
        this.StuPeers = StuPeers;
        this.ProPeers = ProPeers;
        this.StuPorts = StuPorts;
        this.ProPorts = ProPorts;
        this.pref = pref;
        this.partner = -1;
        this.existCSM = true;

        rank = new int[StuPeers.length];
        for(int i = 0; i < pref.length; i++) {
            rank[pref[i]] = i;
        }

        try{
            System.setProperty("java.rmi.server.hostname", this.ProPeers[this.me]);
            registry = LocateRegistry.createRegistry(this.ProPorts[this.me]);
            myStub = (ProfessorRMI) UnicastRemoteObject.exportObject(this, this.ProPorts[this.me]);
            registry.rebind("Professor", myStub);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void CallStudent(Message msg, int id) {
        StudentRMI stub;
        try{
            Registry registry = LocateRegistry.getRegistry(this.StuPorts[id]);
            stub = (StudentRMI) registry.lookup("Student");
            String type = msg.getType();
            if(type.equals("reject"))
                stub.reject(msg);
            else if(type.equals("accept"))
                stub.accept(msg);
        } catch(Exception e){
            System.out.println("fail");
            e.printStackTrace();
        }
    }

    public void propose(Message msg) {
        if(!existCSM) return;
        int studentId = msg.getIndex();
        if(partner == -1) {
            System.out.println("" + me + " send accept to " + studentId);
            partner = studentId;
            CallStudent(new Message(me, "accept"), studentId);
        }
        else if(rank[studentId] < rank[partner]) {
            System.out.println("" + me + " send reject to " + partner);
            System.out.println("" + me + " send accept to " + studentId);
            int temp = partner;
            partner = studentId;
            CallStudent(new Message(me, "reject"), temp);
            CallStudent(new Message(me, "accept"), studentId);
        }
        else if(rank[studentId] > rank[partner]) {
            CallStudent(new Message(me, "reject"), studentId);
        }
    }

    public void decide(Message msg) {
        //System.out.println("Professor " + me + " is decided, matched with Student " + partner);
    }

    public void notify(Message msg) {
        existCSM = false;
        System.out.println("no constrained stable marriage possible");
    }
}
