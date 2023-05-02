package db;


import ar.utn.ap.pronosticos.Configuracion;


import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Conexion {

    private static Connection conexion=null;
    /*private static String nombreDB;
    private static String host="localhost";
    private static String puerto="3306";
    private static String usuarioDB="root";
    private static String contraseniaDB="root";*/

    public static Connection getConexion() throws SQLException, IOException {




        String nombreDB= Configuracion.obtenerValor("nombreDB");
        String host= Configuracion.obtenerValor("host");
        String puerto= Configuracion.obtenerValor("puerto");
        String usuarioDB= Configuracion.obtenerValor("usuarioDB");
        String contraseniaDB= Configuracion.obtenerValor("contraseniaDB");

        System.out.println("------datos del archivo de configuracion leidos correctamente------");



        if(conexion==null){
            conexion= DriverManager.getConnection("jdbc:mysql://" + host +":"+puerto +"/" +nombreDB , usuarioDB, contraseniaDB)  ;
        }
        return conexion;
    }

    public static void cerrarConexion() throws SQLException {
        if(conexion !=null && !conexion.isClosed()){
            conexion.close();
            conexion=null;
        }
    }
}
