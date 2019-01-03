package Tel2Gui1;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.ThermometerPlot;
import org.jfree.chart.plot.dial.DialBackground;
import org.jfree.chart.plot.dial.DialCap;
import org.jfree.chart.plot.dial.DialPlot;
import org.jfree.chart.plot.dial.DialTextAnnotation;
import org.jfree.chart.plot.dial.DialValueIndicator;
import org.jfree.chart.plot.dial.StandardDialFrame;
import org.jfree.chart.plot.dial.StandardDialRange;
import org.jfree.chart.plot.dial.StandardDialScale;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.StandardGradientPaintTransformer;

import com.fazecast.jSerialComm.SerialPort;

/*
 * Author: Jasmine Emanouel
 * Purpose: to graphically show the efficiency of a car
 * Year: 2018
 */

@SuppressWarnings("serial")
public class Telemetry extends JFrame {
	
	
	public SerialPort chosenPort = null;
	 private JPanel panel = new JPanel();
	 private JTabbedPane tabbedPane = new JTabbedPane();
	 private double value = 0;
	 private JFrame window = new JFrame();

	 private JPanel rectangle = new JPanel();
	 private JPanel rectangle1 = new JPanel();
	 private JPanel rectangle2 = new JPanel();
	 private JPanel rectangle3 = new JPanel();
	 
	 private DefaultValueDataset dataset3;
	 private DefaultValueDataset dataset4;
	 private JLabel r1;
	 private JLabel r2;
	 private JLabel r3;
	 private JLabel r4;	
	 private  DefaultValueDataset dataset1;	 
	 
	 public void connect () {
		
		 //Creates drop down panel
		JComboBox<String> portList = new JComboBox<String>();
		JButton connectButton = new JButton("Connect");
		JPanel topPanel = new JPanel();
		topPanel.add(portList);
		topPanel.add(connectButton);
		panel.add(topPanel, BorderLayout.NORTH);
		tabbedPane.add(panel, BorderLayout.SOUTH);
		
		// populate the drop-down box
		SerialPort[] portNames = SerialPort.getCommPorts();
		for(int i = 0; i < portNames.length; i++)
			portList.addItem(portNames[i].getSystemPortName());
				
		// configure the connect button and use another thread to listen for data
		connectButton.addActionListener(new ActionListener(){
			@Override public void actionPerformed(ActionEvent arg0) {
				if(connectButton.getText().equals("Connect")) {
					// attempt to connect to the serial port
					chosenPort = SerialPort.getCommPort(portList.getSelectedItem().toString());
					chosenPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
					if(chosenPort.openPort()) {
						connectButton.setText("Disconnect");
						portList.setEnabled(false);
					
						/*
						 * This thread runs the program. 
						 * It reads each line and gives it to the appropiate case.
						 */
					
						Thread thread = new Thread() {
							@Override public void run() {
								Scanner s = new Scanner(chosenPort.getInputStream());
									while(s.hasNext()) {
									
									String[] c = s.nextLine().split("\\s+"); //Splits the string
										
										//Arrays.stream(c).forEach(System.out::println);
										switch(c[0]) { //the first element of the string
											case "T" : { // runs temp
												value = Double.parseDouble(c[1]);
											    dataset3.setValue(value);	
											    break;
											}
											case "F" : runBlocks(s, c); //runs folts
														break;
											case "V" : runVoltage(s, c); // runs voltage
													   break;
											case "S": runSpeedometer(s, c); // runs speed
														break;
											default :  break;
										}
									}
								}
						}; thread.start();

					}

				} else {
					// disconnect from the serial port
					chosenPort.closePort();
					portList.setEnabled(true);
					connectButton.setText("Connect");
				}
			}
		});
	 }
	 
	 /*
	  * This method creates and places the rectangles and their labels.
	  */
	
	public void rectangle () {
		   rectangle.setBackground(Color.BLACK); /*When nothing is inputted, background is black*/
		   rectangle.setPreferredSize( new Dimension(250, 250) );
		   r1 = new JLabel("0", SwingConstants.CENTER); //inital label reads 0
		   r1.setSize(new Dimension(100, 50));
		   r1.setFont(r1.getFont().deriveFont(34.0f)); //size 34 font
		   rectangle.add(r1, BorderLayout.SOUTH);
		   
		   rectangle1.setBackground( Color.BLACK );
		   rectangle1.setPreferredSize( new Dimension(250, 250) );
		   r2 = new JLabel("0", SwingConstants.CENTER);
		   r2.setSize(new Dimension(100, 50));
		   r2.setFont(r2.getFont().deriveFont(34.0f));
		   rectangle1.add(r2, BorderLayout.SOUTH);
		   
		   rectangle2.setBackground( Color.BLACK );
		   rectangle2.setPreferredSize( new Dimension(250, 250) );
		   r3 = new JLabel("0", SwingConstants.CENTER);
		   r3.setSize(new Dimension(100, 50));
		   r3.setFont(r3.getFont().deriveFont(34.0f));
		   rectangle2.add(r3, BorderLayout.SOUTH);
		   
		   rectangle3.setBackground( Color.BLACK );
		   rectangle3.setPreferredSize( new Dimension(250, 250) );
		   r4 = new JLabel("0", SwingConstants.CENTER);
		   r4.setSize(new Dimension(100, 50));
		   r4.setFont(r4.getFont().deriveFont(34.0f));
		   rectangle3.add(r4);
		   
		   r1.setBackground(Color.WHITE);
		   r1.setOpaque(true);
		   r2.setBackground(Color.WHITE);
		   r2.setOpaque(true);
		   r3.setBackground(Color.WHITE);
		   r3.setOpaque(true);
		   r4.setBackground(Color.WHITE);
		   r4.setOpaque(true);
		   
		   panel.add( rectangle1 );
		   panel.add( rectangle2 );
		   panel.add( rectangle3 );
		   panel.add( rectangle);
		  
		   tabbedPane.add(panel, BorderLayout.NORTH);
	}
	
