package com.example.ecommerce.repository;


import com.example.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Método para buscar usuário pelo email
    Optional<User> findByEmail(String email);

    // Outros métodos específicos podem ser adicionados aqui
}