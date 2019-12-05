import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AlgoritmoGenetico extends UnicastRemoteObject implements Migracao{
    
    private ArrayList<Individuo> populacao;
    private int probabilidadeMutacao;
    private boolean individuosProntos;
    private ArrayList<Individuo> individuosRecebidos;
    private ArrayList<Individuo> individuosAEnviar;
    private Individuo melhorIndividuo;

    ThreadVerifica tr = new ThreadVerifica();
    Migracao remoteObjectReference;
    
    
    public AlgoritmoGenetico(int tamanhoPopulacao,int probabilidadeMutacao,double intervalo[],int precisao) throws RemoteException, MalformedURLException, NotBoundException{
        
        this.probabilidadeMutacao = probabilidadeMutacao;
        this.individuosProntos = false;
        this.populacao = new ArrayList<Individuo>();
        this.individuosRecebidos = new ArrayList<Individuo>();
        this.individuosAEnviar = new ArrayList<Individuo>();
        
        inicializarPopulacao(tamanhoPopulacao, intervalo, precisao);
        
        //configRMI(ipOrigem, ipDestino);
    }

    public void configRMI(String ipOrigem, String ipDestino) throws RemoteException, MalformedURLException, NotBoundException{
        
       
    }
    
    public void evoluir(int numGeracoes, int qntIndividuos,int instanteMigracao) throws NotBoundException, MalformedURLException, RemoteException, InterruptedException {
        remoteObjectReference = (Migracao) Naming.lookup("rmi://10.14.160.247/AlgoritmoGenetico");
        Operacoes op = new Operacoes();
        int geracaoAtual = 0;
        int momentoMigracao = instanteMigracao;
        
        while(geracaoAtual <= numGeracoes) {
            
            ArrayList<Individuo> novaPopulacao = new ArrayList<Individuo>();
            
            if(geracaoAtual == momentoMigracao){ // Inicio migração
                momentoMigracao += instanteMigracao;
                
                this.individuosAEnviar = this.melhoresIndividuos(qntIndividuos);
                this.individuosProntos = true;
                populacao.removeAll(this.individuosAEnviar);
                
               
                System.out.println("Tamanho individuos a enviar: "+this.individuosAEnviar.size());
                
                while(!remoteObjectReference.verificaIndividuosProntos()){
                    Thread.sleep(1000);
                }
                
                this.individuosRecebidos = remoteObjectReference.recebeIndividuos();
                System.out.println("Tamanho individuos Recebidos: "+this.individuosRecebidos.size());
                
                populacao.addAll(this.individuosRecebidos);
//                
            } // Fim da Migração
            
            
            System.out.println("Geracao ("+geracaoAtual+")");
            
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
        System.out.println("Variavel: "+this.individuosProntos);
        System.out.println("Individuos array: "+this.individuosAEnviar.size());
        encontraMelhorIndividuo();
    }
    
    
    public ArrayList<Individuo> recebeIndividuos() throws RemoteException{
        this.individuosProntos = false;
        System.out.println("Entrou recebe individuos");
        return this.individuosAEnviar;
    }
    
    public boolean verificaIndividuosProntos() throws RemoteException{
        return this.individuosProntos;
    }

    public ArrayList<Individuo> melhoresIndividuos(int qtdIndividuos){
        ArrayList<Individuo> melhoresIndividuos = new ArrayList<>();
        Collections.sort(populacao, (a,b) -> a.getAptidao() < b.getAptidao() ? -1: a.getAptidao() > b.getAptidao() ? 1:0);

        for (int i = 0; i < qtdIndividuos; i++) {
            melhoresIndividuos.add(populacao.get(i));
        }
        return melhoresIndividuos;
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
        
        System.out.println("Digite um numero para continuar:");
        
        
        int qntIndividuos = 50;
        int instanteMigracao = 15;
        
        int tamanhoPopulacao = 100;
        int probabilidadeMutacao = 7;
        double[] intervalo = new double[]{-1,2};
        int precisao = 4;
        int numGeracoes = 500;
        
        try {
            ag = new AlgoritmoGenetico(tamanhoPopulacao, probabilidadeMutacao,intervalo, precisao);
            
            System.setProperty("java.rmi.server.hostname", "10.14.160.9" );
	   
            Naming.rebind("rmi://10.14.160.9/AlgoritmoGenetico", ag);
        
            int n=0; 
            if(n==0){
                Scanner teclado = new Scanner(System.in);
                n = teclado.nextInt();
            }
            ag.mostrarPopulacao();
            System.out.println("\nEvoluindo..\n");
            ag.evoluir(numGeracoes, qntIndividuos, instanteMigracao);
            ag.mostrarPopulacao();
            System.out.println("\n");
            ag.imprimeMelhorIndividuo();
        } catch (RemoteException ex) {
            Logger.getLogger(AlgoritmoGenetico.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(AlgoritmoGenetico.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(AlgoritmoGenetico.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(AlgoritmoGenetico.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    
}

