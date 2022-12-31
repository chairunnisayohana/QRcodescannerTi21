package com.example.qrcodescannerti21;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrcode.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //View object
    private Button buttonScanning;
    private TextView textViewName, textViewClass, textViewId;

    //qr code scanner object
    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //View object
        buttonScanning = (Button)findViewById(R.id.buttonscan);
        textViewName = (TextView)findViewById(R.id.textViewNama) ;
        textViewClass= (TextView) findViewById(R.id.textViewKelas);
        textViewId = (TextView)findViewById(R.id.textViewNim);

        //inisialisasi scan object
        qrScan = new IntentIntegrator(this);

        //implementasi onClickListener
        buttonScanning.setOnClickListener(this);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result != null){
            //jika result code scanner tidak sama sekali
            if(result.getContents()== null){
                Toast.makeText(this,  "Hasil scan tidak ada", Toast.LENGTH_LONG).show();
            }else if (Patterns.WEB_URL.matcher(result.getContents()).matches()) {
                Intent visitUrl = new Intent(Intent.ACTION_VIEW, Uri.parse(result.getContents()));
                startActivity(visitUrl);
            } else if (Patterns.PHONE.matcher(result.getContents()).matches()) {
                String telp = String.valueOf(result.getContents());
                Intent Intent = new Intent(android.content.Intent.ACTION_DIAL, Uri.parse("tel:" + telp));
                Intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(Intent);
                try {
                    startActivity(Intent.createChooser(Intent, "waiting"));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(MainActivity.this, "no phone apk installed", Toast.LENGTH_SHORT).show();
                }
            }
            else{

                try {
                    JSONObject obj = new JSONObject(result.getContents());

                    textViewName.setText(obj.getString("nama"));
                    textViewClass.setText(obj.getString("kelas"));
                    textViewId.setText(obj.getString("NIM"));
                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(this, result.getContents(),Toast.LENGTH_LONG).show();
                }
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View v) {
        //inisialisasi qrcode scanning
        qrScan.initiateScan();


    }
}