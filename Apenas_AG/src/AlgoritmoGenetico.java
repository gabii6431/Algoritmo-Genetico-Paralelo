import java.util.ArrayList;
import java.util.Random;
public class AlgoritmoGenetico{
    
    private ArrayList<Individuo> populacao;
    private int probabilidadeMutacao;
    private ArrayList<Individuo> individuosRecebidos;
    private Individuo melhorIndividuo;
    
    
    public AlgoritmoGenetico(int tamanhoPopulacao,int probabilidadeMutacao,double intervalo[],int precisao){
        populacao = new ArrayList<Individuo>();
        this.individuosRecebidos = new ArrayList<Individuo>();
        this.probabilidadeMutacao = probabilidadeMutacao;
        inicializarPopulacao(tamanhoPopulacao, intervalo, precisao);
    }
    
    public void evoluir(int numGeracoes){
        Operacoes op = new Operacoes();
        int geracaoAtual = 0;
        while(geracaoAtual <= numGeracoes) {
            ArrayList<Individuo> novaPopulacao = new ArrayList<Individuo>();
            
            while(novaPopulacao.size() < populacao.size()) {
                Individuo i1 = (Individuo) op.roleta(populacao);
                Individuo i2 = (Individuo) op.roleta(populacao);
                Individuo filhos[] = op.crossover(i1, i2);
                op.mutacao(filhos[0], probabilidadeMutacao);
                op.mutacao(filhos[1], probabilidadeMutacao);
                novaPopulacao.add(filhos[0]);
                novaPopulacao.add(filhos[1]);
            }
            populacao = novaPopulacao;
            geracaoAtual++;
        }
        encontraMelhorIndividuo();
    }
    
    private void inicializarPopulacao(int tamanhoPopulacao,double intervalo[],int precisao) {
        Random r = new Random();
        
        double tamanhoIntervalo = intervalo[1]-intervalo[0];
        int log = (int)log2(tamanhoIntervalo*Math.pow(10, precisao));
        int tamanhoCromossomo = log+1;
        
        for(int i = 0; i < tamanhoPopulacao; ++i) {
            int cromossomo[] = new int[tamanhoCromossomo];
            for(int j = 0; j < tamanhoCromossomo; ++j) {
                cromossomo[j] = r.nextInt(2);
            }
            Individuo individuo = new Individuo(cromossomo, intervalo, precisao);
            populacao.add(individuo);
        }
    }
    
    private double log2(double valor) {
        return Math.log10(valor)/Math.log10(2);
    } 
    
    private void encontraMelhorIndividuo(){
        int index = 0;
        double maxValue = 0;
        for (int i = 0; i < populacao.size(); i++) {
            if(populacao.get(i).getAptidao() > maxValue){
                index = i;
                maxValue = populacao.get(i).getAptidao();
            }
        }
        melhorIndividuo = populacao.get(index);
    }
    
    public void imprimeMelhorIndividuo(){
        System.out.println("Melhor Individuo: ");
        melhorIndividuo.mostrarIndividuo();
    }
    
    public void mostrarPopulacao() {
        for(Individuo i:populacao) {
            i.mostrarIndividuo();
        }
    }
    
    public static void main(String args[]) {
        AlgoritmoGenetico ag;
        
        int tamanhoPopulacao = 100;
        int probabilidadeMutacao = 7;
        double[] intervalo = new double[]{-1,2};
        int precisao = 4;
        int numGeracoes = 1000;
        
        ag = new AlgoritmoGenetico(tamanhoPopulacao, probabilidadeMutacao,intervalo, precisao);
        ag.mostrarPopulacao();
        System.out.println("\nEvoluindo..\n");
        ag.evoluir(numGeracoes);
        ag.mostrarPopulacao();
        System.out.println("\n");
        ag.imprimeMelhorIndividuo();
        
    }
}
