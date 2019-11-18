
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    
    
    public static void main(String args[]) {
        int qntIndividuos = 0;
        String ilhaDestino = "";
        int instanteMigracao = 0;
        
        AlgoritmoGenetico ag;
        try {
            ag = new AlgoritmoGenetico(100, 7, new double[]{-1,2}, 7);
            ag.mostrarPopulacao();
            ag.evoluir(2000, qntIndividuos, ilhaDestino, instanteMigracao);
            ag.mostrarPopulacao();
        } catch (RemoteException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
}
