package com.example.webserviceprueba;

public class GPSreg {
	
	private String idequipo ;
	private String fecha;
	private String latitud;
	private String longitud;
	private String precision;
	private String velocidad;
	
	
	
	
	public GPSreg(String idequipo, String fecha, String latitud,
			String longitud, String precision, String velocidad) {
		super();
		this.idequipo = idequipo;
		this.fecha = fecha;
		this.latitud = latitud;
		this.longitud = longitud;
		this.precision = precision;
		this.velocidad = velocidad;
	}
	public String getIdequipo() {
		return idequipo;
	}
	public void setIdequipo(String idequipo) {
		this.idequipo = idequipo;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getLatitud() {
		return latitud;
	}
	public void setLatitud(String latitud) {
		this.latitud = latitud;
	}
	public String getLongitud() {
		return longitud;
	}
	public void setLongitud(String longitud) {
		this.longitud = longitud;
	}
	public String getPrecision() {
		return precision;
	}
	public void setPrecision(String precision) {
		this.precision = precision;
	}
	public String getVelocidad() {
		return velocidad;
	}
	public void setVelocidad(String velocidad) {
		this.velocidad = velocidad;
	}
	
	

}
