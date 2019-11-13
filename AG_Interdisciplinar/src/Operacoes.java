
import java.util.ArrayList;
import java.util.Random;

// Classe com as operações de Algoritmos Genéticos
public class Operacoes {
    
    // Efetua o crossover entre dois indivíduos
    public IndividuoBinario[] crossover(
            IndividuoBinario i1,
            IndividuoBinario i2) {
        
        int tam = i1.getTamanhoCromossomo();
        
        Random r = new Random();
        int pontoCorte = r.nextInt(tam);
        
        IndividuoBinario ni1 = i1.clonar();
        IndividuoBinario ni2 = i2.clonar();
        
        for(int i = pontoCorte; i < tam; ++i) {
            ni1.setGene(i, i2.getGene(i));
            ni2.setGene(i, i1.getGene(i));
        }
        
        return new IndividuoBinario[]{ni1, ni2};
        
    }
    
    // Método para efetuar a mutação sobre um indivíduo
    // o parâmetro probabilidade assume um valor [0,100]
    public void mutacao(IndividuoBinario individuo,
            int probabilidade) {
        
        Random r = new Random();
        
        int valorAleatorio = r.nextInt(101);
        if(probabilidade >= valorAleatorio) {
            int tam = individuo.getTamanhoCromossomo();
            int pos = r.nextInt(tam);
            int gene = individuo.getGene(pos);
            if(gene == 1) {
                individuo.setGene(pos, 0);
            } else {
                individuo.setGene(pos, 1);
            }

        }
        
    }
    
    // Método da Roleta para determinar os indivíduos mais aptos
    public Individuo roleta(ArrayList<Individuo> populacao) {
        double aptidaoTotal = 0.0;
        // Soma todas as aptidões...
        for(Individuo i:populacao) {
            aptidaoTotal += i.getAptidao();
        }
        
        Random r = new Random();
        
        double valorAleatorio = r.nextDouble()*aptidaoTotal;
        
        double aptidaoAcumulada = 0.0;
        
        for(Individuo i:populacao) {
            aptidaoAcumulada += i.getAptidao();
            if(valorAleatorio <= aptidaoAcumulada) {
                return i;
            }
        }
        
        return null;
        
    }
    
    public Individuo torneio(ArrayList<Individuo> populacao){
        int n = 3;
        Random r = new Random();
        int numeroSorteado[] = new int[n];
        int aux = 0;
        int index = 0;
        double maior = Double.MIN_VALUE;
        numeroSorteado[0] = r.nextInt(populacao.size());
        
        
        for (int i = 1; i < n; i++) {            
            do{
                aux = r.nextInt(populacao.size());
            }while(aux == numeroSorteado[i-1]);
            numeroSorteado[i] = aux;
        }
        
        for (int i = 0; i < n; i++) {
            if(populacao.get(numeroSorteado[i]).getAptidao() > maior){
                maior = populacao.get(numeroSorteado[i]).getAptidao();
                index = numeroSorteado[i];
            }
        }
//        System.out.println("Selecionados: ");;
//        for (int i = 0; i < n; i++) {
//            populacao.get(numeroSorteado[i]).mostrarIndividuo();
//        }
//        
//        populacao.get(index).mostrarIndividuo();
        return populacao.get(index);
    }
    
}
