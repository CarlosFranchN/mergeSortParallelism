package app.classes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class Graph2 extends JComponent {
    private int xAdjPlc = 20;
    private int yAdjPlc = 20;
    private int legendWidth = 100;
    private int PadValue = 45;
    private int width;
    private int height;
    private int xinterval;
    private int yinterval;
    private String[] xlabels;
    private String[] ylabels;
    private String[] legends;
    private int graphWidth;
    private int graphHeight;
    private int[][] xyValues;
    private int mini;
    private int maxi;
    private int graphMinVal;
    private int graphMaxVal;
    private int graphMaxValX;
    private int graphMinValX;
    private int legendPos;
    private int legendCnt;
    private int topMargin = 20; // Margem superior
    private int bottomMargin = 50; // Margem inferior
    private static final long serialVersionUID = 1L;

    public Graph2(int[] dimensions, int[] adjustmentPlacing, String[] xlabels2, String[] ylabels2, int[][] xyValues) {
        setPreferredSize(new Dimension(dimensions[0], dimensions[1]));
        legendCnt = 0;
        this.legends = ylabels2;

        // Definir o valor máximo e mínimo para o gráfico
        int tempMax = Integer.MIN_VALUE;
        for (int[] xyValue : xyValues) {
            for (int j : xyValue) {
                if (j > tempMax) {
                    tempMax = j;
                }
            }
        }
        maxi = tempMax + (int) Math.round((double) tempMax * 0.045);

        int tempMin = Integer.MAX_VALUE;
        for (int[] xyValue : xyValues) {
            for (int j : xyValue) {
                if (j < tempMin) {
                    tempMin = j;
                }
            }
        }
        mini = tempMin;

        graphMaxVal = 10; // Usando valor máximo encontrado
        graphMinVal = 0; // Usando valor mínimo encontrado
        graphMaxValX = maxi;
        graphMinValX = mini;
        this.xyValues = xyValues;

        xAdjPlc = adjustmentPlacing[0];
        yAdjPlc = adjustmentPlacing[1];
        this.width = dimensions[0];
        this.height = dimensions[1];
        this.xlabels = xlabels2;

        // Definir intervalos de rótulos Y com base no valor máximo
        int startYL = graphMaxVal / 15;
        int yValInterval = startYL;
        ylabels = new String[10];
        for (int i = 0; i < 10; ++i) {
            ylabels[i] = Integer.toString(startYL);
            startYL += yValInterval;
        }
        int startXL = graphMinValX;
        int xValInterval = (graphMaxValX - graphMinValX) / 10;
        xlabels = new String[10];
        for (int i = 0; i < 10; ++i) {
            xlabels[i] = Integer.toString(startXL);
            startXL += xValInterval;
        }

        legendWidth = adjustmentPlacing[2];
        graphWidth = (dimensions[0] - adjustmentPlacing[0]) - legendWidth;
        // 1200 - 50 - 150 = 1000
        graphHeight = dimensions[1] - adjustmentPlacing[1] - bottomMargin - topMargin;
        // 800 - 50 -50 -20 = 680
        yinterval = (int) Math.round((double) graphHeight / 10.0);
        // 680/10 = 68
        xinterval = (int) Math.round((double) graphWidth / (double) xlabels2.length);
        // 1000 / 5 = 200
        legendPos = xAdjPlc + graphWidth + 10;
    }

    private int rand(int min, int max) {
        return new Random().nextInt(max - min + 1) + min;
    }

    private void drawPoint(Graphics g, int[] rgb, int x, int y, String legend) {
        int pointRad = 8;
        // System.out.println("x" + x);
        // System.out.println("y" + y);
        g.setColor(new Color(rgb[0], rgb[1], rgb[2]));
        double plotMult = (double) y / (double) graphMaxVal;
        int yplotVal = (int) Math.round((double) graphHeight * plotMult);
        double plotMultX = (double) (x - graphMinValX) / (double) (graphMaxValX - graphMinValX);
        int xplotVal = (int) Math.round((double) graphWidth * plotMultX);
        // g.fillOval(x - (pointRad / 2), graphHeight - yplotVal - (pointRad / 2) + topMargin, pointRad, pointRad);
        g.fillOval(xplotVal - (pointRad / 2), graphHeight - yplotVal - (pointRad / 2), pointRad, pointRad);

        g.setFont(new Font("default", Font.PLAIN, 9));
        g.drawString("" + x, x - (pointRad / 2) + 5, graphHeight - yplotVal - (pointRad / 2) - 5 + topMargin);

        if (legend != null && !legend.isEmpty()) {
            g.drawString(legend, x + 10, graphHeight - yplotVal + 15 + topMargin);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Fundo branco
        g2d.setColor(new Color(255, 255, 255));
        g2d.fillRect(0, 0, width, height);

        g2d.setStroke(new BasicStroke(1.65f));
        g2d.setColor(new Color(0, 0, 0)); 

        // Desenhar bordas do gráfico com margens no topo e fundo
        g2d.drawLine(xAdjPlc, topMargin, xAdjPlc, graphHeight + topMargin);
        //           20 , 20 , 20 , 680+20
        g2d.drawLine(xAdjPlc, graphHeight + topMargin, graphWidth + xAdjPlc, graphHeight + topMargin);
        //              20,  680+20 , 1000+20 , 680+20
        g2d.drawLine(graphWidth + xAdjPlc, topMargin, graphWidth + xAdjPlc, graphHeight + topMargin);
//                      1000+20 , 20 , 1000+20 , 680+20
// 1000 = 100

        // Definir posição dos rótulos do eixo X (tempos)
        int[] xLabelsPos = new int[10];
        int xint = 0;
        for (int i = 0; i < 10; ++i) {
            int x = xint + xAdjPlc + (xinterval / 2);
            // 0 + 20 + 200/2
            g2d.drawString(xlabels[i], x, graphHeight + 30 + topMargin); // Mover para ajustar
            // 120 , 680+30+20
            xLabelsPos[i] = x;
            
            // Desenhar ticks no eixo X
            g2d.drawLine(x, graphHeight + topMargin, x, graphHeight + 5 + topMargin);
            
            xint += xinterval;
        }

        // Definir rótulos do eixo Y (chaves)
        int yint = topMargin;
        for (int i = 0; i < ylabels.length; ++i) {
            String label = ylabels[ylabels.length - 1 - i];
            g2d.drawString(label, xAdjPlc - 30, yint);
            g.setColor(new Color(235, 235, 235));
            g.drawLine(xAdjPlc, yint, xAdjPlc + graphWidth, yint);
            g.setColor(new Color(0, 0, 0));
            yint += yinterval;
        }

        // Desenhar pontos
        for (int i = 0; i < xyValues[0].length; ++i) {
            drawPoint(
                g,
                new int[]{rand(0, 230), rand(0, 230), rand(0, 230)}, 
                (xyValues[0][i]), // valores no eixo X (tempos)
                xyValues[1][i], // valores no eixo Y (chaves)
                String.valueOf(xyValues[1][i]) // legenda (chaves)
            );
        }
    }

    public static void main(String[] args) {
        // Exemplo de uso:
        int[] tempos = {  86, 88, 87, 78, 72, 77 }; // Valores no eixo X
        int[] chaves = {  2, 4, 5, 10, 100, 1000 }; // Valores no eixo Y
        int[][] xyValues = { tempos, chaves };
        
        String[] xlabels = { "6816", "86", "88", "87", "78", "72", "77" }; // Rótulos do eixo X
        String[] ylabels = { "0", "2", "4", "5", "10", "100", "1000" }; // Rótulos do eixo Y

        int[] dimensions = {1200, 600};
        int[] adj = {50, 50, 150};
        Graph2 graph = new Graph2(new int[]{1200, 800}, new int[]{50, 50, 150}, xlabels, ylabels, xyValues);
        JFrame frame = new JFrame("Merge Sort Tempos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(graph);
        frame.pack();
        frame.setVisible(true);
    }
}
