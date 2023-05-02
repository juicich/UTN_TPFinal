package ar.utn.ap.pronosticos;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class Configuracion {

    private static final String ARCHIVO_CONFIG = "src/main/config.properties";

    private static Properties propiedades;

    static {
        propiedades = new Properties();
        try {
            propiedades.load(new FileInputStream(ARCHIVO_CONFIG));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String obtenerValor(String clave) {
        return propiedades.getProperty(clave);
    }
}
