package status200.com.br.celhas.model;

import java.io.Serializable;

/**
 * Created by joao on 01/07/17.
 */

public class Cliente implements Serializable{

    public static String TABELA = "CONTATO";
    public static String ID = "_id";
    public static String NOME = "NOME";
    public static String TELEFONE = "TELEFONE";

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private long id;
    private String nome;
    private String telefone;
    private boolean checked = false;

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

    @Override
    public String toString() {
        return nome + " " + telefone;
    }
}
