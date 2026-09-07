package juego;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class GestorPuntajes {
	
	private static final String ARCHIVO = "puntajes.txt";
    private static final int MAX_PUNTAJES = 10;
    
    public void guardarPuntaje(String nombre, int puntos) {
        List<String[]> puntajes = cargarPuntajes();
        puntajes.add(new String[]{nombre, String.valueOf(puntos)});

        puntajes.sort((a, b) -> Integer.parseInt(b[1]) - Integer.parseInt(a[1]));

        if (puntajes.size() > MAX_PUNTAJES) {
            puntajes = puntajes.subList(0, MAX_PUNTAJES);
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO))) {
            for (String[] p : puntajes) {
                pw.println(p[0] + ";" + p[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String[]> cargarPuntajes() {
        List<String[]> puntajes = new ArrayList<>();
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) {
            return puntajes;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length == 2) {
                    puntajes.add(partes);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return puntajes;
    }

}
