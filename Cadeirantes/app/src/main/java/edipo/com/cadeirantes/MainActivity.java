package edipo.com.cadeirantes;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private TextView tvLatitude;
    private TextView tvLongitude;
    private TextView tvEixoX;

    private Button btnVerBuraco;
    private Button btnReportarBuraco;

    private Sensor accelerometer;
    private SensorManager sensorManager;

    private Localizacao local = null;

    private float sensorX;
    private float sensorY;
    private float sensorZ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Referencias
        tvLatitude = findViewById(R.id.ma_tv_latitude);
        tvLongitude = findViewById(R.id.ma_tv_longitude);
        btnVerBuraco = findViewById(R.id.ma_btn_ver_buraco);
        btnReportarBuraco = findViewById(R.id.ma_btn_reportar_buraco);
        tvEixoX = findViewById(R.id.ma_tv_eixo_x);

        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},0);

        //Firebase
        FirebaseApp.initializeApp(MainActivity.this);
        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference banco = db.getReference("localizacao");

        /* inicializando o sensor acelerometro */
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        //Clique do botao
        btnVerBuraco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity.this, MapaDeBuracos.class);
                startActivity(it);
            }
        });

        btnReportarBuraco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(local != null){
                    banco.push().setValue(local);

                    Toast.makeText(
                            getBaseContext(),
                            "Buraco/desnível informado com sucesso!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(
                            getBaseContext(),
                            "Não conseguimos a sua localização. Por favor verifique o GPS!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Se não possui permissão
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Verifica se já mostramos o alerta e o usuário negou alguma vez.
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                //Caso o usuário tenha negado a permissão anteriormente e não tenha marcado o check "nunca mais mostre este alerta"

                //Podemos mostrar um alerta explicando para o usuário porque a permissão é importante.
                Toast.makeText(
                        getBaseContext(),
                        "Você já negou antes essa permissão! "+
                                "\nPara saber a sua localização necessitamos dessa permissão!",
                        Toast.LENGTH_LONG).show();

                        /* Além da mensagem indicando a necessidade sobre a permissão,
                           podemos solicitar novamente a permissão */
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},0);
            } else {
                //Solicita a permissão
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},0);
            }

        } else {
            //Tudo OK, podemos prosseguir.

            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                    local = new Localizacao();
                    local.setLatitude(location.getLatitude());
                    local.setLongitude(location.getLongitude());

                    Log.d("logMainActivity","eixo x: "+sensorX);
                    if(sensorX > -1.0) {
                        banco.push().setValue(local);

                        Toast.makeText(
                                getBaseContext(),
                                "Buraco/desnível detectado!",
                                Toast.LENGTH_SHORT).show();

                    }

                    tvLatitude.setText("Latitude: " + location.getLatitude());
                    tvLongitude.setText("Longitude: " + location.getLongitude());
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            };

            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);

        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        sensorX = sensorEvent.values[0];
        sensorY = sensorEvent.values[1];
        sensorZ = sensorEvent.values[2];

        //Atualizando eixoX
        tvEixoX.setText("Eixo x: "+sensorX);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}