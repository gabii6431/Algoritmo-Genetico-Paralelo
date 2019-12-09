import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface Migracao extends Remote{
    public ArrayList<Individuo> recebeIndividuos(int geracao) throws RemoteException;
    public boolean verificaIndividuosProntos() throws RemoteException;
}
