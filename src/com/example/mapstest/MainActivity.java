package com.example.mapstest;

import java.lang.reflect.Array;
import java.util.ArrayList;

import org.apache.http.util.LangUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.R.integer;
import android.app.AlertDialog;
import android.content.ClipData.Item;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings.ZoomDensity;
import android.widget.TextView;

public class MainActivity extends FragmentActivity  implements OnMapClickListener, OnMapLongClickListener{

	GoogleMap googleMap;
	String xdestino;
	String ydestino;
	private static final String URL = "http://10.0.0.16/servidorpuntos.php?wsdl";
	private static final String METHOD_NAME = "quemelleva";
	private static final String SOAP_ACTION = "";  
	private static final String NAMESPACE = "";
	
	private static final String METHOD_NAME_2 = "obtener_rutas";
	private static final String METHOD_NAME_3 = "obtener_cercanas";
	private static final String METHOD_NAME_4 = "obtener_zonas";
	private static final String METHOD_NAME_5 = "obtener_trafico";
	public TextView text;	
	ArrayList<Micro> micros = new ArrayList<Micro>();
	
	ArrayList<Zona> zonas = new ArrayList<Zona>();
	ArrayList<Trafico> traficos = new ArrayList<Trafico>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		googleMap = fm.getMap();
		googleMap.setMyLocationEnabled(true);
		/*PolylineOptions rectOptions = new PolylineOptions()
        .add(new LatLng(-17.35, -66.0))
        .add(new LatLng(-17.45, -66.0))  // North of the previous point, but at the same longitude
        .add(new LatLng(-17.45, -66.2))  // Same latitude, and 30km to the west
        .add(new LatLng(-17.35, -66.2))  // Same longitude, and 16km to the south
        .add(new LatLng(-17.35, -66.0)); // Closes the polyline.
		rectOptions.color(Color.RED);
		// Get back the mutable Polyline
		Polyline polyline = googleMap.addPolyline(rectOptions);*/
		text= (TextView)findViewById(R.id.tap_text);
		googleMap.setOnMapLongClickListener(new OnMapLongClickListener() {
			
			@Override
			public void onMapLongClick(LatLng point) {
				//TextView text = (TextView)findViewById(R.id.tap_text);
				//text.setText(point.latitude+"");
				googleMap.clear();
				xdestino=point.longitude+"";
				ydestino=point.latitude+"";
				LatLng NEWARK = new LatLng(point.latitude, point.longitude);

				GroundOverlayOptions newarkMap = new GroundOverlayOptions()
				        .image(BitmapDescriptorFactory.fromResource(R.drawable.bus))
				        .position(NEWARK, 200f);
				googleMap.addGroundOverlay(newarkMap);
				/*Circle circle = googleMap.addCircle(new CircleOptions()
			     .center(new LatLng(point.latitude, point.longitude))
			     .radius(100)
			     .strokeColor(Color.RED)
			     .fillColor(Color.BLUE));*/
				
			}
		});
		
		SoapObject request2 = new SoapObject(NAMESPACE, METHOD_NAME_2);
		SoapSerializationEnvelope envelope2 = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    envelope2.setOutputSoapObject(request2);


	

	    try {	
		    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	    	androidHttpTransport.call(SOAP_ACTION, envelope2);			    	
	         String resultsRequestSOAP = (String) envelope2.getResponse();
	         TextView text = (TextView)findViewById(R.id.tap_text);					
	         //text.setText(resultsRequestSOAP);
	         
	         
	         
	         JSONArray rutas_json = new JSONArray(resultsRequestSOAP);
	         
	         
	         for (int i = 0; i < rutas_json.length(); i++) {
	        	 
				JSONObject micro= rutas_json.getJSONObject(i);
				JSONArray poligono = new JSONArray(micro.getString("poly"));
				ArrayList<Punto>  poly= new ArrayList<Punto>();
				for (int j = 0; j < poligono.length(); j++) {
					JSONObject punto = poligono.getJSONObject(j);
					poly.add(new Punto(Float.parseFloat(punto.getString("x")),Float.parseFloat(punto.getString("y")) ));
				}
				Micro m = new Micro(micro.getString("linea"), micro.getString("tipo")
						, micro.getString("frecuencia")
						, micro.getString("tarifa")
						, micro.getString("horainicio")
						, micro.getString("horafin")
						, poly);
				micros.add(m);
			}
	         
	        
	        
	    } catch (Exception e) {
	    	TextView text = (TextView)findViewById(R.id.tap_text);					
	         text.setText(e.getMessage());	 
	    	//lblRespuesta.setText(e.getMessage());       
	    	//xDestino.setText(e.getMessage());	
	    }
				
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.		
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
    public void onMapLongClick(LatLng point) {
		
		TextView text = (TextView)findViewById(R.id.tap_text);
		text.setText(point.latitude+"");
		
		//Circle circle = googleMap.addCircle(new CircleOptions()
	     //.center(new LatLng(point.latitude, point.longitude))
	     //.radius(10000)
	     //.strokeColor(Color.RED)
	     //.fillColor(Color.BLUE));
    }

