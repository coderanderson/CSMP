import java.rmi.RemoteException;
import java.rmi.Remote;

public interface ProfessorRMI extends Remote{
    void propose(Message msg) throws RemoteException;
    void decide(Message msg) throws RemoteException;
}