	/*
	 * This method creates the speedometer gui
	 * makes it through layers	
	 * 		dial frame
	 * 		heading = dial text annotation
	 * 		numbers = standard tick scale
	 * 		where the dial can go =  standard dial range
	 * 		pin
	 * 		pointer
	 * 		cap
	 */
	
	public void speedometer() {

		dataset1 = new DefaultValueDataset(95.7D);
		
		DialPlot dial = new DialPlot();
		dial.setView(0.0D, 0.0D, 1.0D, 1.0D);
		dial.setDataset(0, dataset1);
	
		StandardDialFrame dialFrame = new StandardDialFrame();
		dialFrame.setBackgroundPaint(Color.BLACK);
		dialFrame.setForegroundPaint(Color.BLACK);
		dial.setDialFrame(dialFrame);
		
		GradientPaint paint = new GradientPaint(new Point(), new Color(255, 255, 255), new Point(), new Color(255, 255, 255));
		DialBackground dialBackground = new DialBackground(paint);
		
		dialBackground.setGradientPaintTransformer(new StandardGradientPaintTransformer(GradientPaintTransformType.VERTICAL));
		dial.setBackground(dialBackground);
		
		 DialTextAnnotation dialtextannotation = new DialTextAnnotation("Speed (km/h)");
         dialtextannotation.setRadius(0.69999999999999996D);
         dial.addLayer(dialtextannotation);
         
         DialValueIndicator dialvalueindicator = new DialValueIndicator(0);
         dialvalueindicator.setOutlinePaint(Color.BLACK);
         dialvalueindicator.setRadius(0.4D);
         dialvalueindicator.setAngle(-103D);
         dial.addLayer(dialvalueindicator);
         
         StandardDialScale standarddialscale = new StandardDialScale(0D, 150D, -120D, -300D, 20D, 4);
         standarddialscale.setTickRadius(0.88D);
         standarddialscale.setTickLabelOffset(0.14999999999999999D);
         dial.addScale(0, standarddialscale);
         
         StandardDialScale standarddialscale1 = new StandardDialScale(0.0D, 100D, -120D, -300D, 10D, 4);
         standarddialscale1.setTickRadius(0.5D);
         standarddialscale1.setTickLabelOffset(0.14999999999999999D);
         standarddialscale1.setMajorTickPaint(Color.WHITE);
         standarddialscale1.setMinorTickPaint(Color.WHITE);
         dial.addScale(1, standarddialscale1);
         
         dial.mapDatasetToScale(1, 1);
         
         StandardDialRange standarddialrange = new StandardDialRange(90D, 100D, Color.BLACK);
         standarddialrange.setScaleIndex(1);
         standarddialrange.setInnerRadius(0.58999999999999997D);
         standarddialrange.setOuterRadius(0.58999999999999997D);
         dial.addLayer(standarddialrange);
         
         org.jfree.chart.plot.dial.DialPointer.Pin pin = new org.jfree.chart.plot.dial.DialPointer.Pin(1);
         pin.setRadius(0.55000000000000004D);
         dial.addPointer(pin);
         
         org.jfree.chart.plot.dial.DialPointer.Pointer pointer = new org.jfree.chart.plot.dial.DialPointer.Pointer();
         dial.addPointer(pointer);
                  
         DialCap dialcap = new DialCap();
         dialcap.setRadius(0.10000000000000001D);
         dial.setCap(dialcap);
         
        JFreeChart jfreechart = new JFreeChart(dial);
        ChartPanel chartPanel = new ChartPanel(jfreechart);
        chartPanel.setPreferredSize(new Dimension(400, 400));
        panel.add(chartPanel);
        tabbedPane.add(panel);
	}
	
	/*
	 * This method creates the thermometer for temperature
	 */
	
