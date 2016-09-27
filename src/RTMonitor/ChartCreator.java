package RTMonitor;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.Integer;
import java.util.Iterator;
import java.util.Date;
import java.text.SimpleDateFormat;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class ChartCreator extends ApplicationFrame{
	public ChartCreator(String title) {
        super(title);
    }

    //creates the dataset 
    private static CategoryDataset createDataset(int[] values) {      
        // row keys
        String series1 = "Number of Users";

        // column keys
        String category1 = "< 3";
        String category2 = "3 - 8";
        String category3 = "8 - 15";
        String category4 = "> 15";      
        
        // create the dataset
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(values[0], series1, category1);
        dataset.addValue(values[1], series1, category2);
        dataset.addValue(values[2], series1, category3);        
        dataset.addValue(values[3], series1, category4);        
        return dataset;        
    }
    

     //Creates a bar chart to show response time distribution
    private static JFreeChart createResponseTimeChart(CategoryDataset dataset) {       
        // create the chart
        JFreeChart chart = ChartFactory.createBarChart(
        "Response Time Distribution",       // chart title
        "Time (s)",                                       // domain axis label
        "Count",                                           // range axis label
         dataset,                                          // data
         PlotOrientation.VERTICAL,          // orientation
         true,                                                // include legend
         true,                                                // tooltips?
         false                                                // URLs?
        );
        // set the background color for the chart
        chart.setBackgroundPaint(Color.white);
        // get a reference to the plot for further customisation...
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.white);
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        // disable bar outlines
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);        
        // set up gradient paints for series...
        GradientPaint gp2 = new GradientPaint(0.0f, 0.0f, Color.red, 
                0.0f, 0.0f, new Color(64, 0, 0));
        renderer.setSeriesPaint(0, gp2);        
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(
                CategoryLabelPositions.createUpRotationLabelPositions(
                        Math.PI / 6.0));      
        return chart;        
    }
       
     //Creates a bar chart to show  responsiveness distribution
    private static JFreeChart createResponsivenessChart(CategoryDataset dataset) {       
        // create the chart
        JFreeChart chart = ChartFactory.createBarChart(
        "Responsiveness Distribution",       // chart title
        "Time (s)",                                         // domain axis label
        "Count",                                             // range axis label
         dataset,                                            // data
         PlotOrientation.VERTICAL,            // orientation
         true,                                                  // include legend
         true,                                                  // tooltips?
         false                                                  // URLs?
        );
        // set the background color for the chart
        chart.setBackgroundPaint(Color.white);
        // get a reference to the plot for further customisation
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.white);
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        // disable bar outlines
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);        
        // set up gradient paints for series
        GradientPaint gp0 = new GradientPaint(0.0f, 0.0f, Color.blue, 
                0.0f, 0.0f, new Color(0, 0, 64));
        renderer.setSeriesPaint(0, gp0);        
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(
                CategoryLabelPositions.createUpRotationLabelPositions(
                        Math.PI / 6.0));      
        return chart;        
    }

   //group response times into 4 categories, i.e. < 3; 3-8; 8-15; >15 
   public void countResponseTime(Page pg, int[] values) {
        Iterator it = pg.getListOfClients();
        while (it.hasNext()) {
            Client cl = (Client) it.next();
            if (cl.getResponseTime() < 3) {
                values[0]++;
            }
            else if (cl.getResponseTime() >= 3 && cl.getResponseTime() < 8) {
                values[1]++;
            }
            if (cl.getResponseTime() >= 8 && cl.getResponseTime() < 15) {
                values[2]++;
            }
            if (cl.getResponseTime() >= 15) {
                values[3]++;
            }
        }
    }
    
   //group responsiveness into 4 categories, i.e. < 3; 3-8; 8-15; >15 
    public void countResponsiveness(Page pg, int[] values) {
        Iterator it = pg.getListOfClients();
        while (it.hasNext()) {
            Client cl = (Client) it.next();
            if (cl.getResponsiveness() < 3) {
                values[0]++;
            }
            else if (cl.getResponsiveness() >= 3 && cl.getResponsiveness() < 8) {
                values[1]++;
            }
            if (cl.getResponsiveness() >= 8 && cl.getResponsiveness() < 15) {
                values[2]++;
            }
            if (cl.getResponsiveness() >= 15) {
                values[3]++;
            }
        }
    }


 //creates a bar chart for the given page monitored within the specified period 
    public void createChart(Page pg, Date from, Date to) {
        ReportFrame frame = new ReportFrame(pg, from, to);
        frame.setTitle("Detailed Report: " + pg.getName());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.show();
    }
    
    class ReportFrame extends JFrame {
        //set a frame as the container
        public ReportFrame(Page pg, Date from, Date to) {
            final int FRAME_WIDTH = 650;
            final int FRAME_HEIGHT = 700;
            setSize(FRAME_WIDTH, FRAME_HEIGHT);     
            int[] counts = {0, 0, 0, 0};
            String d1, d2;
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
            d1 = formatter.format(from);
            d2 = formatter.format(to);
            
            //set page information
            JLabel nameLabel = new JLabel(" Page name: " + pg.getName());
            JLabel periodLabel = new JLabel(" Period: " + d1 + " to " + d2);
            JLabel noOfRequestsLabel = new JLabel(" Number of Requests: " +             
                                                              pg.getRequestCount());
            JLabel medNoOfObjectsLabel = new JLabel(" Number of Embedded Objects (Median): "
                                                                     + pg.getMedianNoOfObjects());
            JLabel minNoOfObjectsLabel = new JLabel(" Number of Embedded Objects (Min): " + 
                                                                 pg.getMinNoOfObjects());
            JLabel maxNoOfObjectsLabel = new JLabel(" Number of Embedded Objects (Max): " 
                                                                  + pg.getMaxNoOfObjects());
            JLabel medResponseSizeLabel = new JLabel(" Response Size in Bytes (Median): " + 
                                                                      pg.getMedianResponseSize());
            JLabel minResponseSizeLabel = new JLabel(" Response Size in Bytes (Min): " + 
                                                                    pg.getMinResponseSize());
            JLabel maxResponseSizeLabel = new JLabel(" Response Size in Bytes (Max): " + 
                                                                     pg.getMaxResponseSize());
            JLabel avgResponseTimeLabel = new JLabel(" Response Time (Mean): " +                  
				pg.getAverageResponseTime());
            JLabel medResponseTimeLabel = new JLabel(" Response Time (Median): " +  
				 pg.getMedianResponseTime());
            JLabel ninResponseTimeLabel = new JLabel(" Response Time (90th-Percentile): " +  
				pg.getResponseTime_90());
            JLabel avgResponsivenessLabel = new JLabel(" Responsiveness (Mean): " +  
				pg.getAverageResponsiveness());
            JLabel medResponsivenessLabel = new JLabel(" Responsiveness (Median): " +   
				pg.getMedianResponsiveness());
            JLabel ninResponsivenessLabel = new JLabel(" Responsiveness (90th-Percentile): " +  
				pg.getResponsiveness_90());
            JPanel pageInfoPanel = new JPanel();
            pageInfoPanel.setLayout(new GridLayout(16,1));
            pageInfoPanel.add(nameLabel);
            pageInfoPanel.add(periodLabel);
            pageInfoPanel.add(noOfRequestsLabel);
            pageInfoPanel.add(medNoOfObjectsLabel);
            pageInfoPanel.add(minNoOfObjectsLabel);
            pageInfoPanel.add(maxNoOfObjectsLabel);
            pageInfoPanel.add(medResponseSizeLabel);
            pageInfoPanel.add(minResponseSizeLabel);
            pageInfoPanel.add(maxResponseSizeLabel);
            pageInfoPanel.add(avgResponseTimeLabel);
            pageInfoPanel.add(medResponseTimeLabel);
            pageInfoPanel.add(ninResponseTimeLabel);
            pageInfoPanel.add(avgResponsivenessLabel);
            pageInfoPanel.add(medResponsivenessLabel);
            pageInfoPanel.add(ninResponsivenessLabel);
            
            countResponseTime(pg, counts);
            CategoryDataset dataset = createDataset(counts);
            JFreeChart chart = createResponseTimeChart(dataset);
            ChartPanel chartPanelRT = new ChartPanel(chart, false);
            chartPanelRT.setPreferredSize(new Dimension(300, 200));
            JScrollPane spane1 = new JScrollPane(chartPanelRT);   
            
            for (int i=0; i<4; i++) {
                counts[i] = 0;
            }
            countResponsiveness(pg, counts);
            dataset = createDataset(counts);
            chart = createResponsivenessChart(dataset);
            ChartPanel chartPanelRV = new ChartPanel(chart, false);
            chartPanelRV.setPreferredSize(new Dimension(300, 200));
            JScrollPane spane2 = new JScrollPane(chartPanelRV);   
            
            Container contentPane = getContentPane();
            contentPane.add(pageInfoPanel, "North");
            contentPane.add(spane1, "Center");
            contentPane.add(spane2, "South");       
        }
        
    }


}
