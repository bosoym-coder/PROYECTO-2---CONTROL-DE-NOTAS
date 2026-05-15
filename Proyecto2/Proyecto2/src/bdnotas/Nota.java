package bdnotas;

public class Nota {

    private int id;
    private String carnet;
    private double zona;
    private double examen;
    private double notaFinal;

    public Nota() {}

    public Nota(String carnet, double zona, double examen) {
        this.carnet = carnet;
        this.zona = zona;
        this.examen = examen;
        this.notaFinal = zona + examen;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCarnet() { return carnet; }
    public void setCarnet(String carnet) { this.carnet = carnet; }

    public double getZona() { return zona; }
    public void setZona(double zona) { this.zona = zona; this.notaFinal = this.zona + this.examen; }

    public double getExamen() { return examen; }
    public void setExamen(double examen) { this.examen = examen; this.notaFinal = this.zona + this.examen; }

    public double getNotaFinal() { return notaFinal; }
    public void setNotaFinal(double notaFinal) { this.notaFinal = notaFinal; }

    @Override
    public String toString() {
        return "Zona: " + zona + " | Examen: " + examen + " | Nota Final: " + notaFinal;
    }
}