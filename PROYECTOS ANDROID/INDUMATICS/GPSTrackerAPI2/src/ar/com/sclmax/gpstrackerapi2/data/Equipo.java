package ar.com.sclmax.gpstrackerapi2.data;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class Equipo {
	
	private String idequipo ;
	private String nombre = "";
	private float color =  BitmapDescriptorFactory.HUE_AZURE;
	
	
	
	public Equipo(String idequipo, String nombre, float color) {
		this.idequipo = idequipo;
		this.nombre = nombre;
		this.color = color;
	}
	
	public Equipo(){
		this.idequipo = "";
		this.nombre = "";
		this.color = BitmapDescriptorFactory.HUE_AZURE;
	}
	public String getIdequipo() {
		return idequipo;
	}
	public void setIdequipo(String idequipo) {
		this.idequipo = idequipo;
	}
	public float getColor() {
		return color;
	}
	public void setColor(float color) {
		this.color = color;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
