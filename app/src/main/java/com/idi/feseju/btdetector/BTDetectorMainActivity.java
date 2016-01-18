package com.idi.feseju.btdetector;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;

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
import android.widget.Toast;


/**
 * Prototipo
 * Actividad principal para listar los dispositivos Bluetooth
 */

public class BTDetectorMainActivity extends AppCompatActivity {

    private BluetoothAdapter mBluetoothAdapter;

    /**
     * Old Good MAIN
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Layout load
        setContentView(R.layout.activity_btdetector_main);

        ///////////////////////////////////////////////////////////////////
        ///Logic START


        /*
        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }
        */

        /// Obtenemos el adaptador bluetooth
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                // Bluetooth is not enabled :)
                //Toast.makeText(this, R.string.error_bluetooth_not_enabled, Toast.LENGTH_SHORT).show();

                new AlertDialog.Builder( this )
                        .setTitle("Bluetooth no está activado")
                        .setMessage("Active Bluetooth y reinicie la aplicación")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Open the Bluetooth setting to enable bluetooth
                                Intent intentOpenBluetoothSettings = new Intent();
                                intentOpenBluetoothSettings.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
                                startActivity(intentOpenBluetoothSettings);
                                finish();
                            }
                        })

                        //.setNegativeButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        //    public void onClick(DialogInterface dialog, int which) {
                        //    }
                        //})
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }


        ///Registro del receiver
        registerReceiver(receiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));


        Button boton = (Button) findViewById(R.id.BtdetectorMainButtonDetectar);
        boton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mBluetoothAdapter.startDiscovery();
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
        if ( mBluetoothAdapter.isDiscovering() ) {
            mBluetoothAdapter.cancelDiscovery();
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