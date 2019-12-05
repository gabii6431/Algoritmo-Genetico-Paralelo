import java.io.Serializable;
import java.util.Locale;

/* Classe que representa um indivíduo cujo:
        (i) cromossomo é um vetor binário.
        (ii) fenótipo é um valor inteiro.
*/
public class Individuo implements Serializable {
    
    private double intervalo[];
    private int precisao;
    private int[] cromossomo;
    
    public Individuo(int[] cromossomo, double[] intervalo, int precisao) {
        this.cromossomo = cromossomo;
        this.intervalo = intervalo;
        this.precisao = precisao;
    }
    
    public Double getFenotipo() {
        int xChapeu = 0;
        double fenotipo;
        for(int i = 0; i < cromossomo.length; ++i) {
            if(cromossomo[i] == 1) {
                xChapeu += Math.pow(2,i);
            }
        }
        int n = cromossomo.length;
        Locale.setDefault(Locale.ENGLISH);
        fenotipo = intervalo[0] + 
                ((intervalo[1]-intervalo[0])/(Math.pow(2,n)-1))*xChapeu;
        fenotipo = Double.valueOf(String.format("%."+precisao+"f", fenotipo));
        return fenotipo;
    }
    
    public double[] getIntervalo() {
        return intervalo;
    }
    
    public double getAptidao() {
        double x = getFenotipo();
        return 1/(Math.pow(2,(x*Math.sin(10*x*Math.PI)+1)));
    }
    
    public double funcao(){
        double x = getFenotipo();
        return (x*Math.sin(10*x*Math.PI)+1);
    }
    
    // Método para retornar o tamanho do cromossomo.
    public int getTamanhoCromossomo() {
        return cromossomo.length;
    }
    
    // Método para clonar um indivíduo.
    public Individuo clonar() {
        int cromossomoClonado[] = new int[cromossomo.length];
        for(int i = 0; i < cromossomo.length; ++i) {
            cromossomoClonado[i] = cromossomo[i];
        }
        Individuo clone = new Individuo(cromossomoClonado, 
                intervalo,precisao);
        return clone;
    }
    
    // Método para modificar o valor de um gene no cromossomo.
    public void setGene(int pos, int valor) {
        cromossomo[pos] = valor;
    }
    
    // Retorna o valor do gene em uma determinada posição
    public int getGene(int pos) {
        return cromossomo[pos];
    }
    
    public void mostrarIndividuo() {
        System.out.print("I =");
        for(int i = 0; i < cromossomo.length; ++i) {
            System.out.print(" "+cromossomo[i]);
        }
        System.out.printf(" Fenotipo = %."+precisao+"f",getFenotipo());
        
        System.out.printf(" Aptidao = %."+precisao+"f",getAptidao());
        
        System.out.printf("f(x) = %."+precisao+"f",funcao());
        
        System.out.println();
    }
}
