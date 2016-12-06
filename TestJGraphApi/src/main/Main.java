package main;

import javax.swing.JFrame;
import javax.swing.UIManager;

public class Main {

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//JFrame
		JFrame frame = new JFrame("TestJGraphAPI");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1350, 900);
		frame.setLayout(null);
		frame.setResizable(false);
		
		JGraph graph = new JGraph("Temperature", 0, 0, 750, 850);
		
		/*for(float x = -20; x < 2; x+=0.1f){
			float y = (float) (Math.pow((x), 2));
			for(int i = 0; i < 3; i++){
				graph.addPoint(i, x-i, y);
			}
			graph.addPoint(10, x-10, y);
			
			
		}*/
		
		frame.add(graph);
		
		graph.setGraphNameTextSize(15);
		graph.addGraphName(0, "PeltierElement");
		graph.addGraphName(1, "SolarPanel");
		graph.addGraphName(2, "WLan");
		graph.setBorder(50);
		graph.setArrowSize(5);
		graph.setxAxisText("Time [h]");
		graph.setyAxisText("Temperature [C°]");
		graph.setMaxValue(9, 9);
		graph.setMinValue(-9, 0);
		graph.setxSeperator(10);
		graph.setySeperator(50);
		
	
		
		for(int i = 0; i < 100; i++){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			graph.addPoint(1, i, i);
			graph.update();
		}
		
		
	}

}
