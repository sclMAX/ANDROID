package ar.com.sclmax.indumatics.gpstracker;

import android.app.Activity;
import android.os.Bundle;


public class Mapa extends Activity implements GPSinteface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapa);


}

    @Override
    public void updateLocation(double lat, double lon) {

    }
}
