package status200.com.br.celhas.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by joao on 01/07/17.
 */

public class Cliente implements Serializable{

    public static String TABELA = "CLIENTE";
    public static String ID = "_id";
    public static String NOME = "NOME";
    public static String TELEFONE = "TELEFONE";
    public static String IMAGEM = "IMAGEM";
    public  static String ANIVERSARIO = "ANIVERSARIO";

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private long id;
    private String nome;
    private String telefone;
    private String imagem;
    private Date aniversario;
    private boolean checked = false;


    public Cliente (){
        this.aniversario = new Date(2000,0,1);
    }


    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }



    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Date getAniversario() {
        return aniversario;
    }

    public void setAniversario(Date aniversario) {
        this.aniversario = aniversario;
    }

    @Override
    public String toString() {
        return nome + " " + telefone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cliente cliente = (Cliente) o;

        return telefone.equals(cliente.telefone);
    }

    @Override
    public int hashCode() {
        return telefone.hashCode();
    }
}
