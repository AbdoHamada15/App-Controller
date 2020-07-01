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

public class Direction extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{
    //Navigation
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    //Direction
    Button forward_btn, left_btn, right_btn, reverse_btn, stop;
    ImageView menuIcon;
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
        menuIcon= findViewById(R.id.menu);
        //textView2 =findViewById(R.id.textView2);
        //try { t1.setText("\nBT Address: "+address); }
        //catch(Exception e){}
        BitmapFactory.Options options= new BitmapFactory.Options();
        options.inSampleSize= 8;

        //Nav
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        //


        navigationDrawer();
        //OnTouchListener code for the forward button (button long press)
        forward_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) //MotionEvent.ACTION_DOWN is when you hold a button down
                {
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
                else if(event.getAction() == MotionEvent.ACTION_UP) //MotionEvent.ACTION_UP is when you release a button
                {
                    command = "10";
                    try {
                        outputStream.write(command.getBytes());
                    }
                    catch(IOException e) {
                        e.printStackTrace();
                        msg("Error");
                    }
                }
                return false;
            }

        });
        //OnTouchListener code for the reverse button (button long press)
        reverse_btn.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    command = "2";
                    try {
                        outputStream.write(command.getBytes());
                        msg("Reverse");
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                        msg("Error");
                    }
                }
                else if(event.getAction() == MotionEvent.ACTION_UP) {
                    command = "10";
                    try {
                        outputStream.write(command.getBytes());
                    }
                    catch(IOException e) {
                        msg("Error");
                    }
                }
                return false;
            }
        });
        //OnTouchListener code for the forward left button (button long press)
        left_btn.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    command = "3";
                    try {
                        outputStream.write(command.getBytes());
                        msg(" Left");
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                        msg("Error");
                    }
                }
                else if(event.getAction() == MotionEvent.ACTION_UP) {
                    command = "10";
                    try {
                        outputStream.write(command.getBytes());
                    }
                    catch(IOException e) {
                        msg("Error");
                    }
                }
                return false;
            }
        });
        //OnTouchListener code for the forward right button (button long press)
        right_btn.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    command = "4";
                    try {
                        outputStream.write(command.getBytes());
                        msg(" Right");
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                        msg("Error");
                    }
                }
                else if(event.getAction() == MotionEvent.ACTION_UP) {
                    command = "10";
                    try {
                        outputStream.write(command.getBytes());
                    }
                    catch(IOException e) {
                        e.printStackTrace();
                        msg("Error");
                    }
                }
                return false;
            }
        });
        //OnTouchListener code for the sTop button (button long press)
        stop.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    command = "5";
                    try {
                        outputStream.write(command.getBytes());
                        msg("Stop");
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                        msg("Error");
                    }
                }
                else if(event.getAction() == MotionEvent.ACTION_UP) {
                    command = "10";
                    try {
                        outputStream.write(command.getBytes());
                    }
                    catch(IOException e) {
                        msg("Error");
                    }
                }
                return false;
            }
        });
    }
    private void navigationDrawer() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_direction);
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawerLayout.isDrawerVisible(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START); }
            else drawerLayout.openDrawer(GravityCompat.START);
            }
        });
       // ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.nav_drawer_open,R.string.nav_drawer_close);
       // drawerLayout.addDrawerListener(toggle);
       // toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerVisible(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START); }
        else  super.onBackPressed();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
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
}