package juego;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

public class VentanaPuntajes extends JDialog {

	private static final long serialVersionUID = 1L;

	public VentanaPuntajes(Frame propietario, List<String[]> puntajes) {
        super(propietario, "Mejores Puntajes", true);
        setSize(350, 400);
        setLocationRelativeTo(propietario);
        getContentPane().setLayout(new BorderLayout());

        String[] columnas = {"Puesto", "Nombre", "Puntos"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {

			private static final long serialVersionUID = 1L;

			@Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        int puesto = 1;
        for (String[] p : puntajes) {
            modelo.addRow(new Object[]{puesto++, p[0], p[1]});
        }

        JTable tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);
        getContentPane().add(scroll, BorderLayout.CENTER);
    }
}