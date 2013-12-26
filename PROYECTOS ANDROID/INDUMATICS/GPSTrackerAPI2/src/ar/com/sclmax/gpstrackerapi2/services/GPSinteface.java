package ar.com.sclmax.gpstrackerapi2.services;

import ar.com.sclmax.gpstrackerapi2.data.CamionPos;

public interface GPSinteface {
	public void updateLocation(CamionPos pos);
	public void updateLocationSMS(CamionPos pos);
	public void setNotification(CharSequence titulo, CharSequence msg);

}
