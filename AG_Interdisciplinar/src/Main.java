
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    
    
    public static void main(String args[]) {
        int qntIndividuos = 50;
        String ilhaDestino = "";
        int instanteMigracao = 5;
        
        AlgoritmoGenetico ag;
        int n=0; 
        if(n==0){
            Scanner teclado = new Scanner(System.in);
            n = teclado.nextInt();
        }
        try {
            ag = new AlgoritmoGenetico(100, 7, new double[]{-1,2}, 7);
            ag.mostrarPopulacao();
            ag.evoluir(2000, qntIndividuos, instanteMigracao);
            ag.mostrarPopulacao();
        } catch (RemoteException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
}
