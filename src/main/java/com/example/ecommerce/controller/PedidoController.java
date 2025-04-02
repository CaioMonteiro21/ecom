package com.example.ecommerce.controller;

import com.example.ecommerce.model.Pedido;
import com.example.ecommerce.model.User;
import com.example.ecommerce.model.Produto;
import com.example.ecommerce.repository.PedidoRepository;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.repository.ProdutoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos") // Base da URL para os endpoints de pedidos
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    // **Endpoint para CRIAR um pedido**
    @PostMapping
    public ResponseEntity<String> criarPedido(@RequestParam Long userId, @RequestBody List<Long> produtoIds) {
        try {
            // Busca o usuário que está fazendo o pedido
            User usuario = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            // Busca os produtos selecionados
            List<Produto> produtos = produtoRepository.findAllById(produtoIds);
            if (produtos.isEmpty()) {
                throw new RuntimeException("Nenhum produto encontrado para os IDs fornecidos.");
            }

            // Calcula o valor total do pedido
            Double valorTotal = produtos.stream().mapToDouble(Produto::getPreco).sum();

            // Verifica se o saldo do usuário é suficiente
            if (usuario.getCreditBalance() < valorTotal) {
                throw new RuntimeException("Saldo insuficiente!");
            }

            // Atualiza o saldo do usuário
            usuario.setCreditBalance(usuario.getCreditBalance() - valorTotal);
            userRepository.save(usuario);

            // Cria e salva o pedido
            Pedido pedido = new Pedido(valorTotal, usuario, produtos);
            pedidoRepository.save(pedido);

            return ResponseEntity.status(HttpStatus.CREATED).body("Pedido criado com sucesso!");

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno ao criar o pedido.");
        }
    }

    // **Endpoint para LISTAR todos os pedidos**
    @GetMapping
    public ResponseEntity<List<Pedido>> listarPedidos() {
        try {
            List<Pedido> pedidos = pedidoRepository.findAll();
            if (pedidos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Status 204 NO CONTENT
            }
            return new ResponseEntity<>(pedidos, HttpStatus.OK); // Status 200 OK
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // **Endpoint para BUSCAR pedido por ID**
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPedidoPorId(@PathVariable Long id) {
        try {
            Pedido pedido = pedidoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
            return new ResponseEntity<>(pedido, HttpStatus.OK); // Status 200 OK
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); // Status 404 NOT FOUND
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // **Endpoint para ATUALIZAR pedido**
    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarPedido(@PathVariable Long id, @RequestBody List<Long> produtoIds) {
        try {
            Pedido pedidoExistente = pedidoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

            // Busca os produtos atualizados
            List<Produto> produtosAtualizados = produtoRepository.findAllById(produtoIds);
            if (produtosAtualizados.isEmpty()) {
                throw new RuntimeException("Nenhum produto encontrado para os IDs fornecidos.");
            }

            // Recalcula o valor total
            Double novoValorTotal = produtosAtualizados.stream().mapToDouble(Produto::getPreco).sum();

            // Atualiza os detalhes do pedido
            pedidoExistente.setProdutos(produtosAtualizados);
            pedidoExistente.setValorTotal(novoValorTotal);
            pedidoRepository.save(pedidoExistente);

            return ResponseEntity.ok("Pedido atualizado com sucesso!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno ao atualizar o pedido.");
        }
    }

    // **Endpoint para DELETAR pedido**
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarPedido(@PathVariable Long id) {
        try {
            pedidoRepository.deleteById(id);
            return ResponseEntity.ok("Pedido deletado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno ao deletar o pedido.");
        }
    }
}