package main;

import analizador.AnalizadorGramatico;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class InterfazAjedrez {
    private JFrame ventana;
    private JTextField campoUsuario;
    private JPasswordField campoContrasena;
    private AnalizadorGramatico analizador;

    public InterfazAjedrez() {
        // Inicializar componentes
        ventana = new JFrame("Analizador de Partidas de Ajedrez");
        ventana.setSize(600, 400);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setLocationRelativeTo(null);
        analizador = new AnalizadorGramatico();
        
        mostrarPantallaLogin();
    }

    private void mostrarPantallaLogin() {
        // Panel principal con fondo
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(240, 217, 181));

        // Título
        JLabel titulo = new JLabel("Iniciar Sesión");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setBounds(230, 50, 200, 30);
        panel.add(titulo);

        // Usuario
        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setBounds(150, 120, 100, 25);
        panel.add(lblUsuario);

        campoUsuario = new JTextField();
        campoUsuario.setBounds(250, 120, 200, 25);
        panel.add(campoUsuario);

        // Contraseña
        JLabel lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setBounds(150, 160, 100, 25);
        panel.add(lblContrasena);

        campoContrasena = new JPasswordField();
        campoContrasena.setBounds(250, 160, 200, 25);
        panel.add(campoContrasena);

        // Botón de ingreso
        JButton btnIngresar = new JButton("Ingresar");
        btnIngresar.setBounds(250, 220, 100, 30);
        btnIngresar.setBackground(new Color(139, 69, 19));
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.addActionListener(e -> validarLogin());
        panel.add(btnIngresar);

        ventana.setContentPane(panel);
        ventana.setVisible(true);
    }

    private void validarLogin() {
        String usuario = campoUsuario.getText();
        String contrasena = new String(campoContrasena.getPassword());

        if (usuario.length() < 3 || usuario.length() > 15) {
            mostrarError("El usuario debe tener entre 3 y 15 caracteres");
            return;
        }

        if (contrasena.length() < 6) {
            mostrarError("La contraseña debe tener al menos 6 caracteres");
            return;
        }

        mostrarPantallaPartida(usuario);
    }

    private void mostrarPantallaPartida(String usuario) {
        // Panel principal
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 217, 181));

        // Panel superior con bienvenida
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBackground(new Color(240, 217, 181));
        panelSuperior.add(new JLabel("Bienvenido, " + usuario));

        // Área de texto para la partida
        JTextArea areaPartida = new JTextArea(5, 40);
        areaPartida.setLineWrap(true);
        JScrollPane scrollPartida = new JScrollPane(areaPartida);

        // Botón para analizar
        JButton btnAnalizar = new JButton("Analizar Partida");
        btnAnalizar.setBackground(new Color(139, 69, 19));
        btnAnalizar.setForeground(Color.WHITE);
        btnAnalizar.addActionListener(e -> {
            String textoPartida = areaPartida.getText();
            if (!textoPartida.isEmpty()) {
                analizador.analizar(textoPartida);
            } else {
                mostrarError("Por favor, ingrese una partida para analizar");
            }
        });

        // Panel inferior con botón
        JPanel panelInferior = new JPanel();
        panelInferior.setBackground(new Color(240, 217, 181));
        panelInferior.add(btnAnalizar);

        // Agregar componentes al panel principal
        panel.add(panelSuperior, BorderLayout.NORTH);
        panel.add(scrollPartida, BorderLayout.CENTER);
        panel.add(panelInferior, BorderLayout.SOUTH);

        // Actualizar ventana
        ventana.setContentPane(panel);
        ventana.revalidate();
        ventana.repaint();
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(ventana, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InterfazAjedrez());
    }
}
