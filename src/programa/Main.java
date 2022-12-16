package programa;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Morad
 */
public class Main {

    public static void main(String[] args) throws SQLException {

        Connection conn = null; //objeto para hacer conexion
        conn = conexion();//conectamos con la ayuda del metodo conexion

        if (conn != null) {
            System.out.println("conectado correctamente");

            Statement st = conn.createStatement();

            //sentencia para crear la nueva tabla
            String cadena = "CREATE TABLE IF NOT EXISTS PersonasPaises("
                    + " Id int primary key AUTO_INCREMENT,"
                    + " Nombre Varchar(15),"
                    + " Apellido varchar(15),"
                    + " Edad tinyint,"
                    + " NombrePais Varchar(25),"
                    + " Tamanio Varchar(15));";

            //ejecutamos la secuencia
            st.execute(cadena);
            
            //sentencia para sacar los datos de las tablas, se utilizan alias
            cadena = "select p.id,p.nombre,p.apellido,p.edad,pa.nombre as pais ,pa.tamanio"
                    + " from Persona AS p,Pais AS pa where p.pais=pa.id;";

            ResultSet rs = st.executeQuery(cadena);

            int id;
            String nombre;
            String apellido;
            String tamanio;
            String pais;
            int edad;

            Statement st1 = conn.createStatement();

            //en el bucle guardamos en las variables obtenidas por el resultSet
            while (rs.next()) {
                id = rs.getInt("Id");
                nombre = rs.getString("Nombre");
                apellido = rs.getString("Apellido");
                edad = rs.getInt("Edad");
                pais = rs.getString("Pais");
                tamanio = rs.getString("Tamanio");

                System.out.println("Nombre: " + nombre + "  Apellido: " + apellido + "  Edad: " + edad + "  Pais; " + pais + "  Tamanio: " + tamanio);

                //sumamos 1 a los paises de costa rica y lo guardamos en la nueva tabla
                if (pais.equals("Costa Rica")) {
                    edad++;
                }
                
                //por cada ciclo que se encuentra un dato se guarda con un insert en la tabla PersonaPaises
                cadena = "Insert into PersonasPaises (Nombre,Apellido,Edad,NombrePais,Tamanio)values"
                        + "('" + nombre + "','" + apellido + "'," + edad + ",'" + pais + "','" + tamanio + "');";
                st1.executeUpdate(cadena);
            }

            //cerramos la conexion, llamando al metodo
            cerrarConexion(conn);

        } else {
            System.out.println("no se ha podido conectar");
        }

    }

    //metodo para contectar bbdd
    public static Connection conexion() {

        String bbdd = "jdbc:mysql://localhost/america";//se usa la base de datos america,facilitada por el script
        String usuario = "root";
        String clave = "";
        Connection conn = null;

        try {

            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(bbdd, usuario, clave);

        } catch (Exception ex) {
            System.out.println("Error al conectar con la base de datos.\n"
                    + ex.getMessage().toString());
        }

        return conn;
    }

    //metodo para cerrar la conexion a la bbdd
    public static void cerrarConexion(Connection conn) {
        try {
            conn.close();
        } catch (SQLException e) {
            System.out.println("la conexion no se ha cerrado");
            System.out.println(e.getMessage().toString());
        }
    }

}