	@Override
	public void onMapClick(LatLng point) {
		TextView text = (TextView)findViewById(R.id.tap_text);
		text.setText(point.latitude+"");
		
	}
		
	public void pintar_rutas(ArrayList<Micro> micros_r)
	{		
        ArrayList<Paint> paints = new ArrayList<Paint>();
        Paint p = new Paint();
        p.setARGB(150, 50, 205, 50);
        
        paints.add(p);
        Paint p2 = new Paint();
        p2.setARGB(150, 205, 133, 63);
        
        paints.add(p2);
        Paint p3 = new Paint();
        p3.setARGB(150, 0, 139, 139);
        
        paints.add(p3);
        Paint p4 = new Paint();
        p4.setARGB(150, 255, 215, 0);
        
        paints.add(p4);
        
       for (int i = 0; i < micros_r.size(); i++) {
       	PolylineOptions rectOptions = new PolylineOptions();
       	ArrayList<Punto> puntos = micros_r.get(i).poly;
       	
			for (int j = 0; j < puntos.size(); j++) {
				
		        rectOptions.add(new LatLng(puntos.get(j).y,puntos.get(j).x)); // Closes the polyline.
		       
			}
			
			rectOptions.color(paints.get(i).getColor());
			// Get back the mutable Polyline
			Polyline polyline = googleMap.addPolyline(rectOptions);

			
			
			
			Circle circle = googleMap.addCircle(new CircleOptions()
		     .center(new LatLng(puntos.get(0).y,puntos.get(0).x))
		     .radius(25)
		     .strokeColor(Color.GREEN)
		     .fillColor(Color.GREEN));
			LatLng NEWARK2 = new LatLng(puntos.get(puntos.size()-1).y, puntos.get(puntos.size()-1).x);

			GroundOverlayOptions newarkMap = new GroundOverlayOptions()
			        .image(BitmapDescriptorFactory.fromResource(R.drawable.flag))
			        .position(NEWARK2, 200f);
			googleMap.addGroundOverlay(newarkMap);
			// Instantiating the class MarkerOptions to plot marker on the map
            MarkerOptions markerOptions = new MarkerOptions();
            LatLng point = new LatLng(puntos.get(0).y,puntos.get(0).x);
            markerOptions.position(point);
            String linea = micros_r.get(i).linea;
    		String tipo = micros_r.get(i).tipo;
    		String frecuencia = micros_r.get(i).frecuencia;
    		String tarifa = micros_r.get(i).tarifa;
    		String horaini = micros_r.get(i).horaini;
    		String horafin = micros_r.get(i).horafin;
    		
    		
    		markerOptions.title(tipo+": "+linea+"");
            markerOptions.snippet("Frec. "+frecuencia+", "
    		+"Bs. "+tarifa+","
    		+"De: "+horaini+"-"
    		+""+horafin+"");
            
            googleMap.addMarker(markerOptions);
            
			
		}
       
	}
	
