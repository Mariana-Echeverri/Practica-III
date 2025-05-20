package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class AjedrezArbolVisualizer extends JPanel implements MouseWheelListener, MouseMotionListener, MouseListener {
    private Arbol.Nodo raiz;
    private int nodeRadius = 25;
    private int horizontalGap = 100;
    private int verticalGap = 70;
    private int treeHeight;

    private double scale = 1.0;
    private double translateX = 0;
    private double translateY = 0;
    private Point lastPoint;

    private Color[] levelColors = {
            new Color(100, 180, 255),  // Azul para la raíz
            new Color(220, 220, 220)   // Gris claro para el resto
    };

    private Arbol.Nodo hoverNode = null;
    private Arbol.Nodo selectedNode = null;

    private JPanel detallesPanel;
    private JLabel jugadaLabel;
    private JLabel infoLabel;

    private Map<Object, Image> tableroCache = new HashMap<>();

    public AjedrezArbolVisualizer(Arbol arbol, JPanel detallesPanel, JLabel jugadaLabel, JLabel infoLabel) {
        this.raiz = arbol.partida;
        this.treeHeight = calculateHeight(raiz);
        this.detallesPanel = detallesPanel;
        this.jugadaLabel = jugadaLabel;
        this.infoLabel = infoLabel;

        int width = (int) Math.pow(2, treeHeight) * horizontalGap;
        int height = (treeHeight + 1) * verticalGap + 50;
        setPreferredSize(new Dimension(width, height));

        addMouseWheelListener(this);
        addMouseMotionListener(this);
        addMouseListener(this);

        ToolTipManager.sharedInstance().setInitialDelay(300);
        ToolTipManager.sharedInstance().setDismissDelay(5000);

        setFocusable(true);

        selectedNode = raiz;
        actualizarPanelDetalles(selectedNode);
    }

    private int calculateHeight(Arbol.Nodo node) {
        if (node == null) return 0;
        return 1 + Math.max(calculateHeight(node.izquierda), calculateHeight(node.derecha));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        g2d.setColor(new Color(245, 245, 245));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        AffineTransform oldTransform = g2d.getTransform();
        g2d.translate(translateX, translateY);
        g2d.scale(scale, scale);

        if (raiz != null) {
            int width = getWidth();
            drawNode(g2d, raiz, width / 2, 50, width / 4, 0);
        }

        g2d.setTransform(oldTransform);

        g2d.setColor(Color.DARK_GRAY);
        g2d.drawString("Usa la rueda del ratón para hacer zoom", 10, 20);
        g2d.drawString("Arrastra para mover el árbol", 10, 40);
        g2d.drawString("Haz clic en un nodo para ver detalles de la jugada", 10, 60);
    }

    private void drawNode(Graphics2D g, Arbol.Nodo node, int x, int y, int xOffset, int level) {
        if (node == null) return;

        // Elegir color según nivel o estado
        Color nodeColor;
        if (level == 0) {
            nodeColor = levelColors[0]; // Color para la raíz
        } else {
            nodeColor = levelColors[1]; // Color para el resto de nodos
        }

        if (node == selectedNode) {
            g.setColor(new Color(100, 149, 237)); // Azul más oscuro para nodo seleccionado
        }
        else if (node == hoverNode) {
            g.setColor(new Color(255, 218, 185)); // Color para hover
        } else {
            g.setColor(nodeColor);
        }

        // Dibujar círculo del nodo
        g.fillOval(x - nodeRadius, y - nodeRadius, 2 * nodeRadius, 2 * nodeRadius);
        g.setColor(Color.BLACK);
        g.drawOval(x - nodeRadius, y - nodeRadius, 2 * nodeRadius, 2 * nodeRadius);

        // Dibujar número del nodo
        String moveNumber = String.valueOf(node.llave);
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(moveNumber);
        g.drawString(moveNumber, x - textWidth / 2, y - 2);

        // Dibujar texto de la jugada
        if (node.jugada != null) {
            String jugadaStr = node.jugada.toString();
            if (jugadaStr.length() > 8) {
                jugadaStr = jugadaStr.substring(0, 7) + "...";
            }
            textWidth = fm.stringWidth(jugadaStr);
            g.drawString(jugadaStr, x - textWidth / 2, y + 12);
        }

        // Calcular posición para el próximo nivel
        int nextY = y + verticalGap;
        
        // Calcular separación horizontal basada en el nivel
        int nextXOffset = xOffset / 2;
        if (nextXOffset < nodeRadius * 2) {
            nextXOffset = nodeRadius * 2;
        }

        // Dibujar hijo izquierdo si existe
        if (node.izquierda != null) {
            int leftX = x - nextXOffset;
            g.drawLine(x, y + nodeRadius, leftX, nextY - nodeRadius);
            drawNode(g, node.izquierda, leftX, nextY, nextXOffset, level + 1);
        }

        // Dibujar hijo derecho si existe
        if (node.derecha != null) {
            int rightX = x + nextXOffset;
            g.drawLine(x, y + nodeRadius, rightX, nextY - nodeRadius);
            drawNode(g, node.derecha, rightX, nextY, nextXOffset, level + 1);
        }
    }

    private Arbol.Nodo findNodeAt(Arbol.Nodo node, int x, int y, int nodeX, int nodeY, int xOffset, int level) {
        if (node == null) return null;

        if (Math.sqrt(Math.pow(x - nodeX, 2) + Math.pow(y - nodeY, 2)) <= nodeRadius) {
            return node;
        }

        int nextY = nodeY + verticalGap;
        int nextXOffset = xOffset / 2;
        if (nextXOffset < nodeRadius * 2) {
            nextXOffset = nodeRadius * 2;
        }

        Arbol.Nodo found = null;
        if (node.izquierda != null) {
            int leftX = nodeX - nextXOffset;
            found = findNodeAt(node.izquierda, x, y, leftX, nextY, nextXOffset, level + 1);
            if (found != null) return found;
        }

        if (node.derecha != null) {
            int rightX = nodeX + nextXOffset;
            found = findNodeAt(node.derecha, x, y, rightX, nextY, nextXOffset, level + 1);
            if (found != null) return found;
        }

        return null;
    }

    private void actualizarPanelDetalles(Arbol.Nodo nodo) {
        if (nodo != null) {
            String jugadaStr = nodo.jugada != null ? nodo.jugada.toString() : "Posición inicial";
            jugadaLabel.setText("Jugada #" + nodo.llave + ": " + jugadaStr);

            StringBuilder info = new StringBuilder("<html>");

            int nivel = calcularNivel(raiz, nodo, 0);
            String turno = (nivel % 2 == 0) ? "Blancas" : "Negras";
            info.append("Turno: ").append(turno).append("<br>");

            info.append("Jugada anterior: ");
            if (nodo.parent != null) {
                info.append("#").append(nodo.parent.llave);
                if (nodo.parent.jugada != null) {
                    info.append(" (").append(nodo.parent.jugada).append(")");
                }
            } else {
                info.append("Ninguna (posición inicial)");
            }
            info.append("<br>");

            info.append("Variantes disponibles: ");
            if (nodo.izquierda != null || nodo.derecha != null) {
                if (nodo.izquierda != null) {
                    info.append("#").append(nodo.izquierda.llave);
                    if (nodo.izquierda.jugada != null) {
                        info.append(" (").append(nodo.izquierda.jugada).append(")");
                    }
                }

                if (nodo.derecha != null) {
                    if (nodo.izquierda != null) info.append(", ");
                    info.append("#").append(nodo.derecha.llave);
                    if (nodo.derecha.jugada != null) {
                        info.append(" (").append(nodo.derecha.jugada).append(")");
                    }
                }
            } else {
                info.append("Ninguna (final de variante)");
            }

            info.append("</html>");
            infoLabel.setText(info.toString());

            if (!detallesPanel.isVisible()) {
                detallesPanel.setVisible(true);
            }
        } else {
            detallesPanel.setVisible(false);
        }
    }

    private int calcularNivel(Arbol.Nodo raiz, Arbol.Nodo objetivo, int nivelActual) {
        if (raiz == null) return -1;
        if (raiz == objetivo) return nivelActual;

        int nivelIzquierda = calcularNivel(raiz.izquierda, objetivo, nivelActual + 1);
        if (nivelIzquierda != -1) return nivelIzquierda;

        return calcularNivel(raiz.derecha, objetivo, nivelActual + 1);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        double oldScale = scale;

        if (e.getWheelRotation() < 0) {
            scale *= 1.1;
        } else {
            scale /= 1.1;
        }

        scale = Math.max(0.1, Math.min(scale, 5.0));

        Point mousePoint = e.getPoint();
        translateX = mousePoint.x - (mousePoint.x - translateX) * (scale / oldScale);
        translateY = mousePoint.y - (mousePoint.y - translateY) * (scale / oldScale);

        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (lastPoint != null) {
            translateX += e.getX() - lastPoint.x;
            translateY += e.getY() - lastPoint.y;
            repaint();
        }
        lastPoint = e.getPoint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int transformedX = (int)((e.getX() - translateX) / scale);
        int transformedY = (int)((e.getY() - translateY) / scale);

        Arbol.Nodo nodeUnderCursor = findNodeAt(raiz, transformedX, transformedY, getWidth() / 2, 50, getWidth() / 4, 0);

        if (nodeUnderCursor != hoverNode) {
            hoverNode = nodeUnderCursor;
            repaint();
        }

        if (nodeUnderCursor != null) {
            String jugadaStr = nodeUnderCursor.jugada != null ? nodeUnderCursor.jugada.toString() : "Posición inicial";
            setToolTipText("Jugada #" + nodeUnderCursor.llave + ": " + jugadaStr);
        } else {
            setToolTipText(null);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (hoverNode != null) {
            selectedNode = hoverNode;
            actualizarPanelDetalles(selectedNode);
            repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        lastPoint = e.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        lastPoint = null;
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {
        hoverNode = null;
        repaint();
    }

    public void guardarComoImagen(String rutaArchivo) {
        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        paint(g2d);
        g2d.dispose();

        try {
            javax.imageio.ImageIO.write(image, "png", new java.io.File(rutaArchivo));
            JOptionPane.showMessageDialog(this,
                    "Imagen guardada correctamente en: " + rutaArchivo,
                    "Guardar Imagen", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al guardar la imagen: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void mostrarArbolAjedrez(Arbol arbol) {
        JFrame frame = new JFrame("Visualizador de Árbol de Ajedrez");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel detallesPanel = new JPanel();
        detallesPanel.setLayout(new BoxLayout(detallesPanel, BoxLayout.Y_AXIS));
        detallesPanel.setBorder(BorderFactory.createTitledBorder("Detalles de la Jugada"));
        detallesPanel.setPreferredSize(new Dimension(300, 600));

        JLabel jugadaLabel = new JLabel("Selecciona un nodo para ver detalles");
        jugadaLabel.setFont(new Font("Dialog", Font.BOLD, 14));
        jugadaLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel infoLabel = new JLabel();
        infoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        detallesPanel.add(Box.createVerticalStrut(10));
        detallesPanel.add(jugadaLabel);
        detallesPanel.add(Box.createVerticalStrut(10));
        detallesPanel.add(infoLabel);
        detallesPanel.add(Box.createVerticalGlue());

        final AjedrezArbolVisualizer visualizer = new AjedrezArbolVisualizer(arbol, detallesPanel, jugadaLabel, infoLabel);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Archivo");

        JMenuItem saveItem = new JMenuItem("Guardar como imagen");
        saveItem.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Guardar Árbol como Imagen");
            if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                String path = fileChooser.getSelectedFile().getAbsolutePath();
                if (!path.toLowerCase().endsWith(".png")) {
                    path += ".png";
                }
                visualizer.guardarComoImagen(path);
            }
        });
        menu.add(saveItem);

        JMenuItem exitItem = new JMenuItem("Salir");
        exitItem.addActionListener(e -> frame.dispose());
        menu.add(exitItem);

        menuBar.add(menu);
        frame.setJMenuBar(menuBar);

        JScrollPane scrollPane = new JScrollPane(visualizer);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(detallesPanel, BorderLayout.EAST);

        frame.add(mainPanel);

        frame.setSize(1100, 700);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
