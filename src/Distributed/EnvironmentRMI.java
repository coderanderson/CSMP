package Distributed;

import java.rmi.RemoteException;
import java.rmi.Remote;

public interface EnvironmentRMI extends Remote{
    void done(Message msg) throws RemoteException;
    void undone(Message msg) throws RemoteException;
}
