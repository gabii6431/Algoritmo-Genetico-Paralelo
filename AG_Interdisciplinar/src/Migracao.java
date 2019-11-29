import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface Migracao extends Remote{
//    public void recebe(ArrayList<Individuo> individuos) throws RemoteException;
    public void recebe(String txt) throws RemoteException;

}
