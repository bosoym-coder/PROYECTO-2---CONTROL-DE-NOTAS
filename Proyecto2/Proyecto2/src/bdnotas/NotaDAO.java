package bdnotas;

import java.sql.*;

public class NotaDAO {

    private Connection con;

    public NotaDAO() {
        this.con = Conexion.getConexion();
    }

    public boolean insertarNota(Nota nota) {
        String sql = "INSERT INTO notas (carnet, zona, examen, nota_final) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nota.getCarnet());
            ps.setDouble(2, nota.getZona());
            ps.setDouble(3, nota.getExamen());
            ps.setDouble(4, nota.getNotaFinal());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al insertar nota: " + e.getMessage());
            return false;
        }
    }

    public boolean tieneNota(String carnet) {
        String sql = "SELECT id FROM notas WHERE carnet = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, carnet);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            return false;
        }
    }

    public Nota obtenerNota(String carnet) {
        String sql = "SELECT * FROM notas WHERE carnet = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, carnet);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Nota nota = new Nota();
                nota.setId(rs.getInt("id"));
                nota.setCarnet(rs.getString("carnet"));
                nota.setZona(rs.getDouble("zona"));
                nota.setExamen(rs.getDouble("examen"));
                nota.setNotaFinal(rs.getDouble("nota_final"));
                return nota;
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener nota: " + e.getMessage());
        }
        return null;
    }

    public boolean actualizarNota(String carnet, double zona, double examen) {
        String sql = "UPDATE notas SET zona = ?, examen = ?, nota_final = ? WHERE carnet = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setDouble(1, zona);
            ps.setDouble(2, examen);
            ps.setDouble(3, zona + examen);
            ps.setString(4, carnet);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar nota: " + e.getMessage());
            return false;
        }
    }
}