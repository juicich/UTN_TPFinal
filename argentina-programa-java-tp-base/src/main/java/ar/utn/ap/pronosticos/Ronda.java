package ar.utn.ap.pronosticos;

import java.util.ArrayList;
import java.util.List;

public class Ronda {
    private Integer numero;
    private List<Partido> partidos;
    private boolean acertoTodos = false;


    //Constructor

    public Ronda(Integer numero) {

        this.numero = numero;
        this.partidos=new ArrayList<Partido>();
    }

    //Getter y Setter


    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public List<Partido> getPartidos() {
        return partidos;
    }

    public void setPartidos(List<Partido> partidos) {
        this.partidos = partidos;
    }
    public void agregarPartido(Partido partido){
        this.partidos.add(partido);
    }

    public boolean acertoTodos(List<Pronostico> apuestas) {
        int puntosDisponiblesEnRonda=this.partidos.size();
        int puntosObtenidosEnRonda=0;
        for (Pronostico pronostico:apuestas) {
            if(pronostico.getPartido().getRonda().getNumero().equals(this.numero)){
                puntosObtenidosEnRonda+=pronostico.puntos();
            }
        }
        this.acertoTodos = puntosDisponiblesEnRonda==puntosObtenidosEnRonda ? true : false;

        return this.acertoTodos;
    }

    public void setAcertoTodos (boolean acerto) {
        this.acertoTodos = acerto;
    }

    public boolean getAcertoTodos() {
        return this.acertoTodos;
    }
}
