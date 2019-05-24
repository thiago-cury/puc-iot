package thiagocury.eti.br.excontroletemperatura;

/**
 * Created by thiagocury on 09/01/2018.
 */

public class Temperatura {

    private String key;
    private double temperatura;
    private double umidade;

    public Temperatura() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(double temperatura) {
        this.temperatura = temperatura;
    }

    public double getUmidade() {
        return umidade;
    }

    public void setUmidade(double umidade) {
        this.umidade = umidade;
    }

    @Override
    public String toString() {
        return "Temperatura = " + temperatura +
                "C\nUmidade = " + umidade+"%";
    }
}
