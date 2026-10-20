package com.natan.barbeariabd.model;

public class Cliente {

    private String cpf;
    private String nome;
    private String rua;
    private String numero;

    public Cliente() {
    }

    public Cliente(String cpf, String nome, String rua, String numero) {
        this.cpf = cpf;
        this.nome = nome;
        this.rua = rua;
        this.numero = numero;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}