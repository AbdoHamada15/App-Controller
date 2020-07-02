package com.abdo.new_app;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class Direction extends AppCompatActivity {
    //Navigation
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;
    //Direction
    Button forward_btn, left_btn, right_btn, reverse_btn, stop;
    //ImageView menuIcon;
    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    private BluetoothSocket socket;
    private OutputStream outputStream;
    private boolean isBtConnected = false;
    String command;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s, Toast.LENGTH_LONG).show();
    }
    private class ConnectBT extends AsyncTask<Void, Void, Void> // UI thread
    {
        private boolean ConnectSuccess = true;
        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(Direction.this, "Connecting...", "Please wait!!!");
        }
        @Override
        protected Void doInBackground(Void... devices)
        {
            try
            {
                if (socket == null || !isBtConnected)
                {myBluetooth = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice device = myBluetooth.getRemoteDevice(address);
                    socket = device.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    socket.connect();
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);
            if (!ConnectSuccess)
            {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            }
            else
            {
                msg("Connected.");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent newint = getIntent();
        address = newint.getStringExtra(Main.EXTRA_ADDRESS);
        setContentView(R.layout.activity_direction);
        //Direction Icons
        forward_btn=findViewById(R.id.forward_btn);
        reverse_btn=findViewById(R.id.reverse_btn);
        stop=findViewById(R.id.stop);
        right_btn=findViewById(R.id.right_btn);
        left_btn=findViewById(R.id.left_btn);
        //Nav
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        //
        navigationDrawer();
        forward_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                command = "1";
                try {
                    outputStream.write(command.getBytes()); //transmits the value of command to the bluetooth module
                    msg("Front");
                }
                catch (IOException e) {
                    e.printStackTrace();
                    msg("Error");
                }
            }
        });
        reverse_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                command = "2";
                try {
                    outputStream.write(command.getBytes()); //transmits the value of command to the bluetooth module
                    msg("Reverse");
                }
                catch (IOException e) {
                    e.printStackTrace();
                    msg("Error");
                }
            }
        });
        left_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                command = "3";
                try {
                    outputStream.write(command.getBytes()); //transmits the value of command to the bluetooth module
                    msg("Left");
                }
                catch (IOException e) {
                    e.printStackTrace();
                    msg("Error");
                }
            }
        });
        right_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                command = "4";
                try {
                    outputStream.write(command.getBytes()); //transmits the value of command to the bluetooth module
                    msg("Right");
                }
                catch (IOException e) {
                    e.printStackTrace();
                    msg("Error");
                }
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                command = "5";
                try {
                    outputStream.write(command.getBytes()); //transmits the value of command to the bluetooth module
                    msg("Stop");
                }
                catch (IOException e) {
                    e.printStackTrace();
                    msg("Error");
                }
            }
        });
    }
    private void navigationDrawer() {
        navigationView.bringToFront();
        mToggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setCheckedItem(R.id.nav_direction);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_direction:
                        msg("Direction");
                        break;
                    case R.id.nav_voice:
                        Intent intent= new Intent(Direction.this,Voice.class);
                        startActivity(intent);
                        msg("Voice");
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerVisible(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START); }
        else  super.onBackPressed();
    }
}