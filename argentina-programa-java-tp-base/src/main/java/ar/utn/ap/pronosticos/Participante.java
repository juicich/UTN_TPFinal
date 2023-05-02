package ar.utn.ap.pronosticos;

import java.util.ArrayList;
import java.util.List;

public class Participante {

    private String nombre;
    private List<Pronostico> pronosticos;


    private int puntosRondaCompleta;
    private int puntosFaseCompleta;

    //Constructor

    public Participante(String nombre) {
        this.nombre = nombre;
        this.pronosticos=new ArrayList<Pronostico>();
        this.puntosRondaCompleta=0;
        this.puntosFaseCompleta=0;
    }

    //Setters y Getters


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Pronostico> getPronosticos() {
        return pronosticos;
    }

    public void setPronosticos(List<Pronostico> pronosticos) {
        this.pronosticos = pronosticos;
    }

    public int getPuntosRondaCompleta() {
        return puntosRondaCompleta;
    }

    public int getPuntosFaseCompleta() {
        return puntosFaseCompleta;
    }

    public void setPuntosFaseCompleta(int puntosFaseCompleta) {
        this.puntosFaseCompleta = puntosFaseCompleta;
    }


    public void agregarPronostico(Pronostico pronostico){
        this.pronosticos.add(pronostico);
    }

    public int puntosPorPronostico(){
        int puntos=0;
        for (Pronostico pronostico:pronosticos) {
            puntos+=pronostico.puntos();
        }
        return puntos;
    }
    public void agregarPuntosRondaCompleta(int i) {
            puntosRondaCompleta+=i;
    }

    public void agregarPuntosFaseCompleta(int ii){
        puntosFaseCompleta+=ii;
    }

    public int puntosTotale(){
        return this.puntosPorPronostico()+this.puntosRondaCompleta+this.puntosFaseCompleta;
    }
}
