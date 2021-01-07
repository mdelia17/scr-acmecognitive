package it.uniroma3.constants;

public class Constants {

	/**
	 * Numero campioni di cui sono composti i segnali e il rumore 
	 * generati all'interno della simulazione
	 * 
	 */
	public static final int NUMERO_CAMPIONI = 1000;
	
	/**
	 * Numero di prove su cui è effettuata la futura decisione della
	 * soglia per simulazione
	 * 
	 */
	public static final int N_PROVE_H0 = 1000;
	
	/**
	 * Numero di prove su cui è calcolata la detection
	 */
	public static final int N_PROVE_H1 = 1000;
	
	/**
	 * Probabilità di falso allarme
	 */
	public static final double PROB_FALSO_ALLARME = Math.pow(10, -2);
	
	/**
	 * Minimo valore di SNR in dB del range su cui la simulazione effettuerà l'analisi
	 */
	public static final int MIN_SNR_dB = -25;
	
	/**
	 * Massimo valore di SNR in dB del range su cui la simulazione effettuerà l'analisi
	 */
	public static final int MAX_SNR_dB = -5;
	
	/**
	 * Soglia percentuale per contronto di probabilità di detection
	 * 
	 */
	public static final double PROB_DETECTION = .7;
	
	/**
	 * Numero di sensori su cui è effettuata la simulazione
	 * 
	 * Inserire un numero dispari per garantire il corretto funzionamento della regola
	 * di maggioranza attuata nell'analisi dei report finali dei sensori
	 */
	public static final int N_SENSORI = 9;

}
