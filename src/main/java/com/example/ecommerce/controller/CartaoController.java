package com.example.ecommerce.controller;

import com.example.ecommerce.model.Cartao;
import com.example.ecommerce.repository.CartaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Cartao")
public class CartaoController {

    @Autowired
    private CartaoRepository creditCardRepository;

    @GetMapping
    public List<Cartao> listCreditCards() {
        return creditCardRepository.findAll();
    }

    @PostMapping
    public Cartao createCreditCard(@RequestBody Cartao creditCard) {
        return creditCardRepository.save(creditCard);
    }

    @GetMapping("/{id}")
    public Cartao getCreditCardById(@PathVariable Long id) {
        return creditCardRepository.findById(id).orElseThrow(() -> new RuntimeException("Cartão de crédito não encontrado"));
    }
}