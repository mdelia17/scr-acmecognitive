package it.uniroma3.model;

import java.util.Random;

public class Rumore {

	Segnale segnale;

	public Rumore(double snr, int lunghezza){

		this.segnale = new Segnale();

		// Linearizzazione dell'SNR_dB
		double snrL = Math.pow(10, (snr/10));

		// Supposta Potenza di segnale unitaria
		double potenzaRumore = 1/snrL;

		Random randomReale, randomImmaginario = null;

		for(int i = 0; i < lunghezza; i++){

			// Instazio oggetti java.util.Random
			randomReale = new Random();
			randomImmaginario =  new Random();

			// Generazione casuale di campioni a VARIANZA UNITARIA e a VALOR MEDIO NULLO. Pertanto è omessa la divisione per la deviazione standard
			this.segnale.addCampione(randomReale.nextGaussian() * Math.sqrt(potenzaRumore/2.0), randomImmaginario.nextGaussian()* Math.sqrt(potenzaRumore/2.0));
		}
	}
	
	// --- Getter --- \\
	
	public double getPotenza(){
		return this.segnale.getPotenza();
	}

	public Segnale getSegnale(){
		return this.segnale;
	}
}