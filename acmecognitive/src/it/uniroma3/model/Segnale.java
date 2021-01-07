package it.uniroma3.model;

import java.util.ArrayList;
import java.util.List;

public class Segnale {
	
	ArrayList<Double> parteReale;
	ArrayList<Double> parteImmaginaria;
	
	double energia;
	int lunghezza;
	
	public Segnale() {
		this.parteReale = new ArrayList<>();
		this.parteImmaginaria = new ArrayList<>();
		this.energia = 0;
		this.lunghezza = 0;
	}
	
	public void addCampione(double pr, double pi) {
		this.parteReale.add(pr);
		this.parteImmaginaria.add(pi);
		this.energia += Math.sqrt((pr*pr) + (pi*pi));
		this.lunghezza++;
	}
	
	public void completaCampioni(List<Double> reali, List<Double> immaginarie) {
		for(int i = 0; i < reali.size(); i++) {
			this.addCampione(reali.get(i), immaginarie.get(i));
		}
	}
	
	// --- Getter --- \\
	
	public double getPotenza(){
		return this.energia/this.lunghezza;
	}	
	
	public ArrayList<Double> getParteReale(){
		return this.parteReale;
	}
	
	public ArrayList<Double> getParteImmaginaria(){
		return this.parteImmaginaria;
	}
	
	public double getLunghezza(){
		return this.lunghezza;		
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("[");
		
		for(int i=0; i < lunghezza; i++) {
			sb.append("(");
			sb.append(String.format("%s", parteReale.get(i)));
			sb.append(", ");
			sb.append(String.format("%s", parteImmaginaria.get(i)));
			sb.append("i");
			sb.append(") ");
		}
		
		sb.append("]");
		
		return sb.toString();
	}
}