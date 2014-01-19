package com.example.mapstest;

import java.util.ArrayList;

public class Trafico {
	
	public String tipo_trafico;
	public ArrayList<Punto> poly;
	public Punto centro;
	
	
	public Trafico(String tipo_trafico, ArrayList<Punto> poly,Punto centro) 
	{
		this.tipo_trafico = tipo_trafico;
		this.poly = poly;
		this.centro=centro;
	}
	
	
}
