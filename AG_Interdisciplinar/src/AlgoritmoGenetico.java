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
    private ArrayList<Individuo> individuosRecebidos;
    ThreadVerifica tr = new ThreadVerifica();
    
    
    public AlgoritmoGenetico(int tamanhoPopulacao,int probabilidadeMutacao,double intervalo[],int precisao) throws RemoteException, MalformedURLException{
        System.setProperty("java.rmi.server.hostname", "172.16.104.67" );
	   
        AlgoritmoGenetico ag = new AlgoritmoGenetico();
        Naming.rebind("rmi://172.16.104.67/AlgoritmoGenetico", ag);
        
        populacao = new ArrayList<Individuo>();
        individuosRecebidos = new ArrayList<Individuo>();
        this.probabilidadeMutacao = probabilidadeMutacao;
        inicializarPopulacao(tamanhoPopulacao, intervalo, precisao);
    }

    public AlgoritmoGenetico() throws RemoteException{    
    }
    
    public void evoluir(int numGeracoes, int qntIndividuos,/*String ilhaDestino,*/ int instanteMigracao) throws NotBoundException, MalformedURLException, RemoteException, InterruptedException {
        Migracao remoteObjectReference = (Migracao) Naming.lookup("rmi://172.16.104.108/AlgoritmoGenetico");
        Operacoes op = new Operacoes();
        int geracaoAtual = 0;
        while(geracaoAtual <= numGeracoes) {
            System.out.println("Geracao: "+geracaoAtual);
            ArrayList<Individuo> novaPopulacao = new ArrayList<Individuo>();
            if(geracaoAtual == instanteMigracao){
                System.out.println("Entrou aqui");
                instanteMigracao += instanteMigracao;
                remoteObjectReference.recebe("Oi gabi");
//                remoteObjectReference.recebe(this.melhoresIndividuos(qntIndividuos));
//                populacao.removeAll(this.melhoresIndividuos(qntIndividuos));
                // Espera receber individuos
//                if(individuosRecebidos.isEmpty()){
//                    System.out.println("Esta dormindo");
//                    synchronized(tr){
//                        tr.wait();
//                    }
//                }
//                populacao.addAll(this.individuosRecebidos);
//                individuosRecebidos = new ArrayList<>();
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
            geracaoAtual++;
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
    
//    @Override
//    public void recebe(ArrayList<Individuo> individuos) throws RemoteException {
//        individuosRecebidos.addAll(individuos);
//        System.out.println("Recebeu individuos");
//        tr.start();
//        
//    }
    
    public void recebe(String txt) throws RemoteException {
//        individuosRecebidos.addAll(individuos);
        System.out.println("Mensagem> "+txt);
        tr.start();
        
    }
    
    
    
    
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
            System.out.println("Erro1");
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            System.out.println("Erro2");
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            System.out.println("Erro3");
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            System.out.println("Erro4");
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }

}
