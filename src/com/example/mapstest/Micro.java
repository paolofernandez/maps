package com.example.mapstest;

import java.util.ArrayList;

public class Micro {
	public String linea;
	public String tipo;
	public String frecuencia;
	public String tarifa;
	public String horaini;
	public String horafin;
	public ArrayList<Punto> poly;
	
	public Micro(String linea, String tipo, String frecuencia, String tarifa,
			String horaini, String horafin, ArrayList<Punto> poly) {
		super();
		this.linea = linea;
		this.tipo = tipo;
		this.frecuencia = frecuencia;
		this.tarifa = tarifa;
		this.horaini = horaini;
		this.horafin = horafin;
		this.poly = poly;
	}
	
	
	

}

