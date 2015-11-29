package com.castrolol.hidra.notas.notas;

import android.content.Context;
import android.widget.ArrayAdapter;
import com.castrolol.hidra.notas.data.NotaDataService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by 'Luan on 28/11/2015.
 */
public class NotaRepository {
    private static NotaRepository instance;
    public static NotaRepository getInstance(Context context) {
        if(instance == null){
            instance = new NotaRepository(context);
        }
        return instance;
    }

    public Nota getById(UUID notaId) {

        return dataService.getNotaById(notaId);

    }

    private Integer corFiltro;

    public void filter(int cor) {

        if (cor == -1){
            corFiltro = null;
            refresh();
            return;
        }

        corFiltro = cor;
       refresh();
    }

    public void filter(String newText) {

        List<Nota> todasAsNotas = dataService.getNotas(getCorFiltro());
        List<Nota> filtradas = new ArrayList<>();

        for(Nota nota : todasAsNotas){
            if(nota.getConteudo().toLowerCase().contains(newText.toLowerCase())){
                filtradas.add(nota);
                continue;
            }
            if(nota.getTitulo().toLowerCase().contains(newText.toLowerCase())) {
                filtradas.add(nota);
                continue;
            }
        }

        notas.clear();
        notas.addAll(filtradas);
        notifyChanges();

    }

    public Integer getCorFiltro() {
        return corFiltro;
    }


    //static listener mechanism

    public interface RepositoryListener {
        void onChanged();
    }

    private static List<RepositoryListener> listeners = new ArrayList<>();

    public static void registerListener(RepositoryListener listener){
        listeners.add(listener);
    }

    //data managment

    private List<Nota> notas;
    private Context context;
    private ArrayAdapter<Nota> adapter;
    private NotaDataService dataService;

    public NotaRepository(Context context){
        notas = new ArrayList<>();
        this.context = context;
        dataService = new NotaDataService(context);
    }

    public List<Nota> getNotas(){
        return notas;
    }

    public void refresh(){
        notas.clear();
        notas.addAll(dataService.getNotas(getCorFiltro()));
        notifyChanges();
    }

    public void add(Nota nota){
        dataService.addNota(nota);
        refresh();
    }

    public void remove(Nota nota){
        dataService.removeNota(nota);
        refresh();
    }

    public void clear() {
        dataService.removeAllNotas();
        refresh();
    }


    public void update(UUID notaId, Nota nota){
        dataService.updateNota(notaId, nota);
        refresh();
    }

    //sync

    private void notifyChanges(){
        for(RepositoryListener listener : listeners){
            listener.onChanged();
        }
    }

    public void keepSyncWith(final ArrayAdapter<Nota> adapter) {

        registerListener(new RepositoryListener() {
            @Override
            public void onChanged() {
                adapter.notifyDataSetChanged();
            }
        });



    }
}
