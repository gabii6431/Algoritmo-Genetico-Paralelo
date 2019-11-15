import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class AlgoritmoGenetico extends UnicastRemoteObject implements Migracao{
    
    private ArrayList<Individuo> populacao;
    private int probabilidadeMutacao;
    private ArrayList<Individuo> individuosRecebidos;
    Thread tr = new Thread();
    
    
    public AlgoritmoGenetico(int tamanhoPopulacao,int probabilidadeMutacao,double intervalo[],int precisao) throws RemoteException{
        populacao = new ArrayList<Individuo>();
        individuosRecebidos = new ArrayList<Individuo>();
        this.probabilidadeMutacao = probabilidadeMutacao;
        tr.start();
        inicializarPopulacao(tamanhoPopulacao, intervalo, precisao);
    }
    
    public void evoluir(int numGeracoes, int qntIndividuos,String ilhaDestino, int instanteMigracao) throws NotBoundException, MalformedURLException, RemoteException, InterruptedException {
        Migracao remoteObjectReference = (Migracao) Naming.lookup(ilhaDestino);
        Operacoes op = new Operacoes();
        
        while(numGeracoes > 0) {
            ArrayList<Individuo> novaPopulacao = new ArrayList<Individuo>();
            if(numGeracoes == instanteMigracao){
                remoteObjectReference.recebe(this.melhoresIndividuos(qntIndividuos));
                populacao.removeAll(this.melhoresIndividuos(qntIndividuos));
                // Espera receber individuos
                synchronized(tr){
                    tr.wait();
                }
                populacao.addAll(this.individuosRecebidos);
                
            }
//            System.out.println("Geração ("+numGeracoes+")");
            
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

    public ArrayList<Individuo> melhoresIndividuos(int qtdIndividuos){
        ArrayList<Individuo> melhoresIndividuos = new ArrayList<>();
        Collections.sort(populacao, (a,b) -> a.getAptidao() < b.getAptidao() ? -1: a.getAptidao() > b.getAptidao() ? 1:0);

        for (int i = 0; i < qtdIndividuos; i++) {
            melhoresIndividuos.add(populacao.get(i));
        }
        return melhoresIndividuos;
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
    
    @Override
    public void recebe(ArrayList<Individuo> individuos) throws RemoteException {
        individuosRecebidos = new ArrayList<>();
        individuosRecebidos.addAll(individuos);
        synchronized(this){
            notify();
        }
        
    }

}
