package com.example.ecommerce.controller;


import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users") // Rota base do controlador
public class UserController {

    @Autowired // Injeta o UserRepository automaticamente
    private UserRepository userRepository;

    // Método para listar todos os usuários
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Retorna 204 se a lista estiver vazia
        }
        return new ResponseEntity<>(users, HttpStatus.OK); // Retorna os usuários com status 200
    }

    // Método para criar um novo usuário
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            User newUser = userRepository.save(user);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED); // Retorna status 201 Created
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); // Retorna 500 em caso de erro
        }
    }

    // Método para buscar um usuário pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK)) // Retorna o usuário encontrado com status 200
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND)); // Retorna 404 se não for encontrado
    }

    // Método para atualizar um usuário
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setName(userDetails.getName());
                    user.setEmail(userDetails.getEmail());
                    user.setPassword(userDetails.getPassword());
                    user.setCreditBalance(userDetails.getCreditBalance());
                    User updatedUser = userRepository.save(user);
                    return new ResponseEntity<>(updatedUser, HttpStatus.OK); // Retorna usuário atualizado com status 200
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND)); // Retorna 404 se não for encontrado
    }

    // Método para deletar um usuário
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        try {
            userRepository.deleteById(id);
            return new ResponseEntity<>("Usuário deletado com sucesso!", HttpStatus.OK); // Retorna 200 OK
        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao deletar o usuário.", HttpStatus.INTERNAL_SERVER_ERROR); // Retorna 500 em caso de erro
        }
    }


}