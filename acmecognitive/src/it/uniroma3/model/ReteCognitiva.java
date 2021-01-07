/**
 * Realizzazione di rete cognitiva in cui è effettuata fase di spectrum sensing cooperativo
 * tramite Energy Detection. Sono presenti @N_SENSORI utenti cooperativi i cui sensing report
 * sono fusi, per raggiungere la decisione globale della rete, con tecniche dell' AND logico, OR
 * logico e della Maggioranza.
 * 
 * Il segnale dell'utente primario è modulato QPSK ed è a potenza unitaria. 
 * 
 * E' stata fissata una probabilità di falso allarme pari a 10^-2, ed un rumore additivo gaussiano bianco
 * per vari SNR.
 * 
 * Il calcolo delle prestazione è stato effettuato per via teorica, simulata e semianalitica.
 * 
 */

package it.uniroma3.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.jfree.ui.RefineryUtilities;

import it.uniroma3.chart.LineChart;
import it.uniroma3.constants.Constants;

public class ReteCognitiva {
	
	public static boolean primo = true;

	public static void main(String args[]) {
		
		Long start = System.currentTimeMillis();

		System.out.println("// --- INIZIO SIMULAZIONE con " + Constants.N_SENSORI  +  " UTENTI COOPERATIVI --- \\");
		Segnale PU = generaPU();

		Map<Sensore, List<Boolean>> report = new HashMap<Sensore, List<Boolean>>();

		for (int i = 0; i < Constants.N_SENSORI; i++) {
			report.put(new Sensore(PU), new ArrayList<Boolean>());
		}
		
		for(Map.Entry<Sensore, List<Boolean>> entryDetection : report.entrySet()) {
			if(primo) System.out.println("Sensore ");
			for(int snr = Constants.MIN_SNR_dB; snr <= Constants.MAX_SNR_dB; snr ++) {
				entryDetection.getValue().add(entryDetection.getKey().detect(snr));
			}
			primo = false;
		}

		for(int snr = Constants.MIN_SNR_dB; snr <= Constants.MAX_SNR_dB; snr++) {
			System.out.println(" ---- > SNR: " + snr);
			List<Boolean> reportSensori = new ArrayList<Boolean>();
			Set<Sensore> sensori = report.keySet();
			for(Sensore s: sensori) {
				reportSensori.add(report.get(s).get(snr - Constants.MIN_SNR_dB));
			}
			calcolaAND(reportSensori);
			calcolaOR(reportSensori);
			calcolaMAJ(reportSensori);
			System.out.println();
		}
		
		// --- CALCOLO TEMPO DI ESECUZIONE DELLA SIMULAZIONE --- \\
		Long fine = System.currentTimeMillis();
		Long tempoInSecondi = fine-start;
		tempoInSecondi = (long) (tempoInSecondi * Math.pow(10, -3));
		System.out.println("Tempo Esecuzione in Secondi: "+ tempoInSecondi);
		
		
		// --- GENERAZIONE DEL GRAFICO SULLE DETECTION --- \\
		Sensore sensore1 = new ArrayList<Sensore>(report.keySet()).get(0);
		EnergyDetector detector = sensore1.getEnergyDetector();
		LineChart chart = new LineChart(detector.getElencoPDSimulate(), detector.getElencoPDTeoriche(), detector.getElencoPDSemianalitiche());
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);
		
	}//fine main

	private static void calcolaMAJ(List<Boolean> reportSensori) {
		System.out.print("Calcolo maggioranza: ");
		int cont = 0;
		for(Boolean b: reportSensori) {
			if(b == true) {
				cont++;
			}
		}
		if(cont > 4) {
			System.out.println("true");
		} else
			System.out.println("false");
	}

	private static void calcolaOR(List<Boolean> reportSensori) {
		System.out.print("Calcolo OR logico: ");
		for(Boolean b: reportSensori) {
			if(b==true) {
				System.out.print("true ");
				return;
			}
		}
		System.out.print("false ");
		return;
	}

	private static void calcolaAND(List<Boolean> reportSensori) {
		System.out.print("Calcolo AND logico: ");
		for(Boolean b: reportSensori) {
			if(b==false) {
				System.out.print("false ");
				return;
			}
		}
		System.out.print("true ");
		return;
	}	

	public static Segnale generaPU() {

		// Generazione del Segnale del PU modulato QPSK ed a potenza unitaria
		System.out.println("\n -> Generazione del segnale dell'utente primario");
		Segnale segnale_PU = new Segnale();

		Random randomReale, randomImmaginario = null;

		System.out.println(" ---> Segnale PU");
		for(int i=0; i < Constants.NUMERO_CAMPIONI; i++) {
			randomReale = new Random();
			randomImmaginario = new Random();

			segnale_PU.addCampione((Math.signum(.5 - randomReale.nextDouble()))/Math.sqrt(2), (Math.signum(.5 - randomImmaginario.nextDouble()))/Math.sqrt(2));
		}

		System.out.println(" ------> " + segnale_PU);
		return segnale_PU;
	}
}


