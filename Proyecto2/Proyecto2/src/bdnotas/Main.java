package bdnotas;

import java.util.List;
import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);
    static AlumnoDAO alumnoDAO = new AlumnoDAO();
    static NotaDAO notaDAO = new NotaDAO();

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  SISTEMA DE CONTROL DE NOTAS - UMG");
        System.out.println("  Programacion I");
        System.out.println("========================================");

        int opcion = 0;
        do {
            mostrarMenu();
            opcion = leerEntero("Seleccione una opcion: ");
            switch (opcion) {
                case 1: ingresarAlumno(); break;
                case 2: ingresarNotas(); break;
                case 3: eliminarAlumno(); break;
                case 4: actualizarDatos(); break;
                case 5: buscarAlumno(); break;
                case 6: obtenerPromedios(); break;
                case 7: listarAlumnos(); break;
                case 8:
                    System.out.println("\nCerrando el sistema... Hasta luego!");
                    Conexion.cerrarConexion();
                    break;
                default:
                    System.out.println("Opcion no valida.");
            }
        } while (opcion != 8);
        sc.close();
    }

    static void mostrarMenu() {
        System.out.println("\n========================================");
        System.out.println("           MENU PRINCIPAL");
        System.out.println("========================================");
        System.out.println("1. Ingreso de Alumnos");
        System.out.println("2. Ingreso de Notas");
        System.out.println("3. Eliminar Alumnos");
        System.out.println("4. Actualizar datos y notas de alumnos");
        System.out.println("5. Buscar alumnos por Carnet o por Nombre");
        System.out.println("6. Obtener Promedios");
        System.out.println("7. Listar Alumnos");
        System.out.println("8. Salir");
        System.out.println("========================================");
    }

    static void ingresarAlumno() {
        System.out.println("\n--- INGRESO DE ALUMNO ---");
        System.out.print("Carnet: ");
        String carnet = sc.nextLine().trim();
        if (alumnoDAO.existeCarnet(carnet)) {
            System.out.println("Ya existe un alumno con ese carnet.");
            return;
        }
        System.out.print("Nombres: ");
        String nombres = sc.nextLine().trim();
        System.out.print("Apellidos: ");
        String apellidos = sc.nextLine().trim();
        String seccion = "";
        while (!seccion.equals("A") && !seccion.equals("B")) {
            System.out.print("Seccion (A/B): ");
            seccion = sc.nextLine().trim().toUpperCase();
        }
        if (alumnoDAO.insertarAlumno(new Alumno(carnet, nombres, apellidos, seccion))) {
            System.out.println("Alumno registrado exitosamente.");
        }
    }

    static void ingresarNotas() {
        System.out.println("\n--- INGRESO DE NOTAS ---");
        System.out.print("Carnet del alumno: ");
        String carnet = sc.nextLine().trim();
        Alumno alumno = alumnoDAO.buscarPorCarnet(carnet);
        if (alumno == null) { System.out.println("Alumno no encontrado."); return; }
        System.out.println("Alumno: " + alumno);
        if (notaDAO.tieneNota(carnet)) {
            System.out.print("Ya tiene notas. Desea sobreescribir? (S/N): ");
            if (!sc.nextLine().trim().toUpperCase().equals("S")) return;
        }
        double zona = leerDouble("Zona (0-60): ");
        while (zona < 0 || zona > 60) zona = leerDouble("Valor invalido. Zona (0-60): ");
        double examen = leerDouble("Examen (0-40): ");
        while (examen < 0 || examen > 40) examen = leerDouble("Valor invalido. Examen (0-40): ");
        if (notaDAO.tieneNota(carnet)) {
            notaDAO.actualizarNota(carnet, zona, examen);
        } else {
            notaDAO.insertarNota(new Nota(carnet, zona, examen));
        }
        System.out.println("Notas registradas. Nota final: " + (zona + examen));
    }

    static void eliminarAlumno() {
        System.out.println("\n--- ELIMINAR ALUMNO ---");
        System.out.println("1. Por Carnet  2. Por Nombre");
        int tipo = leerEntero("Seleccione: ");
        Alumno alumno = null;
        if (tipo == 1) {
            System.out.print("Carnet: ");
            alumno = alumnoDAO.buscarPorCarnet(sc.nextLine().trim());
        } else if (tipo == 2) {
            System.out.print("Nombre o apellido: ");
            List<Alumno> lista = alumnoDAO.buscarPorNombre(sc.nextLine().trim());
            if (lista.isEmpty()) { System.out.println("No se encontraron alumnos."); return; }
            for (int i = 0; i < lista.size(); i++)
                System.out.println((i + 1) + ". " + lista.get(i));
            int sel = leerEntero("Seleccione numero: ");
            if (sel < 1 || sel > lista.size()) { System.out.println("Seleccion invalida."); return; }
            alumno = lista.get(sel - 1);
        }
        if (alumno == null) { System.out.println("Alumno no encontrado."); return; }
        System.out.println("Alumno a eliminar: " + alumno);
        System.out.print("Esta seguro? (S/N): ");
        if (sc.nextLine().trim().toUpperCase().equals("S")) {
            if (alumnoDAO.eliminarAlumno(alumno.getCarnet()))
                System.out.println("Alumno eliminado correctamente.");
        } else {
            System.out.println("Operacion cancelada.");
        }
    }

    static void actualizarDatos() {
        System.out.println("\n--- ACTUALIZAR DATOS ---");
        System.out.print("Carnet del alumno: ");
        String carnet = sc.nextLine().trim();
        Alumno alumno = alumnoDAO.buscarPorCarnet(carnet);
        if (alumno == null) { System.out.println("Alumno no encontrado."); return; }
        System.out.println("Datos actuales: " + alumno);
        System.out.println("1. Nombres  2. Apellidos  3. Seccion  4. Notas  5. Todo");
        int op = leerEntero("Seleccione: ");
        switch (op) {
            case 1:
                System.out.print("Nuevos nombres: ");
                alumno.setNombres(sc.nextLine().trim());
                alumnoDAO.actualizarAlumno(alumno);
                System.out.println("Actualizado.");
                break;
            case 2:
                System.out.print("Nuevos apellidos: ");
                alumno.setApellidos(sc.nextLine().trim());
                alumnoDAO.actualizarAlumno(alumno);
                System.out.println("Actualizado.");
                break;
            case 3:
                String sec = "";
                while (!sec.equals("A") && !sec.equals("B")) {
                    System.out.print("Nueva seccion (A/B): ");
                    sec = sc.nextLine().trim().toUpperCase();
                }
                alumno.setSeccion(sec);
                alumnoDAO.actualizarAlumno(alumno);
                System.out.println("Actualizado.");
                break;
            case 4:
                actualizarNotasAlumno(carnet);
                break;
            case 5:
                System.out.print("Nuevos nombres: ");
                alumno.setNombres(sc.nextLine().trim());
                System.out.print("Nuevos apellidos: ");
                alumno.setApellidos(sc.nextLine().trim());
                String s = "";
                while (!s.equals("A") && !s.equals("B")) {
                    System.out.print("Nueva seccion (A/B): ");
                    s = sc.nextLine().trim().toUpperCase();
                }
                alumno.setSeccion(s);
                alumnoDAO.actualizarAlumno(alumno);
                actualizarNotasAlumno(carnet);
                System.out.println("Todo actualizado.");
                break;
        }
    }

    static void actualizarNotasAlumno(String carnet) {
        double zona = leerDouble("Nueva zona (0-60): ");
        while (zona < 0 || zona > 60) zona = leerDouble("Valor invalido. Zona (0-60): ");
        double examen = leerDouble("Nuevo examen (0-40): ");
        while (examen < 0 || examen > 40) examen = leerDouble("Valor invalido. Examen (0-40): ");
        if (notaDAO.tieneNota(carnet)) notaDAO.actualizarNota(carnet, zona, examen);
        else notaDAO.insertarNota(new Nota(carnet, zona, examen));
        System.out.println("Notas actualizadas. Nota final: " + (zona + examen));
    }

    static void buscarAlumno() {
        System.out.println("\n--- BUSCAR ALUMNO ---");
        System.out.println("1. Por Carnet  2. Por Nombre");
        int tipo = leerEntero("Seleccione: ");
        if (tipo == 1) {
            System.out.print("Carnet: ");
            Alumno a = alumnoDAO.buscarPorCarnet(sc.nextLine().trim());
            if (a != null) {
                System.out.println(a);
                Nota n = notaDAO.obtenerNota(a.getCarnet());
                System.out.println(n != null ? n : "Sin notas registradas.");
            } else System.out.println("No encontrado.");
        } else if (tipo == 2) {
            System.out.print("Nombre o apellido: ");
            List<Alumno> lista = alumnoDAO.buscarPorNombre(sc.nextLine().trim());
            if (lista.isEmpty()) { System.out.println("No se encontraron alumnos."); return; }
            System.out.println("------------------------------------------------------------");
            for (Alumno a : lista) {
                System.out.println(a);
                Nota n = notaDAO.obtenerNota(a.getCarnet());
                System.out.println(n != null ? "  " + n : "  Sin notas registradas.");
                System.out.println("------------------------------------------------------------");
            }
        }
    }

    static void obtenerPromedios() {
        System.out.println("\n--- PROMEDIOS POR SECCION ---");
        System.out.printf("Promedio Seccion A: %.2f%n", alumnoDAO.obtenerPromedio("A"));
        System.out.printf("Promedio Seccion B: %.2f%n", alumnoDAO.obtenerPromedio("B"));
    }

    static void listarAlumnos() {
        System.out.println("\n--- LISTAR ALUMNOS ---");
        String seccion = "";
        while (!seccion.equals("A") && !seccion.equals("B")) {
            System.out.print("Seccion (A/B): ");
            seccion = sc.nextLine().trim().toUpperCase();
        }
        System.out.println("Ordenar por: 1.Carnet  2.Nombre  3.Apellido  4.Nota  5.Sin orden");
        int op = leerEntero("Seleccione: ");
        String orden;
        switch (op) {
            case 1: orden = "carnet"; break;
            case 2: orden = "nombre"; break;
            case 3: orden = "apellido"; break;
            case 4: orden = "nota"; break;
            default: orden = "carnet";
        }
        List<Alumno> lista = alumnoDAO.listarPorSeccionOrdenado(seccion, orden);
        if (lista.isEmpty()) { System.out.println("No hay alumnos en la seccion " + seccion); return; }
        System.out.println("\n=== Programacion I - Seccion " + seccion + " ===");
        System.out.println("------------------------------------------------------------");
        System.out.printf("%-12s %-22s %-22s %-10s%n", "Carnet", "Nombres", "Apellidos", "Nota Final");
        System.out.println("------------------------------------------------------------");
        for (Alumno a : lista) {
            Nota n = notaDAO.obtenerNota(a.getCarnet());
            double nf = (n != null) ? n.getNotaFinal() : 0;
            System.out.printf("%-12s %-22s %-22s %-10.2f%n", a.getCarnet(), a.getNombres(), a.getApellidos(), nf);
        }
        System.out.println("------------------------------------------------------------");
        System.out.println("Total de alumnos: " + lista.size());
    }

    static int leerEntero(String msg) {
        while (true) {
            try { System.out.print(msg); return Integer.parseInt(sc.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("Ingrese un numero valido."); }
        }
    }

    static double leerDouble(String msg) {
        while (true) {
            try { System.out.print(msg); return Double.parseDouble(sc.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("Ingrese un numero valido."); }
        }
    }
}