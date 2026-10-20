package com.natan.barbeariabd.model;
import java.math.BigDecimal;

public class Servico {

    private Integer idServico;
    private String nome;
    private String descricao;
    private int duracao;
    private BigDecimal preco;

    public Servico() {
    }

    public Servico(
            Integer idServico,
            String nome,
            String descricao,
            int duracao,
            BigDecimal preco
    ) {
        this.idServico = idServico;
        this.nome = nome;
        this.descricao = descricao;
        this.duracao = duracao;
        this.preco = preco;
    }

    public Integer getIdServico() {
        return idServico;
    }

    public void setIdServico(Integer idServico) {
        this.idServico = idServico;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getDuracao() {
        return duracao;
    }

    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }
}