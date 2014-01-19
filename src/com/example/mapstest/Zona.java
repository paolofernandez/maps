package com.example.mapstest;

import java.util.ArrayList;

public class Zona {
	
	public String tipo_zona;
	public ArrayList<Punto> poly;
	public Punto centro;
	
	public Zona(String tipo_zona,ArrayList<Punto> poly,Punto centro)
	{
		this.tipo_zona = tipo_zona;
		this.poly = poly;
		this.centro=centro;
	}
}
