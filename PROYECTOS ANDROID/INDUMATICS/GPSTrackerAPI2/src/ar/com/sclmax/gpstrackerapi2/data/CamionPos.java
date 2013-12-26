package ar.com.sclmax.gpstrackerapi2.data;

public class CamionPos {
	
	private String id;
	private String fecha;
	private double latitud;
	private double longitud;
	private float velocidad;
	private float presicion;
	
	

	public CamionPos() {
		this.id = "";
		this.fecha = "";
		this.latitud = 0;
		this.longitud = 0;
		this.velocidad = 0;
		this.presicion = 0;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public double getLatitud() {
		return latitud;
	}

	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}

	public double getLongitud() {
		return longitud;
	}

	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}

	public float getVelocidad() {
		return velocidad;
	}

	public void setVelocidad(float velocidad) {
		this.velocidad = velocidad;
	}

	public float getPresicion() {
		return presicion;
	}

	public void setPresicion(float presicion) {
		this.presicion = presicion;
	}	
}
