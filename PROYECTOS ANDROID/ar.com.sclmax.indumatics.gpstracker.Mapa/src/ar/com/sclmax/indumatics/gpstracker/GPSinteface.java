package ar.com.sclmax.indumatics.gpstracker;

public interface GPSinteface {
	public void updateLocation(double lat, double lon, String titulo, String info, int icono);
	public void updateLocationSMS(String tel, double lat, double lon, String titulo, String info);

}