	public void thermometer() {
		
		int W = 200;                       
		int H = 2 * W;
		
		dataset3 = new DefaultValueDataset(value);
		
		ThermometerPlot plot1 = new ThermometerPlot(dataset3);
		plot1.setUnits(ThermometerPlot.UNITS_CELCIUS);
		JFreeChart chart3;
	    chart3 = new JFreeChart(
				"Thermometer",  // chart title
	            JFreeChart.DEFAULT_TITLE_FONT,
	            plot1,                  // plot
	            true); 
	    plot1.setRange(40, 80); //Changes the range on the axis
	    plot1.setMercuryPaint(Color.RED);
	    plot1.setSubrangeInfo(45, 70, 80);
	    plot1.setSubrangePaint(0, Color.GREEN);
	    plot1.setSubrangePaint(1, Color.orange);
	    plot1.setSubrangePaint(2, Color.red.darker());
	    panel.add(new ChartPanel(chart3, W, H, W, H, W, H, false, true, true, true, true, true));

		tabbedPane.add(panel, BorderLayout.NORTH);
	}	
	
	/*
	 * This method creates the thermometer for voltage.
	 */
	
	public void voltage() {
		int W = 200;
		int H = 2 * W;
		
		dataset4 = new DefaultValueDataset(value);
		ThermometerPlot plot2 = new ThermometerPlot(dataset4);
		plot2.setUnits(ThermometerPlot.UNITS_NONE);
		
		JFreeChart chart4;
	    chart4 = new JFreeChart(
				"Voltage",  // chart title
	            JFreeChart.DEFAULT_TITLE_FONT,
	            plot2,                  // plot
	            true); 
	    plot2.setMercuryPaint(Color.RED);
	    plot2.setRange(300, 700);//Changes the range on the axis
	    plot2.setSubrangeInfo(450, 600, 650); //Changes the coloured lines
	    plot2.setSubrangePaint(0, Color.GREEN);
	    plot2.setSubrangePaint(1, Color.orange);
	    plot2.setSubrangePaint(2, Color.red.darker());
	    panel.add(new ChartPanel(chart4, W, H, W, H, W, H, false, true, true, true, true, true));
	    

		tabbedPane.add(panel, BorderLayout.NORTH);
	}

	/*
	 * This method isn't being run

	
	public void runTemp(Scanner s, String[] line) {
		
		Thread tempThread = new Thread() {
			@Override public void run() {
				
					try {
						value = Double.parseDouble(line[1]);
					    dataset3.setValue(value);	
					    panel.repaint();
						} catch(Exception e) {}
				
			}
		}; tempThread.start();
	}
	*/
	
	/*
	 * This method run's the rectangles.
	 * It sets the color of the blocks to green or red based on data inputted
	 */

	
	public void runBlocks(Scanner s, String[] line) {
		
		Thread faultThread = new Thread() {
			@Override public void run() {
				try {
						value = Double.parseDouble(line[1]);
						if(value == 1)	{ /*TARRANT you have to change this to whatever the value should be*/
							rectangle.setBackground(Color.RED); //bms
							r1.setText("BMS " + value); //prints bms and the current value
							rectangle1.setBackground(Color.RED); //pdoc
							r2.setText("PDOC " + value);
							rectangle2.setBackground(Color.RED);		//imd
							r3.setText("IMD " + value);
							rectangle3.setBackground(Color.RED); //bsp
							r4.setText("BSP " + value);
							panel.repaint();
						} else {
							rectangle.setBackground(Color.GREEN);
							r1.setText("BMS " + value);
							rectangle1.setBackground(Color.GREEN);
							r2.setText("PDOC " + value);
							rectangle2.setBackground(Color.GREEN);
							r3.setText("IMD " + value);
							rectangle3.setBackground(Color.GREEN);
							r4.setText("BSP " + value);
							panel.repaint();
						}
	
					} catch(Exception e) {}
				}
			
		}; faultThread.start();
	}
	
	/*
	 * This method runs the voltage thermometer
	 */
	
	public void runVoltage(Scanner s, String[] line) {
	
		Thread voltageThread = new Thread() {
			@Override public void run() {
				Scanner s = new Scanner(chosenPort.getInputStream());
					try {
				        value = Double.parseDouble(line[1]);
				        dataset4.setValue(value);        
				        panel.repaint();
					} catch(Exception e) {}
				
				} 
			
		}; voltageThread.start();
		
	}
	
	/*
	 * This method runs the speedometer
	 * Whenever the value changes, both the gui number
	 * and dial move.
	 */
	
	public void runSpeedometer(Scanner s, String[] line) {
		Thread speedThread = new Thread() {
			@Override public void run() {
					try {
						//String line = s.nextLine();
				        value = Double.parseDouble(line[1]);
				        dataset1.setValue(value);        
					} catch(Exception e) {}
				
				} 
			
		}; speedThread.start();
	}

	public Telemetry() {
		
		
		connect();
		thermometer();
		voltage();
		rectangle();		
		speedometer();
		
		window.setTitle("UTS Motorsports Electric 2018: Telemetry");
		window.setExtendedState(JFrame.MAXIMIZED_BOTH);
		window.setLayout(new BorderLayout());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getContentPane().add(tabbedPane);
		window.setVisible(true);
	}

	public static void main(String[] args) {
		
		Telemetry g = new Telemetry();
		g.pack();
	}

}