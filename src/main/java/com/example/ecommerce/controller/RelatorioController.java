package com.example.ecommerce.controller;



import com.example.ecommerce.model.Pedido;
import com.example.ecommerce.repository.PedidoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/relatorios") // Base da URL para relatórios
public class RelatorioController {

    @Autowired
    private PedidoRepository pedidoRepository;

    // **Endpoint para Gerar Relatório de Vendas**
    @GetMapping("/vendas")
    public Map<String, Object> gerarRelatorioDeVendas() {
        // Buscar todos os pedidos
        List<Pedido> pedidos = pedidoRepository.findAll();

        // Total de vendas (soma do valor total dos pedidos)
        Double totalVendas = pedidos.stream().mapToDouble(Pedido::getValorTotal).sum();

        // Quantidade total de pedidos
        int quantidadePedidos = pedidos.size();

        // Criar um relatório com os dados coletados
        Map<String, Object> relatorio = new HashMap<>();
        relatorio.put("totalVendas", totalVendas);
        relatorio.put("quantidadePedidos", quantidadePedidos);
        relatorio.put("pedidosDetalhados", pedidos); // Opcional: Retorna os pedidos completos

        // Retornar o relatório em formato JSON
        return relatorio;
    }
}