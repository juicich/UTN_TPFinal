package ar.utn.ap.pronosticos;

public class Pronostico {
	private Partido partido;
	private Equipo equipo;
	private EnumResultado resultado;

	int puntosPartido=Integer.parseInt(Configuracion.obtenerValor("puntosPartido"));
        
        //Constructores

              
	public Pronostico(Partido partido, Equipo equipo, EnumResultado resultado) {
		super();
		this.partido = partido;
		this.equipo = equipo;
		this.resultado = resultado;
	}

        // Setters y Getters
        
        

        public Partido getPartido() {
        return this.partido;
    }

	public Equipo getEquipo() {
		return this.equipo;
	}

	public EnumResultado getResultado() {
		return this.resultado;
	}


	public int puntos() {
		// this.resultado -> pred
		EnumResultado resultadoReal = this.partido.resultado(this.equipo);
		if (this.resultado.equals(resultadoReal)) {
			return puntosPartido;
		} else {
			return 0;
		}

	}

}
