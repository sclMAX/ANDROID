package data;

public class GPSPoint {
	
	private String fecha;
	private double latitud;
	private double longitud;
	private double precision;
	private double velocidad;

	public GPSPoint(double latitud, double longitud, double precision,
			double velocidad) {
		super();
		this.fecha = Utils.getFecha();
		this.latitud = latitud;
		this.longitud = longitud;
		this.precision = precision;
		this.velocidad = velocidad;
	}

	// Getters and Setters
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

	public double getPrecision() {
		return precision;
	}

	public void setPrecision(double precision) {
		this.precision = precision;
	}

	public double getVelocidad() {
		return velocidad;
	}

	public void setVelocidad(double velocidad) {
		this.velocidad = velocidad;
	}
	// END Getters and Setters
}
