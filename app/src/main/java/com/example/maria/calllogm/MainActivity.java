package com.example.maria.calllogm;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button btnTrucades;
    TextView txtLlamadas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnTrucades = (Button) findViewById(R.id.btnMostrar);
        txtLlamadas = (TextView) findViewById(R.id.txtTrucades);

        btnTrucades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri lastCall = CallLog.Calls.CONTENT_URI;
                ContentResolver cr = getContentResolver();

                String[] projection = new String[]{
                        CallLog.Calls.TYPE,
                        CallLog.Calls.NUMBER,
                        CallLog.Calls.CACHED_NAME,
                        CallLog.Calls.DURATION
                };
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Cursor c = cr.query(lastCall, projection, null, null, null);

                int colTipo = c.getColumnIndex(CallLog.Calls.TYPE);
                int colNumber = c.getColumnIndex(CallLog.Calls.NUMBER);

                String tipoLlamada = "Llamada";
                String telefono;

                if (c.moveToFirst()) {

                    do {
                        int tipo = c.getInt(colTipo);
                        telefono = c.getString(colNumber);

                        if (tipo == CallLog.Calls.INCOMING_TYPE) {
                            tipoLlamada = "Entrada";
                        } else if (tipo == CallLog.Calls.OUTGOING_TYPE) {
                            tipoLlamada = "Salida";
                        } else if (tipo == CallLog.Calls.MISSED_TYPE) {
                            tipoLlamada = "Perdida";
                        }

                    }while(c.moveToNext());

                    txtLlamadas.append(tipoLlamada + " - " + telefono + "\n");
                }

            }
        });
    }
}
