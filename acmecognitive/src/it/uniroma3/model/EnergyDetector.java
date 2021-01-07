package it.uniroma3.model;

import java.util.ArrayList;
import java.util.List;

import it.uniroma3.constants.Constants;
import it.uniroma3.utils.Erf;
import it.uniroma3.utils.Utils;

public class EnergyDetector {

	// Report singoli in base a politica di detect scelta
	boolean presenzaUtentePrimario_sim;
	boolean presenzaUtentePrimario_teo;
	boolean presenzaUtentePrimario_semi;
	
	double percentualeSopraSoglia;
	int numeroElementiSopraSoglia;
	
	// Liste necessarie per la stampa del chart
	List<Double> elencoPDTeoriche = new ArrayList<Double>();
	List<Double> elencoPDSimulate = new ArrayList<Double>();
	List<Double> elencoPDSemianalitiche = new ArrayList<Double>();

	public boolean detectSimulata(Soglia soglia, List<Segnale> sequenzaSegnali) {


		this.presenzaUtentePrimario_sim = false;
		double valoreSoglia = soglia.getSogliaSimulata();
		int numeroConfrontiConSoglia = sequenzaSegnali.size();
		int sopraSoglia = 0;
		
		if(ReteCognitiva.primo)System.out.println("\n -> Effettuo Detection ");
		//Per ogni blocco presente nella sequenza, ne estraggo la potenza e la confronto con la soglia simulata corrispondente.
		for(int i=0; i<numeroConfrontiConSoglia ; i++){
			double potenza = sequenzaSegnali.get(i).getPotenza();
			if(ReteCognitiva.primo && i==0) System.out.println(" ---> Energia Segnale Rumoroso 1: " + potenza);
			if(potenza > valoreSoglia){
				sopraSoglia++;				
			}
		}
		this.numeroElementiSopraSoglia = sopraSoglia;

		this.percentualeSopraSoglia = ((double) numeroElementiSopraSoglia)/((double) numeroConfrontiConSoglia);
		elencoPDSimulate.add(percentualeSopraSoglia);

		if(percentualeSopraSoglia >= Constants.PROB_DETECTION) {
			this.presenzaUtentePrimario_sim = true;
		}
		
		// Viene qui chiamata detectTeorica e detectSemianalitica con lo scopo di prendere le PD calcolate su base teorica e semianalitica 
		// da riportare poi su grafico
		detectTeorica(soglia, sequenzaSegnali);
		detectSemianalitica(soglia, sequenzaSegnali);
		
		if(ReteCognitiva.primo) System.out.println(" ---> Percentuale Ascolti sopra soglia Totale: " + percentualeSopraSoglia 
				+ "\n ---> Probabilita di detection: " + Constants.PROB_DETECTION + " \n ---> Report: " + presenzaUtentePrimario_sim +"\n");
		
		return presenzaUtentePrimario_sim;
	}

	public boolean detectTeorica(Soglia soglia, List<Segnale> sequenzaSegnali) {

		this.presenzaUtentePrimario_teo = false;
		double valoreSoglia = soglia.getSogliaTeorica();
		List<Double> energie = new ArrayList<Double>();
		for(Segnale s: sequenzaSegnali) {
			energie.add(s.getPotenza());
		}
		double probabilitaDetectionTeorica = 0.5 + 0.5 * Erf.erf(-valoreSoglia + Utils.valorMedio(energie) * Math.pow(2 * Utils.varianza(energie), -0.5));
		elencoPDTeoriche.add(probabilitaDetectionTeorica);
		
		if(probabilitaDetectionTeorica > 0) {
			this.presenzaUtentePrimario_teo = true;
		}
		return presenzaUtentePrimario_teo;
	}
	
	public boolean detectSemianalitica(Soglia soglia, List<Segnale> sequenzaSegnali) {


		this.presenzaUtentePrimario_semi = false;
		double valoreSoglia = soglia.getSogliaTeorica();
		int numeroConfrontiConSoglia = sequenzaSegnali.size();
		int sopraSoglia = 0;
		
		//Per ogni blocco presente nella sequenza, ne estraggo la potenza e la confronto con la soglia teorica corrispondente.
		for(int i=0; i<numeroConfrontiConSoglia ; i++){
			double potenza = sequenzaSegnali.get(i).getPotenza();
			if(potenza > valoreSoglia){
				sopraSoglia++;				
			}
		}
		this.numeroElementiSopraSoglia = sopraSoglia;

		this.percentualeSopraSoglia = ((double) numeroElementiSopraSoglia)/((double) numeroConfrontiConSoglia);
		elencoPDSemianalitiche.add(percentualeSopraSoglia);

		if(percentualeSopraSoglia >= Constants.PROB_DETECTION) {
			this.presenzaUtentePrimario_semi = true;
		}
		
		return presenzaUtentePrimario_semi;
	}
	
	

	// --- Getter --- \\

	public List<Double> getElencoPDTeoriche() {
		return elencoPDTeoriche;
	}

	public List<Double> getElencoPDSimulate() {
		return elencoPDSimulate;
	}
	
	public List<Double> getElencoPDSemianalitiche() {
		return elencoPDSemianalitiche;
	}

	public double getPercentualeSopraSoglia(){
		return this.percentualeSopraSoglia;
	}

	public int getNumeroElementiSopraSoglia(){
		return this.numeroElementiSopraSoglia;
	}
}