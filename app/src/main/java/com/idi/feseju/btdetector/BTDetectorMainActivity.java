package com.idi.feseju.btdetector;

import android.os.Bundle;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;

/**
 * Actividad principal para listar los dispositivos Bluetooth
 */

public class BTDetectorMainActivity extends AppCompatActivity {

    private BluetoothAdapter BTAdapter = BluetoothAdapter.getDefaultAdapter();

    /**
     * Old Good MAIN
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Layout
        setContentView(R.layout.activity_btdetector_main);

        ///Logic START
        registerReceiver(receiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));

        Button boton = (Button) findViewById(R.id.BtdetectorMainButtonDetectar);
        boton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                BTAdapter.startDiscovery();
            }
        });

    } // End onCreate

    @Override
    protected void onDestroy() {
        stopBluetoothDiscovery();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        stopBluetoothDiscovery();
        super.onPause();
    }

    void stopBluetoothDiscovery() {
        if ( BTAdapter.isDiscovering() ) {
            BTAdapter.cancelDiscovery();
            Log.v("MAIN","Stopping Discovery");
        }
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)) {
                int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE);
                String name = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
                TextView rssi_msg = (TextView) findViewById(R.id.textOutputBluetooth);
                rssi_msg.setText(rssi_msg.getText() + name + " => " + rssi + "dBm\n");
            }
        }
    };

} //End CLASS
