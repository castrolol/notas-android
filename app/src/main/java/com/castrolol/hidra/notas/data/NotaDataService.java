package com.castrolol.hidra.notas.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.castrolol.hidra.notas.notas.Nota;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by 'Luan on 28/11/2015.
 */
public class NotaDataService {

    public final String ID = "Id";
    public final String TITULO = "Titulo";
    public final String CONTEUDO = "Conteudo";
    public final String COR = "Cor";
    public final String DATA_CRIACAO = "DataCriacao";
    public final String TABLE = "Nota";

    public final String[] ALL = new String[]{
            ID,
            TITULO,
            CONTEUDO,
            DATA_CRIACAO,
            COR
    };

    public final Database db;

    public NotaDataService(Context context){
        db = new Database(context);
    }

    public void addNota(Nota nota){

        ContentValues values = new ContentValues();

        values.put(ID, nota.getId().toString());
        values.put(CONTEUDO, nota.getConteudo());
        values.put(TITULO, nota.getTitulo());
        values.put(DATA_CRIACAO, nota.getDataCriacao().toString());
        values.put(COR, nota.getCorDaNota());

        SQLiteDatabase sqlite = db.getWritableDatabase();

        sqlite.insert(TABLE, null, values);

    }

    public void updateNota(UUID notaId, Nota nota){

        ContentValues values = new ContentValues();


        values.put(CONTEUDO, nota.getConteudo());
        values.put(TITULO, nota.getTitulo());
        values.put(DATA_CRIACAO, nota.getDataCriacao().toString());
        values.put(COR, nota.getCorDaNota());

        SQLiteDatabase sqlite = db.getWritableDatabase();

        sqlite.update(TABLE, values,  ID + " = ? ", new String[]{notaId.toString()});

    }

    public void removeNota(Nota nota){
        removeNota(nota.getId());
    }

    public void removeNota(UUID id){
        removeNota(id.toString());
    }
    public void removeNota(String id){

        SQLiteDatabase sqlite = db.getWritableDatabase();
        sqlite.delete(TABLE,  ID + " = ? ", new String[]{id});

    }

    public List<Nota> getNotas(Integer color){

        SQLiteDatabase sqlite = db.getReadableDatabase();

        String where = null;

        if (color != null){
            where = COR + " = " + color;
        }

        Cursor cursor = sqlite.query(TABLE, ALL, where, null, null, null, null);

        return resolveCursor(cursor);
    }

    private List<Nota> resolveCursor(Cursor cursor){
        List<Nota> notas = new ArrayList<>();
        while(cursor.moveToNext()){
            String id = cursor.getString(cursor.getColumnIndex(ID));
            Nota nota = new Nota(UUID.fromString(id));
            nota.setTitulo(cursor.getString(cursor.getColumnIndex(TITULO)));
            nota.setConteudo(cursor.getString(cursor.getColumnIndex(CONTEUDO)));
            String data = cursor.getString(cursor.getColumnIndex(DATA_CRIACAO));
            nota.setDataCriacao(DateTime.parse(data));
            nota.setCorDaNota(cursor.getInt(cursor.getColumnIndex(COR)));
            notas.add(nota);
        }

        cursor.close();

        return notas;
    }

    public void removeAllNotas() {
        SQLiteDatabase sqlite = db.getWritableDatabase();

        sqlite.delete(TABLE, null, null);
    }

    public Nota getNotaById(UUID notaId) {

        SQLiteDatabase sqlite = db.getReadableDatabase();

        Cursor cursor = sqlite.query(TABLE, ALL, ID + " = ? ", new String[]{ notaId.toString() }, null, null, null);

        List<Nota> notas = resolveCursor(cursor);

        if(notas != null && notas.size() > 0) return notas.get(0);

        return null;


    }
}
