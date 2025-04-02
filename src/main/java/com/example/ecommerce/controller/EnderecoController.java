package com.example.ecommerce.controller;

import com.example.ecommerce.model.Endereco;
import com.example.ecommerce.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Endereco")
public class EnderecoController {

    @Autowired
    private EnderecoRepository addressRepository;

    @GetMapping
    public List<Endereco> listAddresses() {
        return addressRepository.findAll();
    }

    @PostMapping
    public Endereco createAddress(@RequestBody Endereco address) {
        return addressRepository.save(address);
    }

    @GetMapping("/{id}")
    public Endereco getAddressById(@PathVariable Long id) {
        return addressRepository.findById(id).orElseThrow(() -> new RuntimeException("Endereço não encontrado"));
    }
}