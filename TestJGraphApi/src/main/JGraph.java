package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class JGraph extends JPanel{

	private int x;
	private int y;
	private int height;
	private int width;
	private String name;

	private int border = 20;
	private int graphTextsBorder = 150;

	private int maxValueY=100;
	private int minValueY=0;

	private int minValueX = 0;
	private int maxValueX = 100;

	private int ArrowSize = 10;

	private int maxGraphs = 16;
	private List<ArrayList<Point>> points = new ArrayList<ArrayList<Point>>();
	private String[] graphNames = new String[maxGraphs];

	private int titleTextSize = 25;
	private int graphNameTextSize = 20;

	private String xAxisText = "x Axis";
	private String yAxisText = "y Axis";

	private int xSeperator = 10;
	private int ySeperator = 50;
	
	private boolean showGrid = true;
	private int dotSize = 4;
	private boolean showDots = true;

	public JGraph(String name, int x, int y, int width, int height) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
		setBounds(x, y, width, height);
		initPointLists();
	}
	
	private void initPointLists(){
		for(int i = 0; i < maxGraphs; i++){
			points.add(new ArrayList<Point>());
		}
	}

	private void drawGraph(int id, Graphics g){
		if(points.get(id).size() > 1){
			for(int i = 0; i < points.get(id).size(); i++){
				//if there are more than 1 Point and the lines are in range of the graph -> draw Graph
				if(i < points.get(id).size()-1 && points.get(id).get(i+1).x >= minValueX && points.get(id).get(i+1).y >= minValueY 
						&& points.get(id).get(i).x <= maxValueX && points.get(id).get(i).y <= maxValueY){
					Point startPoint = points.get(id).get(i);
					Point endPoint = points.get(id).get(i+1);
					g.drawLine(
							getGraphX(startPoint.x),
							getGraphY(startPoint.y),
							getGraphX(endPoint.x),
							getGraphY(endPoint.y)); 
					if(showDots){
						g.fillOval(getGraphX(startPoint.x)-dotSize/2, getGraphY(startPoint.y)-dotSize/2, dotSize, dotSize);
					}
				}
			}
		}
	}

	private int getGraphY(float y){
		return (int) (height - ((y- (float) minValueY)* (float) (height-2*border))/ (float)(maxValueY - minValueY)) - border;
	}

	private int getGraphX(float x){
		return (int) (((x-(float) minValueX)*(float)(width-2*border))/ (float)(maxValueX - minValueX)) + border;	
	}

	private static boolean repaint = true;
	private long lastTime = 0;
	private long delta = 1000;	
	public void update(){
		long nowTime = System.currentTimeMillis();
		if(nowTime - lastTime > delta){
			validate();
			repaint();
			lastTime = nowTime;
		}
	}

	public void paintComponent(Graphics g){		//gets called automaticly!

		g.setColor(Color.white);
		g.fillRect(0, 0, width, height);

		//Draw Graphs
		g.setColor(Color.red);
		int graphCounter = 0;
		g.setFont(new Font("TimesRoman", Font.PLAIN, graphNameTextSize));
		for(int i = 0; i < points.size(); i++){
			if(points.get(i).size() != 0){
				graphCounter++;
				g.setColor(getColorID(i));
				if(graphNames[i] == null){
					g.drawString("Graph" + i, width-graphTextsBorder, border/2*(1+graphCounter)+border/2);
				}else{
					g.drawString(graphNames[i], width-graphTextsBorder, border/2*(1+graphCounter)+border/2);
				}
				drawGraph(i, g);
			}			
		}


		//Draw Frame
		g.setColor(Color.white);
		g.fillRect(0, 0, width, border);
		g.fillRect(width-border, 0, border, height);
		g.fillRect(0,0, border, height);
		g.fillRect(0, height-border, width, border);

		//Draw GraphBaseArrows
		g.setColor(Color.black);
		g.drawLine( getGraphX(minValueX), getGraphY(minValueY), width-border, getGraphY(minValueY));
		
		//Draw NumberIndicators
		drawXIndicators(g);
		drawYIndicators(g);
				
		//Draw 0 line in Y
		if(minValueY < 0){
			g.drawLine( getGraphX(minValueX), getGraphY(0), width-border, getGraphY(0));
			drawArrowHead(getGraphX(minValueX)+width-2*border, getGraphY(0), g, 1);
		}else{
			g.drawLine( getGraphX(minValueX), getGraphY(minValueY), width-border, getGraphY(minValueY));
			drawArrowHead(getGraphX(minValueX)+width-2*border, getGraphY(minValueY), g, 1);
		}
		

		g.drawLine( getGraphX(minValueX), getGraphY(minValueY), getGraphX(minValueX), border);
		drawArrowHead(getGraphX(minValueX), getGraphY(maxValueY), g, 2);

		

		//Draw texts and markers
		g.setColor(Color.black);
		g.setFont(new Font("TimesRoman", Font.PLAIN, titleTextSize));
		g.drawString(xAxisText, width-border, getGraphY(0)-border/4);
		g.drawString(yAxisText, (int) ((float)border*1.2f), border);
		g.drawString(name, width/2-50, border);
	}


	private void drawYIndicators(Graphics g) {
		float deltaY = ((float) (maxValueY-minValueY)/ (float)(ySeperator));		
		for(int i = 0; i < ySeperator+1; i++){
			//draw Grid
			if(showGrid){
				g.setColor(new Color(225, 225, 225));
				g.drawLine(getGraphX(minValueX), getGraphY(deltaY*i+minValueY), getGraphX(maxValueX), getGraphY(deltaY*i+minValueY));
				g.setColor(Color.black);
			}
			//Draw Indicators
			g.drawLine(getGraphX(minValueX)-3, getGraphY(deltaY*i+minValueY), getGraphX(minValueX)+3, getGraphY(deltaY*i+minValueY));
			float num = Math.round((i*deltaY+minValueY)*10.0f)/10.0f;
			g.drawString(""+num, getGraphX(minValueX)-40, getGraphY(deltaY*i+minValueY));
		}		
	}

	private void drawXIndicators(Graphics g) {
		float deltaX = ((float) (maxValueX-minValueX)/ (float)(xSeperator));
		for(int i = 0; i < xSeperator+1; i++){
			//draw Grid
			if(showGrid){
				g.setColor(new Color(225, 225, 225));
				g.drawLine(getGraphX(i*deltaX+minValueX), getGraphY(minValueY), getGraphX(i*deltaX+minValueX), getGraphY(maxValueY));
				g.setColor(Color.black);
			}
			//Draw Indicators
			g.drawLine(getGraphX(i*deltaX+minValueX), getGraphY(minValueY)-3, getGraphX(i*deltaX+minValueX), getGraphY(minValueY)+3);
			float num = Math.round((i*deltaX+minValueX)*10.0f)/10.0f;
			g.drawString(""+num, getGraphX(i*deltaX+minValueX), getGraphY(minValueY)+30);
		}
	}

	public boolean addPoint(int id, float x, float y){
		repaint = true;
		if(id < maxGraphs){
			points.get(id).add(new Point(x, y));
			return true;
		}else{
			System.err.println("com.saturn91.JGraph: addPoints: you can only add " +maxGraphs + " Graphs (id=0..." + (maxGraphs-1)+ ")");
			return false;
		}

	}

	public boolean setPointList(int id, ArrayList<Float> x, ArrayList<Float> y){
		repaint = true;
		points.get(id).clear();
		if(x.size() == y.size()){
			for(int i = 0; i < x.size(); i++){
				points.get(id).add(new Point(x.get(i), y.get(i)));
			}
			return true;
		}else{
			System.err.println("com.saturn91.JGraph: setPointsList x.size != y.size!");
			return false;
		}
	}

	public boolean addGraphName(int id, String name){
		repaint = true;
		if(id < 15){
			graphNames[id] = name;
			return true;
		}else{
			System.err.println("com.saturn91.JGraph addGraphName: you can only add " +maxGraphs + " Graphs (id=0..." + (maxGraphs-1)+ ")");
			return false;
		}

	}

	public void setMaxValue(int maxValueX, int maxValueY) {
		repaint = true;
		this.maxValueY = maxValueY;
		this.maxValueX = maxValueX;
	}

	public void setMinValue(int minValueX, int minValueY) {
		repaint = true;
		this.minValueY = minValueY;
		this.minValueX = minValueX;
	}

	public void setBorder(int border) {
		repaint = true;
		this.border = border;
	}

	public void setGraphTextBorder(int graphTextBorder){
		repaint = true;
		this.graphTextsBorder = graphTextBorder;
	}

	public void setTitleTextSize(int titleTextSize) {
		repaint = true;
		this.titleTextSize = titleTextSize;
	}

	public void setGraphNameTextSize(int graphNameTextSize) {
		repaint = true;
		this.graphNameTextSize = graphNameTextSize;
	}

	public void setArrowSize(int arrowSize) {
		repaint = true;
		ArrowSize = arrowSize;
	}	

	public void setxAxisText(String xAxisText) {
		repaint = true;
		this.xAxisText = xAxisText;
	}

	public void setyAxisText(String yAxisText) {
		repaint = true;
		this.yAxisText = yAxisText;
	}	

	public void setxSeperator(int xSeperator) {
		repaint = true;
		this.xSeperator = xSeperator;
	}

	public void setySeperator(int ySeperator) {
		repaint = true;
		this.ySeperator = ySeperator;
	}

	public void setShowGrid(boolean showGrid) {
		this.showGrid = showGrid;
	}

	public void setDotSize(int dotSize) {
		this.dotSize = dotSize;
	}

	public void setShowDots(boolean showDots) {
		this.showDots = showDots;
	}

	public Color getColorID(int id){
		switch (id){
		case 0:
			return new Color(255, 0 , 0);
		case 1:
			return new Color(0, 200, 0);
		case 2:
			return new Color(0, 0, 255);
		case 3:
			return new Color(255, 75, 0);
		case 4:
			return new Color(255, 0, 255);
		case 5: 
			return new Color(0, 200, 200);
		case 6:
			return new Color(200, 75, 200);
		case 7: 
			return new Color(200, 200, 0);
		case 8:
			return new Color(0, 200, 0);
		case 9:
			return new Color(0, 0, 200);
		case 10:
			return new Color(200, 200, 0);
		case 11:
			return new Color(125, 0, 200);
		case 12: 
			return new Color(0, 200, 125);
		case 13:
			return new Color(200, 125, 125);
		default:
			return new Color(50, 50, 125);
		}


	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param g
	 * @param direction 1: right, 2: up, 3: left, else: down 
	 */
	private void drawArrowHead(int x, int y, Graphics g, int direction){
		switch (direction){

		case 1:
			int[] xValues1 = {0+x, 0+x, 2*ArrowSize+x};
			int[] yValues1 = {ArrowSize+y, -ArrowSize+y, 0+y};
			g.fillPolygon(xValues1, yValues1, 3);
			break;

		case 2:
			int[] xValues2 = {-ArrowSize+x, ArrowSize+x, 0+x};
			int[] yValues2 = {0+y, 0+y, -2*ArrowSize+y};
			g.fillPolygon(xValues2, yValues2, 3);
			break;

		case 3:
			int[] xValues3 = {0+x, 0+x, -2*ArrowSize+x};
			int[] yValues3 = {ArrowSize+y, -ArrowSize+y, 0+y};
			g.fillPolygon(xValues3, yValues3, 3);
			break;

		default:
			int[] xValuesd = {-ArrowSize+x, ArrowSize+x, 0+x};
			int[] yValuesd = {0+y, 0+y, 2*ArrowSize+y};
			g.fillPolygon(xValuesd, yValuesd, 3);
			break;				
		}		
	}
	
	public void clear(){
		points= new ArrayList<ArrayList<Point>>();
		initPointLists();
	}
}
