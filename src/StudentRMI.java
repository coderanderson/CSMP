import java.rmi.RemoteException;
import java.rmi.Remote;

public interface StudentRMI extends Remote{
    void reject(Message msg) throws RemoteException;
    void advance(Message msg) throws RemoteException;
    void initiate(Message msg) throws RemoteException;
}
