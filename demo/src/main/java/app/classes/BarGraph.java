package app.classes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class BarGraph {
    private JFrame frame;

    public BarGraph() {
        frame = new JFrame("Bar Graph");
        frame.setSize(800, 600);  // Alterado para 800x600
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(frame.getSize());
        
        // Aumentar o tamanho da área de desenho
        frame.add(new DrawBars(frame.getSize()));  // Passa o novo tamanho para o gráfico
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String... argv) {
        // new BarGraph();
            // int graphWidth = 500;
            // int graphHeight = 350;
    
            // int widthForYLabels = 40;
            // int heightForXLabels = 40;
    
            // int widthForItemLabel = 100;
            
            // // create your line graph

            // int[] p = {14200,12200,11900,23000,20200};
            // // adding panel to another panel
            // JPanel container = new JPanel();
            // container.setSize(750,480);
            // // container.add(p);
    
            // // adding the parent panel to the main frame
            // JFrame f = new JFrame();
            // f.add(container);
            // f.pack();
            // f.setSize(750,480);
            // f.setVisible(true);
            // f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }
    

    public static class DrawBars extends JPanel implements MouseListener {
        /*
         * Declare Instance Variables Here
         */
        private int x = 200;
        private int y = 2000;
        private double[] mergeSort_serial = new double[5];
        private double[] mergeSort_paralelo = new double[5];

        public DrawBars(Dimension dimension) {
            setSize(dimension);
            setPreferredSize(dimension);
            addMouseListener(this);

            // Leitura do arquivo CSV
            SimpleCSVReader file = new SimpleCSVReader("C:\\Users\\Admin\\Desktop\\Pastas\\Java\\javaMeOdeia\\myMergeSort_paralelo\\projeto_main\\demo\\src\\main\\java\\app\\csv\\merge.csv");
            List<String[]> dados = file.readCSV();

            // Inicializando mergeSort com dados do CSV
            for (int i = 1; i < 5; i++) {
                System.out.println(dados.get(1)[i]);
                mergeSort_serial[i] = Double.parseDouble(dados.get(1)[i]) / 100; // Exemplo de normalização
            }
            for (int i = 1; i < 5; i++) {
                System.out.println(dados.get(2)[i]);
                mergeSort_paralelo[i] = Double.parseDouble(dados.get(2)[i]) / 100; // Exemplo de normalização
            }
        }

        @Override
public void paintComponent(Graphics g) {

    Graphics2D g2 = (Graphics2D) g;
    Dimension d = getSize();

    


    // Redimensiona o gráfico para se ajustar ao novo tamanho
    g2.setColor(Color.white);
    g2.fillRect(0, 0, d.width, d.height);

    Color purple = new Color(102, 0, 102);
    Color red = Color.red;

    // Ticks do eixo Y (ajustados para o novo tamanho)
    g2.setColor(Color.black);
    for (int i = 0; i <= d.height; i += 100) {  // Aumenta os ticks com a nova altura
        g2.drawLine(0, d.height - i, d.width, d.height - i);  // Linhas horizontais ajustadas
        g2.drawString(Integer.toString(i), 0, d.height - i);  // Valores ajustados
    }

    // Rótulo do eixo X
    g2.setFont(new Font("Arial", Font.PLAIN, 14));
    g2.drawString("Tamanho do Array", d.width / 2 , d.height );  // Centralizado

    // Desenho das barras para MergeSort Serial (ajustado)
    for (int i = 1; i < mergeSort_serial.length; i++) {
        int barHeight = (int) (mergeSort_serial[i]);
        int xPos = i * 100 + 50;  // Aumentar espaçamento horizontal
        int yPos = d.height - barHeight - 50;  // Ajuste com base na nova altura

        g2.setColor(purple);
        g2.fillRect(xPos, yPos, 20, barHeight);  // Barras mais largas

        g2.setColor(Color.black);
        g2.drawString(String.format("%.2f", mergeSort_serial[i]), xPos, yPos - 5);
    }

    // Desenho das barras para MergeSort Paralelo (ajustado)
    for (int i = 1; i < mergeSort_paralelo.length; i++) {
        int barHeight = (int) (mergeSort_paralelo[i]);
        int xPos = i * 100 + 100;  // Espaçamento maior para paralelo
        int yPos = d.height - barHeight - 50;

        g2.setColor(red);
        g2.fillRect(xPos, yPos, 20, barHeight);  // Barras mais largas

        g2.setColor(Color.black);
        g2.drawString(String.format("%.2f", mergeSort_paralelo[i]), xPos, yPos - 5);
    }

    // Legenda
    g2.setFont(new Font("Arial", Font.PLAIN, 14));
    g2.setColor(purple);
    g2.drawString("Serial", d.width - 100, 30);

    g2.setColor(red);
    g2.drawString("Paralelo", d.width - 100, 50);

    // Título
    g2.setColor(Color.black);
    g2.setFont(new Font("Arial", Font.PLAIN, 20));
    g2.drawString("Merge Sort Data", d.width / 2 - 100, 20);

}
        public void mousePressed(MouseEvent e) {
            x = e.getX();
            y = e.getY();
            repaint();
        }

        public void mouseReleased(MouseEvent e) {}

        public void mouseEntered(MouseEvent e) {}

        public void mouseExited(MouseEvent e) {}

        public void mouseClicked(MouseEvent e) {}
    }

    public class LineSample{

}
}