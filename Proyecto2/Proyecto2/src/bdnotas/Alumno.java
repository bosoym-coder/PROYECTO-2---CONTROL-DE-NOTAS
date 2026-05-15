package bdnotas;

public class Alumno {

    private String carnet;
    private String nombres;
    private String apellidos;
    private String seccion;

    public Alumno() {}

    public Alumno(String carnet, String nombres, String apellidos, String seccion) {
        this.carnet = carnet;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.seccion = seccion;
    }

    public String getCarnet() { return carnet; }
    public void setCarnet(String carnet) { this.carnet = carnet; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getSeccion() { return seccion; }
    public void setSeccion(String seccion) { this.seccion = seccion; }

    @Override
    public String toString() {
        return "Carnet: " + carnet + " | Nombre: " + nombres + " " + apellidos + " | Seccion: " + seccion;
    }
}