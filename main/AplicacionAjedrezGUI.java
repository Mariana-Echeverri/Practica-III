package main;

import analizador.AnalizadorSintactico;
import analizador.AnalizadorGramatico;
import modelo.ResultadoAnalisis;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.*;

/**
 * Aplicación de ajedrez con interfaz gráfica que permite analizar partidas
 */
public class AplicacionAjedrezGUI {
    // Ventana principal
    private JFrame ventana;

    // Componentes para login
    private JTextField campoUsuario;
    private JPasswordField campoContraseña;

    // Componentes para análisis
    private JTextArea areaPartida;
    private JTextArea areaResultados;

    /**
     * Método principal para iniciar la aplicación
     */
    public static void main(String[] args) {
        // Crear e iniciar la interfaz gráfica
        AplicacionAjedrezGUI app = new AplicacionAjedrezGUI();
        app.iniciar();
    }

    /**
     * Inicia la aplicación y muestra la ventana principal
     */
    public void iniciar() {
        // Configurar el aspecto visual (Look & Feel)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("No se pudo establecer el aspecto visual del sistema");
        }

        // Crear la ventana principal
        ventana = new JFrame("Analizador de Ajedrez");
        ventana.setSize(800, 600);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setBackground(new Color(240, 217, 181));

        // Mostrar la pantalla de login
        mostrarPantallaLogin();

