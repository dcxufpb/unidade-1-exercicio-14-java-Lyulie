package com.example.project;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TestVenda {
    
    private void verificarCampoObrigatorio(
        String mensagemEsperada,
        Venda venda
    ) {
        try {
            venda.dadosVenda();
            venda.imprimirCupom();
            assertEquals("RuntimeException", "pass");
        } catch (RuntimeException re) {
            assertEquals(mensagemEsperada, re.getMessage());
        }
    }

    private void validaItemAdicionado(
        String mensagemEsperada,
        Venda venda,
        Produto produto,
        int quantidade
    ) {
        try {
            venda.getItens().adicionarItem(produto, quantidade);
            assertEquals("RuntimeException", "pass");
        } catch (RuntimeException re) {
            assertEquals(mensagemEsperada, re.getMessage());
        }
    }

    String MSG_ERR_LOJA_INVALIDA = "Loja é um campo obrigatório. Insira uma loja válida.";
    String MSG_ERR_CCF_INVALIDO = "O CCF inserido não é válido.";
    String MSG_ERR_COO_INVALIDO = "O COO inserido não é válido.";
    String MSG_ERR_CCF = "O Contador de Cupom Fiscal (CCF) é obrigatório.";
    String MSG_ERR_COO = "O Contador de Ordem de Operação (COO) é obrigatório.";

    private Endereco enderecoSample = new Endereco(
        "Rua 1", 
        11, 
        "Complemento 1", 
        "Bairro 1", 
        "Município 1", 
        "Estado 1", 
        "Cep 1"
    );

    private Loja lojaSample = new Loja(
        "Loja 1",
        enderecoSample,
        "(11)1111-1111",
        "Observacao 1",
        "987654321",
        "123456789"
    );

    private int COO = 123456;
    private int CCF = 123456;

    private Venda vendaSample = new Venda(
        lojaSample,
        CCF,
        COO
    );

    private Produto produtoSample = new Produto(
        001, 
        "Maçã", 
        "R$", 
        11.11, 
        "ST"
    );

    /**
     * Testar Campos Obrigatórios
     * @Vendas loja, ccf, coo
     */

    @Test
    public void validarLoja() {
        Venda lojaNula = vendaSample;
        lojaNula.setLoja(null);
        verificarCampoObrigatorio(MSG_ERR_LOJA_INVALIDA, lojaNula);
    }

    @Test
	public void validarCCF() {
        Venda ccfVazio = vendaSample;
        ccfVazio.setCcf(0);
        verificarCampoObrigatorio(MSG_ERR_CCF, ccfVazio);

        Venda ccfIncorreto = vendaSample;
        ccfIncorreto.setCcf(12345);
        verificarCampoObrigatorio(MSG_ERR_CCF_INVALIDO, ccfIncorreto);
    }

    @Test
	public void validarCOO() {
        Venda cooVazio = vendaSample;
        cooVazio.setCoo(0);
        verificarCampoObrigatorio(MSG_ERR_COO, cooVazio);

        Venda cooIncorreto = vendaSample;
        cooIncorreto.setCoo(12345);
        verificarCampoObrigatorio(MSG_ERR_COO_INVALIDO, cooIncorreto);
    }

    /**
     * Testar dados gerados?
     * @Vendas datahora
     */

    @Test
    public void validarDataHora() {
        Venda venda = vendaSample;
        String dataAtual = Venda.getDataAtual();
        assertEquals(
            dataAtual.substring(0, 15), 
            venda.getDataHora()
                 .substring(0, 15)
        );
    }

    //

    String MSG_ERR_SEM_ITENS = "Não há itens para imprimir.";
    String MSG_ERR_ITEM_DUPLICADO = "O produto já está na lista.";
    String MSG_ERR_QUANTIDADE = "Item de Venda com quantidade zero ou negativa.";
    String MSG_ERR_VALOR_VENDA = "Produto com valor unitário zero ou negativo.";

    @Test
    public void vendaSemItens() {
        Venda semItens = vendaSample;

        try {
            semItens.imprimirCupom();
            assertEquals("RuntimeException", "pass");
        } catch (RuntimeException re) {
            assertEquals(MSG_ERR_SEM_ITENS, re.getMessage());
        }
    }
    
    @Test
    public void vendaItemDuplicado() {
        Venda itemDuplicado = vendaSample;

        itemDuplicado.adicionarItem(produtoSample, 2);
 
        validaItemAdicionado(
            MSG_ERR_ITEM_DUPLICADO, 
            itemDuplicado,
            produtoSample, 
            3
        );
    }

    @Test
    public void quantidadeMenorQueUm() {
        Venda quantidadeMenorQueUm = vendaSample;

        validaItemAdicionado(
            MSG_ERR_QUANTIDADE, 
            quantidadeMenorQueUm,
            produtoSample, 
            0
        );
    }

    @Test
    public void valorUnitarioZeroInferior() {
        Venda venda = vendaSample;
        Produto valorUnitarioZeroInferior = produtoSample;
        valorUnitarioZeroInferior.setValorUnitario(-3);
        
        validaItemAdicionado(
            MSG_ERR_VALOR_VENDA, 
            venda,
            valorUnitarioZeroInferior, 
            2
        );
    }
}