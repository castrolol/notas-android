package com.castrolol.hidra.notas;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.core.view.MenuItemCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.castrolol.hidra.notas.data.NotaShareService;
import com.castrolol.hidra.notas.notas.Nota;
import com.castrolol.hidra.notas.notas.NotaRepository;
import com.castrolol.hidra.notas.ui.NotaColorizer;

import java.util.UUID;

public class EditarNotaActivity extends AppCompatActivity {

    UUID notaId;
    Nota nota;
    NotaColorizer colorizer;
    EditText txtTitulo;
    EditText txtConteudo;
    LinearLayout notaLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_nota);
        colorizer = new NotaColorizer(this);

        notaLayout = (LinearLayout)findViewById(R.id.nota_editar_layout);
        txtTitulo = (EditText)findViewById(R.id.edit_titulo);
        txtConteudo = (EditText)findViewById(R.id.edit_conteudo);

        txtTitulo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                updateShareIntent();
                return false;
            }
        });

        txtConteudo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                updateShareIntent();
                return false;
            }
        });




        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }catch (Exception e){}

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        String notaId = extras.getString("notaId");

        if(notaId == null){
            initializeNova();
        }else{
            initializeEdicao(notaId);
        }


    }

    ShareActionProvider shareActionProvider;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.editar, menu);

        MenuItem item = menu.findItem(R.id.menu_item_share);



        if(notaId == null){
            item.setVisible(false);
        }else {
            shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider( item);
            updateShareIntent();
        }

        return true;
    }

    private void updateShareIntent() {
        shareActionProvider.setShareIntent(NotaShareService.fromNota(nota));
    }


    private void initializeEdicao(String notaId) {
        setTitle("Editar Nota");
        this.notaId = UUID.fromString(notaId);
        nota = NotaRepository.getInstance(this).getById(this.notaId);




        txtTitulo.setText(nota.getTitulo());
        txtConteudo.setText(nota.getConteudo());

        atualizarCor();

    }

    private void initializeNova() {
        setTitle("Nova Nota");
        nota = new Nota();

    }


    private void saveNota() {

        String titulo = txtTitulo.getText().toString();
        String conteudo = txtConteudo.getText().toString();

        if(titulo == null || "".equals(titulo.trim())){
            titulo = "Nota Sem Titulo";
        }

        nota.setTitulo(titulo);
        nota.setConteudo(conteudo);

        if(notaId == null){
            NotaRepository.getInstance(this).add(nota);
        }else{
            NotaRepository.getInstance(this).update(notaId, nota);
        }

        onBackPressed();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_remove:
                deleteNota();
                return true;
            case R.id.action_accept:
                saveNota();
                return true;
            case R.id.color_blue:
                mudarCor(NotaColorizer.BLUE);
                return true;
            case R.id.color_green:
                mudarCor(NotaColorizer.GREEN);
                return true;
            case R.id.color_purple:
                mudarCor(NotaColorizer.PURPLE);
                return true;
            case R.id.color_red:
                mudarCor(NotaColorizer.RED);
                return true;
            case R.id.color_white:
                mudarCor(NotaColorizer.WHITE);
                return true;
            case R.id.color_yellow:
                mudarCor(NotaColorizer.YELLOW);
                return true;



            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteNota() {
        final Nota notaARemover = nota;


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Remover Nota '" + nota.getTitulo() + "'");
        builder.setMessage("Deseja realmente remover esta nota?");
        builder.setPositiveButton("Remover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                NotaRepository.getInstance(EditarNotaActivity.this).remove(notaARemover);
                onBackPressed();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.show();

    }

    private void mudarCor(int color) {
        nota.setCorDaNota(color);
        atualizarCor();
    }

    private void atualizarCor() {
        colorizer.changeViewColor(notaLayout, nota.getCorDaNota());
    }

}
