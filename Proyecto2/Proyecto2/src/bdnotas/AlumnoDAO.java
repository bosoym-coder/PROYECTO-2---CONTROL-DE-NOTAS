package bdnotas;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlumnoDAO {

    private Connection con;

    public AlumnoDAO() {
        this.con = Conexion.getConexion();
    }

    public boolean insertarAlumno(Alumno alumno) {
        String sql = "INSERT INTO alumnos (carnet, nombres, apellidos, seccion) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, alumno.getCarnet());
            ps.setString(2, alumno.getNombres());
            ps.setString(3, alumno.getApellidos());
            ps.setString(4, alumno.getSeccion());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al insertar alumno: " + e.getMessage());
            return false;
        }
    }

    public boolean existeCarnet(String carnet) {
        String sql = "SELECT carnet FROM alumnos WHERE carnet = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, carnet);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean eliminarAlumno(String carnet) {
        String sql = "DELETE FROM alumnos WHERE carnet = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, carnet);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarAlumno(Alumno alumno) {
        String sql = "UPDATE alumnos SET nombres = ?, apellidos = ?, seccion = ? WHERE carnet = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, alumno.getNombres());
            ps.setString(2, alumno.getApellidos());
            ps.setString(3, alumno.getSeccion());
            ps.setString(4, alumno.getCarnet());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar: " + e.getMessage());
            return false;
        }
    }

    public Alumno buscarPorCarnet(String carnet) {
        String sql = "SELECT * FROM alumnos WHERE carnet = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, carnet);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Alumno(rs.getString("carnet"), rs.getString("nombres"),
                                  rs.getString("apellidos"), rs.getString("seccion"));
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar: " + e.getMessage());
        }
        return null;
    }

    public List<Alumno> buscarPorNombre(String nombre) {
        List<Alumno> lista = new ArrayList<>();
        String sql = "SELECT * FROM alumnos WHERE nombres LIKE ? OR apellidos LIKE ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%" + nombre + "%");
            ps.setString(2, "%" + nombre + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Alumno(rs.getString("carnet"), rs.getString("nombres"),
                                     rs.getString("apellidos"), rs.getString("seccion")));
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar por nombre: " + e.getMessage());
        }
        return lista;
    }

    public List<Alumno> listarPorSeccionOrdenado(String seccion, String orden) {
        List<Alumno> lista = new ArrayList<>();
        String columna;
        switch (orden.toLowerCase()) {
            case "carnet":   columna = "a.carnet"; break;
            case "nombre":   columna = "a.nombres"; break;
            case "apellido": columna = "a.apellidos"; break;
            case "nota":     columna = "COALESCE(n.nota_final, 0)"; break;
            default:         columna = "a.carnet";
        }
        String sql = "SELECT a.* FROM alumnos a LEFT JOIN notas n ON a.carnet = n.carnet "
                   + "WHERE a.seccion = ? ORDER BY " + columna;
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, seccion);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Alumno(rs.getString("carnet"), rs.getString("nombres"),
                                     rs.getString("apellidos"), rs.getString("seccion")));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar: " + e.getMessage());
        }
        return lista;
    }

    public double obtenerPromedio(String seccion) {
        String sql = "SELECT AVG(n.nota_final) AS promedio FROM notas n "
                   + "INNER JOIN alumnos a ON n.carnet = a.carnet WHERE a.seccion = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, seccion);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble("promedio");
        } catch (SQLException e) {
            System.out.println("Error al obtener promedio: " + e.getMessage());
        }
        return 0;
    }
}