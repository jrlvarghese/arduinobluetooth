package com.example.android.arduinobluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity
{
    private int REQUEST_ENABLE_BT = 1;
    //widgets
    Button btnPaired;
    ListView deviceList;
    //Bluetooth
    private BluetoothAdapter mBtAdapter = null;
    private Set<BluetoothDevice> pairedDevices;
    public static String EXTRA_ADDRESS = "device_address";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Calling widgets
        btnPaired = (Button)findViewById(R.id.button);
        deviceList = (ListView)findViewById(R.id.listView);

        // First step is to get the bluetooth adapter
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBtAdapter == null){
            // Device doesn't support bluetooth
            Toast.makeText(this,"Device doesn't support bluetooth!!", Toast.LENGTH_LONG).show();
        }

        // Second step is to enable bluetooth OR check whether the bluetooth is enabled or not
        if(!mBtAdapter.isEnabled()){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        btnPaired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                pairedDevicesList();
            }
        });

//        // Query the list of already paired devices
//        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
//
//        if (pairedDevices.size() > 0) {
//            // There are paired devices. Get the name and address of each paired device.
//            for (BluetoothDevice device : pairedDevices) {
//                String deviceName = device.getName();
//                String deviceHardwareAddress = device.getAddress(); // MAC address
//            }
//        }
    }

    private void pairedDevicesList()
    {
        // pairedDevices is a Set (which means, it contains no duplicate elements
        pairedDevices = mBtAdapter.getBondedDevices();
        // Initialise a new ArrayList to hold the name and address of the bluetooth devices
        ArrayList list = new ArrayList();

        // If there are paired devices existing then add the name and address of the paired devices to the arraylist
        if (pairedDevices.size()>0){
            // Loop through all the elements
            for(BluetoothDevice bt : pairedDevices)
            {
                //Get the device's name and the address and add to the list
                list.add(bt.getName() + "\n" + bt.getAddress());
            }
        }
        else{
            // If there is no paired devices present show a toast message about the same
            Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
        }

        // Get array adapter and attach the list to the array adapter
        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
        deviceList.setAdapter(adapter);
        // Attach an onClick listener such that clicking on the device name and address will lead to start a new activity
        deviceList.setOnItemClickListener(myListClickListener); //Method called when the device from the list is clicked

    }

    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener()
    {
        public void onItemClick (AdapterView<?> av, View v, int arg2, long arg3)
        {
            // Get the device MAC address, the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);

            // Make an intent to start next activity.
            Intent i = new Intent(MainActivity.this, LedControl.class);

            //Change the activity.
            i.putExtra(EXTRA_ADDRESS, address); //this will be received at ledControl (class) Activity
            startActivity(i);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