        // Mostrar la ventana centrada en la pantalla
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
    }

    /**
     * Muestra la pantalla de login
     */
    private void mostrarPantallaLogin() {
        // Limpiar ventana
        ventana.getContentPane().removeAll();
        ventana.setTitle("Analizador de Ajedrez - Inicio de Sesión");

        // Panel principal con diseño absoluto
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(240, 217, 181));

        // Añadir título
        JLabel titulo = new JLabel("ANALIZADOR DE PARTIDAS DE AJEDREZ");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setBounds(220, 50, 400, 30);
        panel.add(titulo);

        // Añadir imagen de ajedrez (representada por un panel)
        JPanel imagenAjedrez = new JPanel();
        imagenAjedrez.setBackground(new Color(139, 69, 19));
        imagenAjedrez.setBounds(350, 100, 100, 100);
        JLabel iconoAjedrez = new JLabel("♟️");
        iconoAjedrez.setFont(new Font("Dialog", Font.PLAIN, 48));
        iconoAjedrez.setForeground(Color.WHITE);
        imagenAjedrez.add(iconoAjedrez);
        panel.add(imagenAjedrez);

        // Crear panel de login
        JPanel panelLogin = new JPanel(null);
        panelLogin.setBounds(200, 220, 400, 250);
        panelLogin.setBackground(new Color(222, 184, 135));
        panelLogin.setBorder(BorderFactory.createTitledBorder("Iniciar Sesión"));

        // Etiqueta y campo de usuario
        JLabel etiquetaUsuario = new JLabel("Usuario:");
        etiquetaUsuario.setBounds(50, 50, 100, 25);
        panelLogin.add(etiquetaUsuario);

        campoUsuario = new JTextField();
        campoUsuario.setBounds(150, 50, 200, 25);
        panelLogin.add(campoUsuario);

        // Etiqueta y campo de contraseña
        JLabel etiquetaContraseña = new JLabel("Contraseña:");
        etiquetaContraseña.setBounds(50, 90, 100, 25);
        panelLogin.add(etiquetaContraseña);

        campoContraseña = new JPasswordField();
        campoContraseña.setBounds(150, 90, 200, 25);
        panelLogin.add(campoContraseña);

        // Botón de ingreso
        JButton botonEntrar = new JButton("Entrar");
        botonEntrar.setBounds(150, 140, 100, 30);
        botonEntrar.setBackground(new Color(139, 69, 19));
        botonEntrar.setForeground(Color.WHITE);
        botonEntrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                validarLogin();
            }
        });
        panelLogin.add(botonEntrar);

        // Instrucciones de formato
        JLabel instrucciones = new JLabel("<html>Usuario: 3-15 caracteres (letras, números y _)<br>" +
                "Contraseña: mínimo 6 caracteres (letras y números)</html>");
        instrucciones.setFont(new Font("Arial", Font.ITALIC, 12));
        instrucciones.setBounds(50, 180, 300, 40);
        panelLogin.add(instrucciones);

        // Añadir panel de login al panel principal
        panel.add(panelLogin);

        // Añadir panel principal a la ventana
        ventana.getContentPane().add(panel);
        ventana.revalidate();
        ventana.repaint();
    }

    /**
     * Valida las credenciales de inicio de sesión
     */
    private void validarLogin() {
        String usuario = campoUsuario.getText();
        String contraseña = new String(campoContraseña.getPassword());

        // Validar formato de usuario (3-15 caracteres alfanuméricos o guión bajo)
        boolean usuarioValido = Pattern.matches("[a-zA-Z0-9_]{3,15}", usuario);

        // Validar formato de contraseña (mínimo 6 caracteres, con letras y números)
        boolean contraseñaValida = contraseña.length() >= 6 &&
                contraseña.matches(".*[A-Za-z].*") &&
                contraseña.matches(".*\\d.*");

        // Mostrar errores si los hay
        if (!usuarioValido) {
            JOptionPane.showMessageDialog(ventana,
                    "El usuario debe tener entre 3 y 15 caracteres (letras, números o guión bajo).",
                    "Error de formato", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!contraseñaValida) {
            JOptionPane.showMessageDialog(ventana,
                    "La contraseña debe tener al menos 6 caracteres e incluir letras y números.",
                    "Error de formato", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Si las credenciales son válidas, mostrar la pantalla principal
        mostrarPantallaPrincipal(usuario);
    }

    /**
     * Muestra la pantalla principal de la aplicación
     */
    private void mostrarPantallaPrincipal(String nombreUsuario) {
        // Limpiar ventana
        ventana.getContentPane().removeAll();
        ventana.setTitle("Analizador de Ajedrez - Usuario: " + nombreUsuario);

        // Crear un panel con diseño de cuadrícula
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBackground(new Color(240, 217, 181));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel superior con información de usuario y botones
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(new Color(222, 184, 135));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Etiqueta de bienvenida
        JLabel etiquetaBienvenida = new JLabel("Bienvenido, " + nombreUsuario);
        etiquetaBienvenida.setFont(new Font("Arial", Font.BOLD, 14));
        panelSuperior.add(etiquetaBienvenida, BorderLayout.WEST);

        // Botón para cerrar sesión
        JButton botonCerrarSesion = new JButton("Cerrar Sesión");
        botonCerrarSesion.setBackground(new Color(139, 69, 19));
        botonCerrarSesion.setForeground(Color.WHITE);
        botonCerrarSesion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarPantallaLogin();
            }
        });
        panelSuperior.add(botonCerrarSesion, BorderLayout.EAST);

        // Añadir panel superior
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);

        // Panel central con pestañas para diferentes funcionalidades
        JTabbedPane pestañas = new JTabbedPane();

        // Pestaña de análisis de partidas
        JPanel panelAnalisis = crearPanelAnalisis();
        pestañas.addTab("Análisis de Partidas", null, panelAnalisis, "Analizar notación de partidas de ajedrez");

        // Pestaña de tablero de ajedrez
        JPanel panelTablero = crearPanelTablero();
        pestañas.addTab("Tablero", null, panelTablero, "Ver un tablero de ajedrez");

        // Añadir pestañas al panel principal
        panelPrincipal.add(pestañas, BorderLayout.CENTER);

        // Añadir panel principal a la ventana
        ventana.getContentPane().add(panelPrincipal);
        ventana.revalidate();
        ventana.repaint();
    }

    /**
     * Crea el panel para el análisis de partidas
     */
    private JPanel crearPanelAnalisis() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(240, 217, 181));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel para entrada de la partida
        JPanel panelEntrada = new JPanel(new BorderLayout(5, 5));
        panelEntrada.setBorder(BorderFactory.createTitledBorder("Notación de la Partida"));

        // Área de texto para introducir la partida
        areaPartida = new JTextArea();
        areaPartida.setFont(new Font("Monospaced", Font.PLAIN, 14));
        areaPartida.setRows(5);
        areaPartida.setLineWrap(true);
        areaPartida.setWrapStyleWord(true);
        JScrollPane scrollPartida = new JScrollPane(areaPartida);
        panelEntrada.add(scrollPartida, BorderLayout.CENTER);

        // Botón para analizar
        JButton botonAnalizar = new JButton("Analizar Partida");
        botonAnalizar.setBackground(new Color(139, 69, 19));
        botonAnalizar.setForeground(Color.WHITE);
        botonAnalizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                analizarPartida();
            }
        });
        panelEntrada.add(botonAnalizar, BorderLayout.SOUTH);

        // Panel para resultados
        JPanel panelResultados = new JPanel(new BorderLayout(5, 5));
        panelResultados.setBorder(BorderFactory.createTitledBorder("Resultados del Análisis"));

        // Área de texto para mostrar resultados
        areaResultados = new JTextArea();
        areaResultados.setFont(new Font("Monospaced", Font.PLAIN, 14));
        areaResultados.setEditable(false);
        JScrollPane scrollResultados = new JScrollPane(areaResultados);
        panelResultados.add(scrollResultados, BorderLayout.CENTER);

        // Dividir el panel en dos secciones
        JSplitPane divisor = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelEntrada, panelResultados);
        divisor.setResizeWeight(0.3); // 30% arriba, 70% abajo

        panel.add(divisor, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Crea el panel para el tablero de ajedrez
     */
    private JPanel crearPanelTablero() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(240, 217, 181));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel para el tablero
        JPanel tablero = new JPanel(new GridLayout(8, 8));
        tablero.setBorder(BorderFactory.createLineBorder(new Color(139, 69, 19), 5));

        // Crear el tablero 8x8
        for (int fila = 0; fila < 8; fila++) {
            for (int columna = 0; columna < 8; columna++) {
                // Color de la casilla (alternando)
                Color color = (fila + columna) % 2 == 0 ?
                        new Color(240, 217, 181) : // Color claro
                        new Color(181, 136, 99);   // Color oscuro

                // Crear casilla
                JPanel casilla = new JPanel(new BorderLayout());
                casilla.setBackground(color);

                // Etiqueta para la pieza
                JLabel pieza = new JLabel("", JLabel.CENTER);
                pieza.setFont(new Font("Dialog", Font.PLAIN, 36));

                // Colocar piezas iniciales
                if (fila == 0) {
                    // Piezas negras
                    switch (columna) {
                        case 0: case 7: pieza.setText("♜"); break; // Torre
                        case 1: case 6: pieza.setText("♞"); break; // Caballo
                        case 2: case 5: pieza.setText("♝"); break; // Alfil
                        case 3: pieza.setText("♛"); break; // Reina
                        case 4: pieza.setText("♚"); break; // Rey
                    }
                } else if (fila == 1) {
                    pieza.setText("♟"); // Peones negros
                } else if (fila == 6) {
                    pieza.setText("♙"); // Peones blancos
                } else if (fila == 7) {
                    // Piezas blancas
                    switch (columna) {
                        case 0: case 7: pieza.setText("♖"); break; // Torre
                        case 1: case 6: pieza.setText("♘"); break; // Caballo
                        case 2: case 5: pieza.setText("♗"); break; // Alfil
                        case 3: pieza.setText("♕"); break; // Reina
                        case 4: pieza.setText("♔"); break; // Rey
                    }
                }

                casilla.add(pieza, BorderLayout.CENTER);
                tablero.add(casilla);
            }
        }

        // Añadir el tablero al centro del panel
        panel.add(tablero, BorderLayout.CENTER);

        // Añadir información del turno
        JLabel turno = new JLabel("Turno: Blancas", JLabel.CENTER);
        turno.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(turno, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Analiza la partida ingresada por el usuario
     */
    private void analizarPartida() {
        // Obtener el texto de la partida
        String textoPartida = areaPartida.getText().trim();

        // Verificar si hay texto
        if (textoPartida.isEmpty()) {
            mostrarMensajeError("Por favor, introduzca la notación de una partida para analizar.");
            return;
        }

        try {
            // Realizar el análisis sintáctico
            AnalizadorSintactico analizador = new AnalizadorSintactico();
            ResultadoAnalisis resultado = analizador.analizar(textoPartida);

            // Mostrar resultados
            StringBuilder sb = new StringBuilder();

            if (resultado.esValido()) {
                sb.append("✅ LA PARTIDA ES SINTÁCTICAMENTE VÁLIDA\n\n");
                sb.append("📊 INFORMACIÓN:\n");
                sb.append("► Número de turnos analizados: ").append(resultado.getNumeroTurnos()).append("\n\n");

                // Generar y mostrar el árbol visual
                AnalizadorGramatico analizadorGramatico = new AnalizadorGramatico();
                analizadorGramatico.analizar(textoPartida);
                
                sb.append("📋 ÁRBOL DE DERIVACIÓN:\n");
                sb.append("---------------------------------------------\n");
                sb.append("Se ha generado una ventana con el árbol visual de la partida.\n");
            } else {
                sb.append("❌ LA PARTIDA CONTIENE ERRORES SINTÁCTICOS\n\n");
                sb.append("📝 DETALLES DEL ERROR:\n");
                sb.append(resultado.getMensajeError()).append("\n");
            }

            // Mostrar resultados en el área de texto
            areaResultados.setText(sb.toString());
            areaResultados.setCaretPosition(0); // Volver al inicio del texto

        } catch (Exception e) {
            mostrarMensajeError("Error al analizar la partida: " + e.getMessage());
        }
    }

    /**
     * Muestra un mensaje de error
     */
    private void mostrarMensajeError(String mensaje) {
        JOptionPane.showMessageDialog(ventana, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
