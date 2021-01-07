package it.uniroma3.model;

import java.util.ArrayList;
import java.util.Collections;
import it.uniroma3.constants.*;
import it.uniroma3.utils.Utils;

public class Soglia {

	double sogliaSimulata;
	double sogliaTeorica;

	ArrayList<Double> energie;

	public Soglia(double snr, int numeroProve, double probabilitaFalsoAllarme){

		this.energie = new ArrayList<Double>();
		generaEnergie(snr, numeroProve);

		// ---- SOGLIA per SIMULAZIONE ---- \\

		// Calcolo indice per soglia
		int indiceSoglia = numeroProve - (int) (numeroProve * probabilitaFalsoAllarme);		
		//Ordine le potenze di rumore in ordine crescente.
		Collections.sort(this.energie);
		
		if(ReteCognitiva.primo) {
			System.out.println(" ---> Array di Energie Decrescenti: ");
			System.out.println(" ------> " + this.energie);
		}
		
		//Estraggo il valore della soglia.
		this.sogliaSimulata = this.energie.get(indiceSoglia);

		if(ReteCognitiva.primo)System.out.println("\n ---> Soglia simulata calcolata: \n ------> " + sogliaSimulata +"\n");


		// ---- SOGLIA per via TEORICA ---- \\
		
		double media = Utils.valorMedio(energie);
		double varianza = Utils.varianza(energie);
		try {
			this.sogliaTeorica = media + Math.sqrt(2 * varianza) * Utils.InvErf(1 - 2 * Constants.PROB_FALSO_ALLARME);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		
		if(ReteCognitiva.primo) System.out.println(" ---> Soglia teorica calcolata: \n ------> " + sogliaTeorica +"\n");
	}

	private void generaEnergie(double snr, int numeroProve) {

		// Generazione segnali di rumore
		// Inserimento dei valori di potenza in una lista

		for(int i=0; i < numeroProve; i++){
			// Creazione i segnali rumore di lunghezza @NUMERO_CAMPIONI,
			Rumore rumore = new Rumore(snr, Constants.NUMERO_CAMPIONI);
			this.energie.add(rumore.getPotenza());				
		}
	}

	public double getSogliaSimulata(){
		return this.sogliaSimulata;
	}
	
	public double getSogliaTeorica(){
		return this.sogliaTeorica;
	}
}