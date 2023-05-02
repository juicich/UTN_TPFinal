package ar.utn.ap.pronosticos;

import db.Conexion;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainTP {

	static Collection<Partido>partidos;
	static Map<Integer, Ronda>rondas;
	static Map<Integer, Fase> fases;
	static Map<String, Participante> participantes;

	public static void main(String[] args) throws SQLException, IOException {


		// Lee archivo de resultados


		leerArchivoResultados(args);

		//Lee los pronosticos y crea la coleccion de partidos
		crearPronosticos();


		//Puntos extras por rondas y fases

		int puntosExtraPorRonda=Integer.parseInt(Configuracion.obtenerValor("puntosExtraPorRonda"));
		int puntosExtraPorFase=Integer.parseInt(Configuracion.obtenerValor("puntosExtraPorFase"));


		for(String apostador:participantes.keySet()){

			Participante participante=participantes.get(apostador);                        //traer participante

			for (Integer nroRonda: rondas.keySet()){
				Ronda ronda=rondas.get(nroRonda);                                            //traer ronda
				List<Pronostico> apuestas= participantes.get(apostador).getPronosticos();    //traer los pronosticos del participante
				boolean cumplioRonda=ronda.acertoTodos(apuestas);                            //ver si cumple con el acierto de todos los partidos
				if (cumplioRonda){                                                            //si cumple con todos los aciertos agregar el bono
					participante.agregarPuntosRondaCompleta(puntosExtraPorRonda);
				}
			}

			for (Integer nroFase:fases.keySet()){
				boolean faseCompleta = true;
				int i = 0;
				Fase fase=fases.get(nroFase);

				List<Ronda> rondasFase = fase.getRondas();
				int largoRondas = rondasFase.size();

				while (faseCompleta == true && i < largoRondas){
					Ronda rondaFase = rondasFase.get(i);
					faseCompleta = rondaFase.getAcertoTodos();
					i++;
				}

				if (faseCompleta){
					participante.agregarPuntosFaseCompleta(puntosExtraPorFase);

				}
			}


			// mostrar los puntos


		}


		for(String apostador:participantes.keySet()){
			System.out.println("---------------------------------------------"+ apostador +"----------------------------------------------------------");

			System.out.println("los puntos obtenidos por partidos fueron :"+ participantes.get(apostador).puntosPorPronostico());
			System.out.println("los puntos extras obtenidos por rondas fueron :" + participantes.get(apostador).getPuntosRondaCompleta());
			System.out.println("los puntos extras obtenidos por fase completa fueron :" + participantes.get(apostador).getPuntosFaseCompleta());
			System.out.println("Los puntos TOTALES obtenidos fueron :"+ participantes.get(apostador).puntosTotale());
			System.out.println("-------------------------------------------------------------------------------------------------------------");


		}

	}

	private static void crearPronosticos() throws SQLException, IOException {
		participantes=new HashMap<>();


		Connection conexion= Conexion.getConexion();
		PreparedStatement seleccionDatosDB= conexion.prepareStatement("select * from pronosticos");
		ResultSet datosDB=seleccionDatosDB.executeQuery();

		while(datosDB.next()){

			Equipo equipo1 = new Equipo(datosDB.getString("Equipo1"));
			Equipo equipo2 = new Equipo(datosDB.getString("Equipo2"));
			Partido partido = null;

			for (Partido partidoCol : partidos) {
				if (partidoCol.getEquipo1().getNombre().equals(equipo1.getNombre()) && partidoCol.getEquipo2().getNombre().equals(equipo2.getNombre())) {
					partido = partidoCol;
				}
			}
			Equipo equipo = null;
			EnumResultado resultado = null;
			if("X".equals(datosDB.getString("Gana1"))) {
				equipo = equipo1;
				resultado = EnumResultado.GANADOR;
			}
			if("X".equals(datosDB.getString("Empata"))) {
				equipo = equipo1;
				resultado = EnumResultado.EMPATE;
			}
			if("X".equals(datosDB.getString("Gana2"))) {
				equipo = equipo1;
				resultado = EnumResultado.PERDEDOR;
			}


			Pronostico pronostico = new Pronostico(partido, equipo, resultado);

			String nombreParticipante=datosDB.getString("Participante");

			Participante participante=null;

			if(participantes.containsKey(nombreParticipante)){
				participante=participantes.get(nombreParticipante);

			}else{
				participante=new Participante(nombreParticipante);
				participantes.put(nombreParticipante,participante);


			}
			participante.agregarPronostico(pronostico);

		}
		Conexion.cerrarConexion();
	}

	private static void leerArchivoResultados(String[] args) {
		partidos = new ArrayList<Partido>();

		Path pathResultados = Paths.get(args[0]);
		List<String> lineasResultados = null;

		// Definimos la expresión regular del archivo de resultados.csv
		String patron = "^[a-zA-Z ]+,[0-9]+,[0-9]+,[a-zA-Z ]+,[0-9]+,[0-9]+$";
		// Se establece el patrón de comparacion
		Pattern p = Pattern.compile(patron);

		rondas = new HashMap<>();
		fases=new HashMap<>();


		try {
			lineasResultados = Files.readAllLines(pathResultados);
		} catch (IOException e) {
			System.out.println("No se pudo leer la linea de resultados...");
			System.out.println(e.getMessage());
			System.exit(1);
		}
		boolean primera = true;
		for (String lineaResultado : lineasResultados) {
			if (primera) {

				primera = false;
			} else {

				// Lee el archivo de resultados y arma la coleccion de partidos

				Matcher matcher = p.matcher(lineaResultado);

				if (!matcher.matches()) {
					System.out.println("Línea inválida: " + lineaResultado);
				}

				String[] campos = lineaResultado.split(",");

				Equipo equipo1 = new Equipo(campos[0]);
				Equipo equipo2 = new Equipo(campos[3]);
				Partido partido = new Partido(equipo1, equipo2);
				partido.setGolesEq1(Integer.parseInt(campos[1]));
				partido.setGolesEq2(Integer.parseInt(campos[2]));

				int nroRonda = Integer.parseInt(campos[4]);
				Ronda ronda = null;
				if (!rondas.containsKey(nroRonda)) {

					ronda = new Ronda(nroRonda);
					rondas.put(nroRonda, ronda);

				} else {
					ronda = rondas.get(nroRonda);

				}

				int nroFase=Integer.parseInt(campos[5]);
				Fase fase=null;
				if(!fases.containsKey(nroFase)) {

					fase=new Fase(nroFase);
					fases.put(nroFase,fase);
				} else{
					fase=fases.get(nroFase);
				}
				ronda.agregarPartido(partido);
				partido.setRonda(ronda);
				partido.setNroFase(nroFase);
				fase.agregarRonda(ronda);


				partidos.add(partido);
			}

		}
	}
}