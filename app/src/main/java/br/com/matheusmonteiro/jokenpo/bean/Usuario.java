package br.com.matheusmonteiro.jokenpo.bean;

import java.io.Serializable;

public class Usuario implements Serializable {


    private String nome;
    private String email;
    private String firebase;
    private String foto;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirebase() {
        return firebase;
    }

    public void setFirebase(String firebase) {
        this.firebase = firebase;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", firebase='" + firebase + '\'' +
                ", foto='" + foto + '\'' +
                '}';
    }
}
