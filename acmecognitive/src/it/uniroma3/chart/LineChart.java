package it.uniroma3.chart;

import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;

import it.uniroma3.constants.Constants;

public class LineChart extends ApplicationFrame {

	private static final long serialVersionUID = 1L;

	public LineChart(List<Double> elencoPDSimulate, List<Double> elencoPDTeoriche, List<Double> elencoPDSemianalitiche) {
		super("Thresholds graphs");
		JFreeChart lineChart = ChartFactory.createLineChart(
				"Detection",
				"SNR [dB]","Probabilità di detection",
				createDataset(elencoPDSimulate, elencoPDTeoriche, elencoPDSemianalitiche),
				PlotOrientation.VERTICAL,
				true,true,false);

		ChartPanel chartPanel = new ChartPanel(lineChart);
		chartPanel.setPreferredSize(new java.awt.Dimension(560,367));
		setContentPane(chartPanel);
	}

	private DefaultCategoryDataset createDataset(List<Double> elencoPDSimulate, List<Double> elencoPDTeoriche, List<Double> elencoPDSemianalitiche) {
		
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		Integer counter = Constants.MIN_SNR_dB;
		
		// Costruzione curva di detection TEORICA
		for(Double pd : elencoPDTeoriche) {
			dataset.addValue(pd, "Curva teorica", counter.toString());
			counter++;
		}

		// Costruzione curva di detection SIMULATA
		counter = Constants.MIN_SNR_dB;
		for(Double pd : elencoPDSimulate) {
			dataset.addValue(pd, "Curva simulata", counter.toString());
			counter++;
		}
		
		// Costruzione curva di detection SEMIANALITICA
		counter = Constants.MIN_SNR_dB;
		for(Double pd : elencoPDSemianalitiche) {
			dataset.addValue(pd, "Curva semianalitica", counter.toString());
			counter++;
		}
		
		return dataset;
	}
}
