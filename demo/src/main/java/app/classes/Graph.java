package app.classes;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class Graph extends JComponent {
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

        private int legendPos;
        private int legendCnt;

        private static final long serialVersionUID = 1L;

        public Graph(int[] dimensions, int[] adjustmentPlacing, String[] xlabels2, String[] ylabels2, int[][] xyValues)
        {
            setPreferredSize(new Dimension(dimensions[0], dimensions[1]));

            legendCnt = 0;
            this.legends = ylabels2;
            // finds the max value
            int tempMax = 0;
            for(int i=0;i<xyValues.length;++i){
                for(int j=0;j<xyValues[i].length; ++j){
                    if(xyValues[i][j] > tempMax){
                        tempMax = xyValues[i][j];
                    }
                }
            }
            maxi = tempMax+(int)Math.round((double)tempMax*0.045);
            
            // finds the lowest
            int tempMin = tempMax;
            for(int i=0;i<xyValues.length;++i){
                for(int j=0;j<xyValues[i].length;++j){
                    if(xyValues[i][j]<tempMin){
                        tempMin = xyValues[i][j];
                    }
                }
            }
            mini = tempMin;

            graphMaxVal = (maxi+PadValue)-(mini-PadValue);
            graphMinVal = 0;
            
            this.xyValues = xyValues;

            xAdjPlc = adjustmentPlacing[0];
            yAdjPlc = adjustmentPlacing[1];

            this.width = dimensions[0];
            this.height = dimensions[1];

            
            this.xlabels = xlabels2;

            int startYL = graphMaxVal/10;
            int yValInterval = startYL;
            ylabels = new String[10];
            for(int i=0; i<10; ++i){
                ylabels[i] = Integer.toString(startYL);
                startYL+=yValInterval;
            }

            legendWidth = adjustmentPlacing[2];
            graphWidth = (dimensions[0] - adjustmentPlacing[0])-legendWidth;
            graphHeight = dimensions[1] - adjustmentPlacing[1];

            yinterval = (int)Math.round((double)graphHeight/10.0);
            xinterval = (int)Math.round((double)graphWidth/(double)xlabels2.length);

            legendPos = xAdjPlc+graphWidth+10;
        }

                private int rand(int min, int max)
        {
            if (min > max || (max - min + 1 > Integer.MAX_VALUE)) {
                throw new IllegalArgumentException("Invalid range");
            }
            return new Random().nextInt(max - min + 1) + min;
        }

        private void lineSeries(Graphics g, int[] rgb,int[] x, int[] y)
        {
            
            if (legendCnt == legends.length) legendCnt = 0;

    g.setColor(new Color(rgb[0], rgb[1], rgb[2]));
    g.drawString(legends[legendCnt], legendPos, (15 * (legendCnt + 1)));
    legendCnt++;

    int prevx = 0;
    int prevy = y[0] - (yinterval / 2); // Para usar o valor de y como eixo x
    int pointRad = 8;

    for (int i = 0; i < x.length; ++i) {
        g.setColor(new Color(rgb[0], rgb[1], rgb[2]));

        // Converte para as novas coordenadas
        double plotMult = (double) x[i] / (double) graphMaxVal;
        int xplotVal = (int) Math.round((double) graphWidth * plotMult);

        g.drawLine(prevx, graphHeight - prevy, xplotVal, graphHeight - y[i]); // Inverte as coordenadas
        g.fillOval(xplotVal - (pointRad / 2), graphHeight - y[i] - (pointRad / 2), pointRad, pointRad);

        g.setFont(new Font("default", Font.PLAIN, 9));
        g.drawString("" + y[i], xplotVal - (pointRad / 2) + 5, graphHeight - y[i] - (pointRad / 2) - 5);

        prevx = xplotVal;
        prevy = y[i];
    }
        }


        @Override
        public void paintComponent(Graphics g)
        {
                super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;

    // Configurações do gráfico
    g2d.setFont(new Font("SansSerif", Font.BOLD, 10));
    g2d.setColor(new Color(255, 255, 255));
    g2d.fillRect(0, 0, width, height);
    g2d.setStroke(new BasicStroke(1.65f));
    g2d.setColor(new Color(0, 0, 0)); // preto

    // Bordas do gráfico
    g2d.drawLine(xAdjPlc, 0, xAdjPlc, graphHeight);
    g2d.drawLine(xAdjPlc, graphHeight, graphWidth + xAdjPlc, graphHeight);
    g2d.drawLine(graphWidth + xAdjPlc, 0, graphWidth + xAdjPlc, graphHeight);

    // Labels do eixo X
    int[] xLabelsPos = new int[xlabels.length];
    g2d.setColor(new Color(rand(0, 150), rand(0, 150), rand(0, 150))); // cor aleatória
    int xint = 0;
    for (int i = 0; i < xlabels.length; ++i) {
        g2d.drawString(xlabels[i], xint + xAdjPlc + (xinterval / 2) - xlabels[i].length(), graphHeight + 20);
        xLabelsPos[i] = xint + xAdjPlc + (xinterval / 2);
        xint += xinterval;
    }

    // Labels do eixo Y
    int yint = 0;
    for (int i = 0; i < ylabels.length; ++i) {
        String label = ylabels[ylabels.length - 1 - i];
        g2d.drawString(label, (xAdjPlc / 2) - label.length(), yint);
        g2d.setColor(new Color(235, 235, 235));
        g2d.drawLine(xAdjPlc, yint, xAdjPlc + graphWidth, yint);
        g2d.setColor(new Color(0, 0, 0));
        yint += yinterval;
    }

    // Adicionando os xSticks
    for (int i = 0; i < xLabelsPos.length; ++i) {
        g.setColor(new Color(0, 0, 0));
        g.drawLine(xLabelsPos[i], graphHeight - 5, xLabelsPos[i], graphHeight + 5);
    }

    // Plotando os dados com os eixos invertidos
    for (int i = 0; i < xyValues.length; ++i) {
        lineSeries(
            g,
            new int[]{rand(0, 230), rand(0, 230), rand(0, 230)}, // cores
            xyValues[i], // usa os valores de y
            xLabelsPos // usa as posições de x
        );
    }
        }

        public static boolean isNumeric(String strNum) {
            if (strNum == null) {
                return false;
            }
            try {
                double d = Double.parseDouble(strNum);
            } catch (NumberFormatException nfe) {
                return false;
            }
            return true;
        }

        public static void main(String[] args) {   
            int[] dimensions = {600,400};
            int[] adj = {50,50,150};
            int[] mergeSort_serial = new int[5];
            int[] mergeSort_paralelo = new int[5];

            SimpleCSVReader file = new SimpleCSVReader("C:\\Users\\Admin\\Desktop\\Pastas\\Java\\javaMeOdeia\\myMergeSort_paralelo\\projeto_main\\demo\\src\\main\\java\\app\\csv\\merge.csv");
            List<String[]> dados = file.readCSV();

            // Inicializando mergeSort com dados do CSV
            for (int i = 1; i <= 5; i++) {
                System.out.println(dados.get(1)[i]);
                
                mergeSort_serial[i-1] = Integer.parseInt(dados.get(1)[i]) / 1000; // Exemplo de normalização
            }
            for (int i = 1; i <= 5; i++) {
                System.out.println(dados.get(2)[i]);
                mergeSort_paralelo[i-1] = Integer.parseInt(dados.get(2)[i]) / 1000; // Exemplo de normalização
            }

            int[][] xyValues = {
                mergeSort_serial,
                mergeSort_paralelo
            };
            String[] xlabels = {"1","2","3","4","5"};
            String[] legends = {"Serial" , "Paralelo"};
            Graph graph = new Graph(dimensions, adj, xlabels, legends, xyValues);
            JFrame frame = new JFrame("Merge Sort Tempos");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);

            // Adicionando o gráfico ao JFrame
            frame.add(graph);

            // Tornar visível
            frame.setVisible(true);
            }
    }

