
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;



public class AlgoritmoGenetico{
    
    private ArrayList<Individuo> populacao;
    private int probabilidadeMutacao;
    
    public AlgoritmoGenetico(int tamanhoPopulacao,int probabilidadeMutacao,double intervalo[],int precisao) {
        populacao = new ArrayList<Individuo>();
        this.probabilidadeMutacao = probabilidadeMutacao;
        inicializarPopulacao(tamanhoPopulacao, intervalo, precisao);
    }
    
    public void evoluir(int numGeracoes) {
        Operacoes op = new Operacoes();
        while(numGeracoes > 0) {
            System.out.println("Geração ("+numGeracoes+")");
            ArrayList<Individuo> novaPopulacao = new ArrayList<Individuo>();
            while(novaPopulacao.size() < populacao.size()) {
                IndividuoBinario i1 = (IndividuoBinario) op.roleta(populacao);
                IndividuoBinario i2 = (IndividuoBinario) op.roleta(populacao);
                IndividuoBinario filhos[] = op.crossover(i1, i2);
                op.mutacao(filhos[0], probabilidadeMutacao);
                op.mutacao(filhos[1], probabilidadeMutacao);
                novaPopulacao.add(filhos[0]);
                novaPopulacao.add(filhos[1]);
            }
            populacao = novaPopulacao;
            numGeracoes--;
        }
    }
    
        public void evoluirElitismo(int numGeracoes, int porcentagemSelecao) {
        Operacoes op = new Operacoes();
        int aux = 0;
        int quantidadeElitismo = populacao.size() * (porcentagemSelecao/100);
        
        while(numGeracoes > 0) {
            System.out.println("Geração ("+numGeracoes+")");
            ArrayList<Individuo> novaPopulacao = new ArrayList<Individuo>();
            Collections.sort(populacao, (a,b) -> a.getAptidao() < b.getAptidao() ? -1: a.getAptidao() > b.getAptidao() ? 1:0);
            while(aux != quantidadeElitismo){
                novaPopulacao.add(populacao.get(aux));
                aux++;
            }
            if(quantidadeElitismo % 2 != 0){
                IndividuoBinario aleatorio = (IndividuoBinario) op.roleta(populacao);
                novaPopulacao.add(aleatorio);
            }
            while(novaPopulacao.size() < populacao.size()) {
                IndividuoBinario i1 = (IndividuoBinario) op.roleta(populacao);
                IndividuoBinario i2 = (IndividuoBinario) op.roleta(populacao);
                IndividuoBinario filhos[] = op.crossover(i1, i2);
                op.mutacao(filhos[0], probabilidadeMutacao);
                op.mutacao(filhos[1], probabilidadeMutacao);
                novaPopulacao.add(filhos[0]);
                novaPopulacao.add(filhos[1]);
            }
            populacao = novaPopulacao;
            numGeracoes--;
        }
    }

    
    public void mostrarPopulacao() {
        for(Individuo i:populacao) {
            i.mostrarIndividuo();
        }
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
            IndividuoBinario individuo = new IndividuoBinario(cromossomo, intervalo, precisao);
            populacao.add(individuo);
        }
    }
    
    private double log2(double valor) {
        return Math.log10(valor)/Math.log10(2);
    } 
    
}
