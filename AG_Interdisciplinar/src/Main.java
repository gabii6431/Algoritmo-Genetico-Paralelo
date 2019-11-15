
public class Main {
    
    public static void main(String args[]) {
        
        AlgoritmoGenetico ag = new AlgoritmoGenetico(100, 7, new double[]{-1,2}, 7);
        ag.mostrarPopulacao();
        ag.evoluir(2000);
        ag.mostrarPopulacao();
        
    }
    
}
