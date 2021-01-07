package it.uniroma3.model;

import java.util.ArrayList;
import java.util.List;

import it.uniroma3.constants.Constants;

public class Sensore {

	private EnergyDetector energyDetector;

	private Segnale segnale_PU;


	public Sensore(Segnale segnale_PU) {
		energyDetector = new EnergyDetector();
		this.segnale_PU = segnale_PU;
	}


	public Boolean detect(double snr) {
		
		// IPOTESI H0, segnale PU assente
		
		if(ReteCognitiva.primo)System.out.println("\n -> SNR : "+(int)snr);
		if(ReteCognitiva.primo)System.out.println("\n -> IPOTESI H0");
		// Calcolo della soglia: tre politiche adottate
		Soglia soglia = new Soglia(snr, Constants.N_PROVE_H0, Constants.PROB_FALSO_ALLARME);
		
		// IPOTESI H1, segnale PU presente
		
		if(ReteCognitiva.primo)System.out.println(" -> IPOTESI H1");
		List<Segnale> segnali = generate(snr);

		return energyDetector.detectSimulata(soglia, segnali);
		
	}



	private List<Segnale> generate(double snr) {

		List<Segnale> segnali = new ArrayList<Segnale>();

		// Generazione dei segnali rumorosi
		for(int i=0; i < Constants.N_PROVE_H1; i++){
			// Creazione i segnali rumore di lunghezza NUMERO_CAMPIONI,
			Rumore rumore = new Rumore(snr, Constants.NUMERO_CAMPIONI);
			Segnale segnale_rumoroso = new Segnale();
			for(int j=0; j < segnale_PU.getLunghezza(); j++) {
				double parteRealeFinale = segnale_PU.getParteReale().get(j) + rumore.getSegnale().getParteReale().get(j);
				double parteImmaginariaFinale = segnale_PU.getParteImmaginaria().get(j) + rumore.getSegnale().getParteImmaginaria().get(j);
				segnale_rumoroso.addCampione(parteRealeFinale, parteImmaginariaFinale);
			}
			segnali.add(segnale_rumoroso);			
		}
		if(ReteCognitiva.primo)System.out.println(" --->Segnale Rumoroso 1 di " + Constants.N_PROVE_H1 + ": \n ------> "+ segnali.get(1));
		return segnali;
	}
	
	// -- Getter per Chart -- \\
	
	public List<Double> getPDTeoriche(){
		return this.energyDetector.getElencoPDTeoriche();
	}
	
	public List<Double> getPDSimulate(){
		return this.energyDetector.getElencoPDSimulate();
	}
	
	public List<Double> getPDSemianalitiche(){
		return this.energyDetector.getElencoPDSemianalitiche();
	}
	
	public EnergyDetector getEnergyDetector() {
		return energyDetector;
	}

}
