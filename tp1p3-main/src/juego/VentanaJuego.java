package juego;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import javax.swing.InputMap;
import javax.swing.ActionMap;
import javax.swing.KeyStroke;
import javax.swing.AbstractAction;
import javax.swing.JComponent;

public class VentanaJuego extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel[][] celdas;
	private Juego juego;
	private GestorPuntajes gestorPuntajes = new GestorPuntajes();
	private final JButton botonArr = new JButton("▲");
	private final JButton botonAb = new JButton("▼");
	private final JButton botonIzq = new JButton("◄");
	private final JButton botonDer = new JButton("►");
	private final JButton botonDeshacer = new JButton("Deshacer");
	private final JButton botonSugerir = new JButton("Sugerir Jugada");
	private final JButton botonReiniciar = new JButton("Reiniciar Partida");
	private final JLabel lblPuntos = new JLabel("");
	private final JLabel lblProxFicha = new JLabel("");
	private final JButton btnPuntajes = new JButton("Ver Puntajes");
	
	private void actualizarTablero() {
	    int size = juego.getSize();
	    for (int f = 0; f < size; f++) {
	        for (int c = 0; c < size; c++) {
	            int valor = juego.getValor(f, c);
	            JLabel lbl = celdas[f][c];
	            lbl.setText(valor == 0 ? "" : String.valueOf(valor));
	            lbl.setBackground(colorSegunValor(valor));
	        }
	    }
	    lblPuntos.setText("Puntos: " + juego.getPuntos());
	    lblProxFicha.setText("Proxima: " + juego.getProximaFicha());
	}

	private Color colorSegunValor(int valor) {
	    switch (valor) {
	        case 0: return Color.LIGHT_GRAY;
	        case 1: return new Color(173, 216, 230);  // celeste
	        case 2: return new Color(255, 182, 193);  // rosa
	        default: return new Color(255, 228, 150); // beige para 3, 6, 9...
	    }
	}
	
	private void finalizarJuego() {
	    String nombre = JOptionPane.showInputDialog(this,
	            "Fin del juego! Puntaje: " + juego.getPuntos() + "\nIngresá tu nombre:");
	    if (nombre != null && !nombre.trim().isEmpty()) {
	        gestorPuntajes.guardarPuntaje(nombre.trim(), juego.getPuntos());
	    }
	}
	
	private void mostrarPuntajes() {
		List<String[]> puntajes = gestorPuntajes.cargarPuntajes();
	    VentanaPuntajes ventanaPuntajes = new VentanaPuntajes(this, puntajes);
	    ventanaPuntajes.setVisible(true);
	}
	
	private void procesarMovimiento(boolean seMovio) {
	    actualizarTablero();
	    if (juego.terminado()) {
	        finalizarJuego();
	    }
	}
	
	private void deshacerJugada() {
	    if (juego.deshacer()) {
	        actualizarTablero();
	    } else {
	        JOptionPane.showMessageDialog(this, "No hay jugadas para deshacer");
	    }
	}

	private void reiniciarJuego() {
	    juego.reiniciar();
	    actualizarTablero();
	}

	private void sugerirJugada() {
	    Direccion sugerencia = juego.jugadaSugerida();
	    if (sugerencia != null) {
	        JOptionPane.showMessageDialog(this, "Te sugiero mover hacia: " + sugerencia);
	    } else {
	        JOptionPane.showMessageDialog(this, "No hay jugadas que te sumen puntos ahora mismo");
	    }
	}
	

	public static void main(String[] args) {
		try {
	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaJuego frame = new VentanaJuego();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private void configurarTeclado() {
	    InputMap inputMap = contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
	    ActionMap actionMap = contentPane.getActionMap();

	    inputMap.put(KeyStroke.getKeyStroke("UP"), "moverArriba");
	    inputMap.put(KeyStroke.getKeyStroke("W"), "moverArriba");
	    actionMap.put("moverArriba", new AbstractAction() {
	    	private static final long serialVersionUID = 1L;  //para q no salte la alerta del AbstractAction
	        public void actionPerformed(ActionEvent e) {
	            procesarMovimiento(juego.moverArriba());
	        }
	    });

	    inputMap.put(KeyStroke.getKeyStroke("DOWN"), "moverAbajo");
	    inputMap.put(KeyStroke.getKeyStroke("S"), "moverAbajo");
	    actionMap.put("moverAbajo", new AbstractAction() {
	    	private static final long serialVersionUID = 1L;
	        public void actionPerformed(ActionEvent e) {
	            procesarMovimiento(juego.moverAbajo());
	        }
	    });

	    inputMap.put(KeyStroke.getKeyStroke("LEFT"), "moverIzquierda");
	    inputMap.put(KeyStroke.getKeyStroke("A"), "moverIzquierda");
	    actionMap.put("moverIzquierda", new AbstractAction() {
	    	private static final long serialVersionUID = 1L;
	        public void actionPerformed(ActionEvent e) {
	            procesarMovimiento(juego.moverIzquierda());
	        }
	    });

	    inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "moverDerecha");
	    inputMap.put(KeyStroke.getKeyStroke("D"), "moverDerecha");
	    actionMap.put("moverDerecha", new AbstractAction() {
	    	private static final long serialVersionUID = 1L;
	        public void actionPerformed(ActionEvent e) {
	            procesarMovimiento(juego.moverDerecha());
	        }
	    });
	}

	public VentanaJuego() {
		juego = new Juego();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panelNorte = new JPanel();
		contentPane.add(panelNorte, BorderLayout.NORTH);
		btnPuntajes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mostrarPuntajes();
			}
		});
		panelNorte.add(btnPuntajes);
		panelNorte.add(lblPuntos);
		panelNorte.add(lblProxFicha);
		panelNorte.add(botonReiniciar);
		botonReiniciar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reiniciarJuego();
			}
		});
		
		JPanel panelTablero = new JPanel();
		contentPane.add(panelTablero, BorderLayout.CENTER);
		panelTablero.setLayout(new GridLayout(4, 4, 0, 0));
		
		JPanel panelSur = new JPanel();
		panelSur.setPreferredSize(new Dimension(450, 100));
		contentPane.add(panelSur, BorderLayout.SOUTH);
		panelSur.setLayout(new GridLayout(2, 3, 0, 0));
		panelSur.add(botonSugerir);
		botonSugerir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				sugerirJugada();
			}
		});
		panelSur.add(botonArr);
		botonArr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				procesarMovimiento(juego.moverArriba());
			}
		});
		panelSur.add(botonDeshacer);
		botonDeshacer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deshacerJugada();
			}
		});
		panelSur.add(botonIzq);
		botonIzq.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				procesarMovimiento(juego.moverIzquierda());
			}
		});
		panelSur.add(botonAb);
		botonAb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				procesarMovimiento(juego.moverAbajo());
			}
		});
		panelSur.add(botonDer);
		botonDer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				procesarMovimiento(juego.moverDerecha());
					}
				});
		
		int size = juego.getSize(); 
		panelTablero.setLayout(new GridLayout(size, size, 5, 5)); // 5,5 = espacio entre celdas
		celdas = new JLabel[size][size];

		for (int f = 0; f < size; f++) {
		    for (int c = 0; c < size; c++) {
		        JLabel lbl = new JLabel("", SwingConstants.CENTER);
		        lbl.setOpaque(true); // sin esto, setBackground() no se ve
		        lbl.setBackground(Color.LIGHT_GRAY);
		        celdas[f][c] = lbl;
		        panelTablero.add(lbl);
		    }
		}
		
		actualizarTablero();
	    configurarTeclado();
	}
		
	}


