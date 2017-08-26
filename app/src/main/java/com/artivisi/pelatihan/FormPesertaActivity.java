package com.artivisi.pelatihan;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.artivisi.pelatihan.domain.Peserta;
import com.artivisi.pelatihan.service.HttpService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormPesertaActivity extends AppCompatActivity {

    EditText txtNama, txtEmail, txtTanggalLahir;
    Button btnSimpan;
    HttpService httpService = new HttpService();
    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    Peserta p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_peserta);

        txtNama = (EditText) findViewById(R.id.txt_nama);
        txtEmail = (EditText) findViewById(R.id.txt_email);
        txtTanggalLahir = (EditText) findViewById(R.id.txt_tanggal_lahir);
        btnSimpan = (Button) findViewById(R.id.btn_simpan);
        p = new Peserta();
        Bundle b = getIntent().getExtras();
        if(b.containsKey("peserta")){
            p = (Peserta) b.getSerializable("peserta");
            txtNama.setText(p.getNama());
            txtEmail.setText(p.getEmail());
            txtTanggalLahir.setText(format.format(p.getTanggalLahir()));
        }

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                p.setNama(txtNama.getText().toString());
                p.setEmail(txtEmail.getText().toString());
                try {
                    p.setTanggalLahir(format.parse(txtTanggalLahir.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new SavePesertaExecutor(p).execute();
            }
        });
    }

    private class SavePesertaExecutor extends AsyncTask<Void, Void, Object>{

        private Peserta p;

        public SavePesertaExecutor(Peserta p) {
            this.p = p;
        }

        @Override
        protected Object doInBackground(Void... voids) {
            try {
                return httpService.savePeserta(p);
            } catch (Exception e){
                String errorMsg = e.getLocalizedMessage();
                return errorMsg;
            }
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if(o instanceof String){
                String errorMsg = (String) o;
                Toast.makeText(FormPesertaActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(FormPesertaActivity.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                if(getIntent().getExtras().containsKey("peserta"))
                    getIntent().removeExtra("peserta");
                startActivity(new Intent(FormPesertaActivity.this, MainActivity.class));
                finish();
            }
        }
    }
}