	public void pintar_zona(ArrayList<Zona> zonas_r)
	{
		googleMap.clear();
        ArrayList<Paint> paints = new ArrayList<Paint>();
        Paint p = new Paint();
        p.setARGB(150, 255, 0, 0);
        
        paints.add(p);
        Paint p2 = new Paint();
        p2.setARGB(150, 0, 255, 0);
        
        paints.add(p2);
        Paint p3 = new Paint();
        p3.setARGB(150, 255, 255, 0);
        
        paints.add(p3);
        
        
       for (int i = 0; i < zonas_r.size(); i++) {
       	PolygonOptions rectOptions = new PolygonOptions();
       	ArrayList<Punto> puntos = zonas_r.get(i).poly;
       	for (int j = 0; j < puntos.size(); j++) {
				
		        rectOptions.add(new LatLng(puntos.get(j).y,puntos.get(j).x)); // Closes the polyline.
		       
			}
			if(zonas_r.get(i).tipo_zona.contains("Segura"))
			{
				rectOptions.fillColor(paints.get(1).getColor());
				rectOptions.strokeColor(paints.get(1).getColor());
			}
			
			if(zonas_r.get(i).tipo_zona.contains("Medianamente"))
			{
				rectOptions.fillColor(paints.get(2).getColor());
				rectOptions.strokeColor(paints.get(2).getColor());
			}
			
			if(zonas_r.get(i).tipo_zona.contains("Insegura"))
			{
				rectOptions.fillColor(paints.get(0).getColor());
				rectOptions.strokeColor(paints.get(0).getColor());
			}
			// Get back the mutable Polyline
			
			//Polyline polyline = googleMap.addPolyline(rectOptions);
			Polygon polygon = googleMap.addPolygon(rectOptions);
			
			
						
            MarkerOptions markerOptions = new MarkerOptions();
            LatLng point = new LatLng(zonas_r.get(i).centro.y,zonas_r.get(i).centro.x);
            markerOptions.position(point);
            String linea = zonas_r.get(i).tipo_zona;
    		markerOptions.title(linea);
            googleMap.addMarker(markerOptions);
            
			
		}
       
	}
	
	
	public void pintar_trafico(ArrayList<Trafico> traficos_r)
	{
		googleMap.clear();
        ArrayList<Paint> paints = new ArrayList<Paint>();
        Paint p = new Paint();
        p.setARGB(150, 50, 205, 50);
        
        paints.add(p);
        Paint p2 = new Paint();
        p2.setARGB(150, 205, 133, 63);
        
        paints.add(p2);
        Paint p3 = new Paint();
        p3.setARGB(150, 0, 139, 139);
        
        paints.add(p3);
        Paint p4 = new Paint();
        p4.setARGB(150, 255, 215, 0);
        
        
        
        paints.add(p4);
        
       for (int i = 0; i < traficos_r.size(); i++) {
       	PolygonOptions polygonOptions = new PolygonOptions();
       	ArrayList<Punto> puntos = traficos_r.get(i).poly;
       	
			for (int j = 0; j < puntos.size(); j++) {
				
		        polygonOptions.add(new LatLng(puntos.get(j).y,puntos.get(j).x));
		       
			}
			polygonOptions.fillColor(paints.get(i%4).getColor());
			polygonOptions.strokeColor(paints.get(i%4).getColor());
			Polygon polygon = googleMap.addPolygon(polygonOptions);
		}
       
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection		
        text.setText("");
	    switch (item.getItemId()) {
	    case R.id.que_me_lleva:
	    	{	
	    		Boolean entro=false;
	    		do{
	    			entro=false;
	    	SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);	   
		    request.addProperty("xorigen", googleMap.getMyLocation().getLongitude()+"");
		    request.addProperty("yorigen", googleMap.getMyLocation().getLatitude()+"");
		    request.addProperty("xdestino",xdestino);
		    request.addProperty("ydestino", ydestino);
		    request.addProperty("distancia", "500");
		    
		    //Envelope SOAP
		    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		    envelope.setOutputSoapObject(request);



		    String[] lineas = {};
		    try {	
			    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
		    	androidHttpTransport.call(SOAP_ACTION, envelope);			    	
		         String resultsRequestSOAP = (String) envelope.getResponse();
		          lineas= resultsRequestSOAP.split(",");
		         text.setText(resultsRequestSOAP+" "+googleMap.getMyLocation().getLatitude()+" "+googleMap.getMyLocation().getLongitude()+" "+xdestino+" "+ydestino);	       			    			        
		         
		         ArrayList<Micro> micros2 = new ArrayList<Micro>();
				    for (int i = 0; i < lineas.length; i++) {
						for (int j = 0; j < micros.size(); j++) {
							if(lineas[i].equals(micros.get(j).linea))
								micros2.add(micros.get(j));
							
								
						}
					}
			    	pintar_rutas(micros2);	    		    
			    	xdestino="0";
			    	ydestino="0";
			    	
		    } catch (Exception e) {
		    	entro=true;
		         //text.setText(e.getMessage());	 
		    	//lblRespuesta.setText(e.getMessage());       
		    	//xDestino.setText(e.getMessage());	
		    }		    	    		    	
	    		}while(entro);
	    		return true;
	    	}
	    case R.id.todas_rutas:
	    	googleMap.clear();
	    	pintar_rutas(micros);
	    	
	    	
	    	return true;
	    case R.id.obtener_cercanas:
	    {
	    	Boolean entro=false;
    		do{
    			entro=false;
    			googleMap.clear();
	    	SoapObject request3 = new SoapObject(NAMESPACE, METHOD_NAME_3);	  
	    	SoapSerializationEnvelope envelope3 = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		    envelope3.setOutputSoapObject(request3);
		    request3.addProperty("x", googleMap.getMyLocation().getLongitude()+"");
		    request3.addProperty("y", googleMap.getMyLocation().getLatitude()+"");
		    String[] lineas2 = {};
	    	try {	
			    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
		    	androidHttpTransport.call(SOAP_ACTION, envelope3);			    	
		         String resultsRequestSOAP = (String) envelope3.getResponse();		         
		          lineas2= resultsRequestSOAP.split(",");
		         text.setText(resultsRequestSOAP+" "+googleMap.getMyLocation().getLatitude()+" "+googleMap.getMyLocation().getLongitude()+" "+xdestino+" "+ydestino);	       			    			        
		         ArrayList<Micro> micros3 = new ArrayList<Micro>();
				    for (int i = 0; i < lineas2.length; i++) {
						for (int j = 0; j < micros.size(); j++) {
							if(lineas2[i].equals(micros.get(j).linea))
								micros3.add(micros.get(j));
							
								
						}
					}
			    	pintar_rutas(micros3);
		    } catch (Exception e) {
		    	entro=true;
		         //text.setText(e.getMessage());	 
		    	//lblRespuesta.setText(e.getMessage());       
		    	//xDestino.setText(e.getMessage());	
		    }	    	
    		}while(entro);
	    	return true;
	    }
	    case R.id.tipo_zona:
	    {
	    	Boolean entro=false;
    		do{
    			entro=false;
	    	SoapObject request4 = new SoapObject(NAMESPACE, METHOD_NAME_4);	  
	    	SoapSerializationEnvelope envelope4 = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		    envelope4.setOutputSoapObject(request4);
		    
		    try {	
			    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
		    	androidHttpTransport.call(SOAP_ACTION, envelope4);			    	
		         String resultsRequestSOAP = (String) envelope4.getResponse();		         
		         //text.setText("Hola "+resultsRequestSOAP);	     
		         
		         JSONArray zonas_json = new JSONArray(resultsRequestSOAP);	
		         for (int i = 0; i < zonas_json.length(); i++) {
					JSONObject zona_json = zonas_json.getJSONObject(i);
					ArrayList<Punto> poly = new ArrayList<Punto>();
					JSONArray poly_json = new JSONArray( zona_json.getString("poly"));
					//text.setText(poly_json.length());
					for (int j = 0; j < poly_json.length(); j++) {
						JSONObject punto_json = poly_json.getJSONObject(j);
						poly.add(new Punto(Float.parseFloat( punto_json.getString("x")), Float.parseFloat( punto_json.getString("y"))));
						//text.setText(punto_json.getString("x"));						
					}
					JSONObject punto_centro_json=zona_json.getJSONObject("puntocentro");					
					Punto centro=new Punto(Float.parseFloat(punto_centro_json.getString("x")),Float.parseFloat(punto_centro_json.getString("y")));					
					Zona zona = new Zona(zona_json.getString("tipozona"), poly,centro);
					zonas.add(zona);
				}
		        pintar_zona(zonas);
		         
		        
		    } catch (Exception e) {	
		    	entro=true;
		         //text.setText(e.getMessage());	 
		    	//lblRespuesta.setText(e.getMessage());       
		    	//xDestino.setText(e.getMessage());	
		    }
    		}while(entro);
	    	return true;
	    }
	    case R.id.tipo_trafico:
	    {
	    	Boolean entro=false;
    		do{
    			entro=false;
	    	SoapObject request5 = new SoapObject(NAMESPACE, METHOD_NAME_5);	  
	    	SoapSerializationEnvelope envelope5 = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		    envelope5.setOutputSoapObject(request5);
		    
		    try {	
			    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
		    	androidHttpTransport.call(SOAP_ACTION, envelope5);			    	
		         String resultsRequestSOAP = (String) envelope5.getResponse();		 
		         JSONArray traficos_json = new JSONArray(resultsRequestSOAP);	
		         for (int i = 0; i < traficos_json.length(); i++) {
					JSONObject trafico_json = traficos_json.getJSONObject(i);
					ArrayList<Punto> poly = new ArrayList<Punto>();
					JSONArray poly_json = new JSONArray( trafico_json.getString("poly"));
					//text.setText(poly_json.length());
					for (int j = 0; j < poly_json.length(); j++) {
						JSONObject punto_json = poly_json.getJSONObject(j);
						poly.add(new Punto(Float.parseFloat( punto_json.getString("x")), Float.parseFloat( punto_json.getString("y"))));
						//text.setText(punto_json.getString("x"));
					}
					JSONObject centro_json=trafico_json.getJSONObject("puntocentro");
					Punto centro=new Punto(Float.parseFloat(centro_json.getString("x")),Float.parseFloat(centro_json.getString("y")));
					 Trafico trafico = new Trafico(trafico_json.getString("tipotrafic"), poly,centro);
					traficos.add(trafico);
				}
		        pintar_trafico(traficos);
		         
		        
		    } catch (Exception e) {	
		    	entro=true;
		         //text.setText(e.getMessage());	 
		    	//lblRespuesta.setText(e.getMessage());       
		    	//xDestino.setText(e.getMessage());	
		    }
    		}while(entro);
	    	return true;
	    }
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
}
