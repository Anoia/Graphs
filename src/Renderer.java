import graphs.*;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashSet;


public class Renderer extends JFrame{
	private Graph graph;
	
	private int size = 600;
	private int radiusBig;
	private int radiusSmall;
	private int m = size/2;



	
	
	public Renderer(Graph graph){
		super("Graphentheorie");
		this.graph = graph;
		
		radiusBig = (4 * m) / 5; 
		radiusSmall =  Math.abs(m-radiusBig)/3;
		
		initializeVertexPositions();
		
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.add(new GraphenPanel());
		this.pack();
		this.setLocationRelativeTo(null);
		
	}

	
	private void initializeVertexPositions() {
		int i = 0;
		int n = graph.getVertices().size();
		for(String vertex: graph.getVertices()){
			double t = 2 * Math.PI * i / n;
			int x = (int) Math.round(m + radiusBig*Math.cos(t));
			int y = (int) Math.round(m + radiusBig * Math.sin(t));
			graph.addVertexPosition(vertex, new int[]{x, y});
            i++;
		}
		
	}




    private class GraphenPanel extends JPanel{
		
		
		
		@Override
		public Dimension getPreferredSize(){
			return new Dimension(size, size);
		}
		
		@Override
		public void paintComponent(Graphics g){

			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setColor(Color.BLACK);
			//g2d.drawOval(m-radiusBig, m-radiusBig, 2*radiusBig, 2*radiusBig);

			//draw Edges
            if(graph instanceof UndirectedGraph){
                drawEdges(g2d, (UndirectedGraph) graph);
            } else if(graph instanceof DirectedGraph){
                drawEdges(g2d, (DirectedGraph) graph);
            }

            drawVertices(g2d);


		}

        /**
         * Zeichnet die Kanten für einen ungerichteten Graphen
         * @param g2d
         * @param g ungerichteter Graph
         */
        public void drawEdges(Graphics2D g2d, UndirectedGraph g){
            for(HashSet<String> edge: g.getEdges()){
                ArrayList<Integer> positions = new ArrayList<Integer>();
                for(String vertex: edge){
                    positions.add(g.getVertexPosition(vertex)[0]);
                    positions.add(g.getVertexPosition(vertex)[1]);
                }

                g2d.drawLine(positions.get(0), positions.get(1), positions.get(2), positions.get(3));
            }

        }

        /**
         * Zeichnet die Kanten für einen Gerichteten Graphen
         * @param g2d
         * @param g gerichteter Graph
         */
        public void drawEdges(Graphics2D g2d, DirectedGraph g){
            for(ArrayList<String> edge: g.getEdges()){
                Point fromPt = new Point(g.getVertexPosition(edge.get(0))[0], g.getVertexPosition(edge.get(0))[1]);
                Point toPt = new Point(g.getVertexPosition(edge.get(1))[0], g.getVertexPosition(edge.get(1))[1]);

                drawLineWithArrow(g2d, fromPt, toPt);

            }
        }

        /**
         * Zeichnet die Pfeile für gerichtete Graphen
         * @param g2d
         * @param tail
         * @param tip
         */
        public void drawLineWithArrow(Graphics2D g2d, Point tail, Point tip){
            double distance = Math.sqrt(Math.pow(tail.x-tip.x, 2) + Math.pow(tail.y-tip.y, 2));
            double newDistance = distance-radiusSmall;
            //move tip, not covered by vertex anymore
            double cx = (tip.x-tail.x) / distance * newDistance;
            double cy =  (tip.y-tail.y) / distance * newDistance;
            tip = new Point((int)(tail.x + cx),(int)(tail.y + cy));

            double phi = Math.toRadians(30);
            int barb = 10;

            g2d.drawLine(tail.x, tail.y, tip.x, tip.y);

            double dy = tip.y - tail.y;
            double dx = tip.x - tail.x;
            double theta = Math.atan2(dy, dx);
            double x, y, rho = theta + phi;

            for(int j = 0; j < 2; j++)
            {
                x = tip.x - barb * Math.cos(rho);
                y = tip.y - barb * Math.sin(rho);
                g2d.draw(new Line2D.Double(tip.x, tip.y, x, y));
                rho = theta - phi;
            }

        }

        /**
         * Zeichnet die Ecken des Graphen g
         * @param g2d
         */
        public void drawVertices(Graphics2D g2d){
            //draw vertices
            for(String vertex: graph.getVertices()){
                int[] pos = graph.getVertexPosition(vertex);
                circeWithText(g2d, vertex, pos[0], pos[1]);
            }
        }
		
		public void circeWithText(Graphics2D g, String vertex, int x, int y){
			g.setColor(Color.WHITE);
			g.fillOval(x-radiusSmall, y-radiusSmall, 2*radiusSmall, 2*radiusSmall);
			g.setColor(Color.BLACK);
			g.drawOval(x-radiusSmall, y-radiusSmall, 2*radiusSmall, 2*radiusSmall);
			g.drawString(vertex, x-3, y+3);
		}
		
	}
}
