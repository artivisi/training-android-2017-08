package com.artivisi.pelatihan;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.artivisi.pelatihan.domain.Peserta;
import com.artivisi.pelatihan.service.HttpService;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView lvPeserta;
    ArrayAdapter<Peserta> adapter;
    HttpService httpService = new HttpService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvPeserta = (ListView) findViewById(R.id.lv_peserta);
        adapter = new ArrayAdapter<Peserta>(this, android.R.layout.simple_dropdown_item_1line);
        lvPeserta.setAdapter(adapter);
        new GetPesertaExecutor().execute();

        lvPeserta.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Peserta peserta = adapter.getItem(i);
                Intent intent = new Intent(MainActivity.this, FormPesertaActivity.class);
                intent.putExtra("peserta", peserta);
                startActivity(intent);
                finish();
            }
        });

        lvPeserta.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Peserta peserta = adapter.getItem(i);
                new DeletePesertaExecutor(peserta.getId()).execute();
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
                startActivity(new Intent(this, FormPesertaActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private class GetPesertaExecutor extends AsyncTask<Void, Void, Object>{

        @Override
        protected Object doInBackground(Void... voids) {
            try {
                return httpService.getAllPeserta();
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
                Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            } else {
                List<Peserta> listPeserta = (List<Peserta>) o;
                adapter.clear();
                adapter.addAll(listPeserta);
                adapter.notifyDataSetChanged();
            }
        }
    }

    private class DeletePesertaExecutor extends AsyncTask<Void, Void, Object>{
        private String id;

        public DeletePesertaExecutor(String id) {
            this.id = id;
        }

        @Override
        protected Object doInBackground(Void... voids) {
            try {
                return httpService.deletePeserta(id);
            } catch (Exception e) {
                String errorMsg = e.getLocalizedMessage();
                return errorMsg;
            }
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if(o instanceof String){
                String errorMsg = (String) o;
                Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Data berhasil di hapus", Toast.LENGTH_SHORT).show();
                new GetPesertaExecutor().execute();
            }
        }
    }
}
