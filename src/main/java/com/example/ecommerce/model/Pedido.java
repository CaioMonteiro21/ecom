package com.example.ecommerce.model;



import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataCriacao = LocalDateTime.now(); // Data e hora do pedido
    private Double valorTotal; // Valor total do pedido

    @ManyToOne
    private User usuario; // Relacionamento com o usuário que fez o pedido

    @ManyToMany
    private List<Produto> produtos; // Lista de produtos comprados no pedido

    // Construtores
    public Pedido() {}

    public Pedido(Double valorTotal, User usuario, List<Produto> produtos) {
        this.valorTotal = valorTotal;
        this.usuario = usuario;
        this.produtos = produtos;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }
}