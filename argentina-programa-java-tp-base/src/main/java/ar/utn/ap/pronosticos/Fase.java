package ar.utn.ap.pronosticos;

import java.util.ArrayList;
import java.util.List;

public class Fase {
    private Integer nroFase;
    private List<Ronda> rondas;

    //Constructor


    public Fase(Integer nroFase) {
        this.nroFase = nroFase;
        this.rondas=new ArrayList<Ronda>();
    }

    //Getters y Setters

    public Integer getNroFase() {
        return nroFase;
    }

    public void setNroFase(Integer nroFase) {
        this.nroFase = nroFase;
    }

    public List<Ronda> getRondas() {
        return this.rondas;
    }

    public void setRondas(List<Ronda> rondas) {
        this.rondas = rondas;
    }
    public void agregarRonda(Ronda ronda){
        this.rondas.add(ronda);
    }

    public boolean acertoTodosEnLaFase(List<Pronostico> apuestas) {
        int puntosDisponiblesEnFase=this.rondas.size();
        int puntosObtenidosEnFase=0;
        for (Pronostico pronostico:apuestas) {
            if(pronostico.getPartido().getRonda().getNumero().equals(this.nroFase)){
                puntosObtenidosEnFase+=pronostico.puntos();
            }
        }

        return puntosDisponiblesEnFase==puntosObtenidosEnFase;
    }
}
