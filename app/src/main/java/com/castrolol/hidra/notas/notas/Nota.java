package com.castrolol.hidra.notas.notas;

import android.text.Html;
import android.text.Spanned;

import org.joda.time.DateTime;

import java.util.UUID;

/**
 * Created by 'Luan on 28/11/2015.
 */
public class Nota {


    private UUID id;
    private String conteudo;
    private String titulo;
    private DateTime dataCriacao;
    private int corDaNota;


    public Nota(){
        id = UUID.randomUUID();
        dataCriacao = DateTime.now();
    }

    public Nota(UUID id){
        this.id = id;
    }

    public UUID getId() {
        return id;
    }


    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public DateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(DateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getDescricaoResumida() {
        String resumo = "";

        resumo = conteudo.replaceAll("\n", " ");

        if(resumo.length() > 30){
            resumo = resumo.substring(0, 29);
        }

        return resumo;
    }

    public String getDataLiteral() {
        return dataCriacao.toString("dd/MM/yyyy");
    }

    public int getCorDaNota() {
        return corDaNota;
    }

    public void setCorDaNota(int corDaNota) {
        this.corDaNota = corDaNota;
    }

    public Spanned toHtml(){

        String htmlString = "<h2>"+getTitulo()+"</h2>";
        htmlString +=  "<p>"+getConteudo()+"</p>";
        return Html.fromHtml(htmlString);
    }

}
