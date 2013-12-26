package ar.com.sclmax.gpstrackerapi2.data;

public class WayPoint {

	private String nombre;
	private double latitud;
	private double longitud;

	public WayPoint(String nombre, double latitud, double longitud) {
		super();
		this.nombre = nombre;
		this.latitud = latitud;
		this.longitud = longitud;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
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

}
