package views;
import javax.swing.*;
import java.awt.*;

public class LineNumberComponent extends JComponent {
    private final JTextArea textArea;

    public LineNumberComponent(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Configura el estilo del número de línea
        g.setColor(Color.GRAY);
        FontMetrics fm = g.getFontMetrics();
        int lineHeight = textArea.getFontMetrics(textArea.getFont()).getHeight(); // Usa el mismo height

        // Establece el margen superior para alinearlo con el JTextArea
        Insets textAreaInsets = textArea.getInsets();
        int marginTop = textAreaInsets.top;

        // Calcular las posiciones de las líneas visibles
        int startOffset = textArea.viewToModel2D(new Point(0, 0));
        int endOffset = textArea.viewToModel2D(new Point(0, getHeight()));

        try {
            int startLine = textArea.getLineOfOffset(startOffset);
            int endLine = textArea.getLineOfOffset(endOffset);

            for (int line = startLine; line <= endLine; line++) {
                int y = (line - startLine) * lineHeight + marginTop; // Alineación con margen superior
                g.drawString(Integer.toString(line + 1), 5, y + lineHeight - 3); // Ajusta la posición de texto
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Dimension getPreferredSize() {
        // Mantener un ancho fijo para los números
        return new Dimension(50, textArea.getHeight());
    }
}
