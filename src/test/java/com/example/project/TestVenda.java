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

    private String LOGRADOURO = "Rua 1";
    private int NUMERO = 11;
    private String COMPLEMENTO = "Complemento 1";
    private String BAIRRO = "Bairro 1";
    private String MUNICIPIO = "Municipio 1";
    private String ESTADO = "Estado 1";
    private String CEP = "11111-111";

    private String NOME_LOJA = "Loja 1";
    private String TELEFONE = "(11)1111-1111";
    private String OBSERVACAO = "Observacao 1";
    private String CPNJ = "123456789";
    private String INSCRICAO_ESTADUAL = "987654321";
 
    private String MSG_ERR_LOJA_INVALIDA = "Loja é um campo obrigatório. Insira uma loja válida.";
    private String MSG_ERR_CCF_INVALIDO = "O CCF inserido não é válido.";
    private String MSG_ERR_COO_INVALIDO = "O COO inserido não é válido.";
    private String MSG_ERR_CCF = "O Contador de Cupom Fiscal (CCF) é obrigatório.";
    private String MSG_ERR_COO = "O Contador de Ordem de Operação (COO) é obrigatório.";

    private Endereco enderecoSample = new Endereco(
        LOGRADOURO, 
        11, 
        COMPLEMENTO, 
        BAIRRO, 
        MUNICIPIO, 
        ESTADO, 
        CEP
    );

    private Loja lojaSample = new Loja(
        NOME_LOJA,
        enderecoSample,
        TELEFONE,
        OBSERVACAO,
        CPNJ,
        INSCRICAO_ESTADUAL
    );

    private String COO = "123456";
    private String CCF = "123456";

    private Venda vendaSample = new Venda(
        lojaSample,
        CCF,
        COO
    );

    String CODIGO = "001";
    String DESCRICAO = "Banana";
    String UNIDADE = "R$";
    double VALOR_UNITARIO = 11.11;
    String SUBSTITUICAO_TRIBUTARIA = "ST";

    private Produto produtoSample = new Produto(
        CODIGO, 
        DESCRICAO, 
        UNIDADE, 
        VALOR_UNITARIO, 
        SUBSTITUICAO_TRIBUTARIA
    );

    private int QUANTIDADE = 4;

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
        ccfVazio.setCcf("");
        verificarCampoObrigatorio(MSG_ERR_CCF, ccfVazio);

        Venda ccfIncorreto = vendaSample;
        ccfIncorreto.setCcf("12345");
        verificarCampoObrigatorio(MSG_ERR_CCF_INVALIDO, ccfIncorreto);
    }

    @Test
	public void validarCOO() {
        Venda cooVazio = vendaSample;
        cooVazio.setCoo("");
        verificarCampoObrigatorio(MSG_ERR_COO, cooVazio);

        Venda cooIncorreto = vendaSample;
        cooIncorreto.setCoo("12345");
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

    private String nextLine = System.lineSeparator();
    private String DATAHORA = "11/11/1111 11:11:11V";
    private String HIFENS = "------------------------------";
    
    String UND = UNIDADE;
    String VU = String.format("%.2f", VALOR_UNITARIO);
    String ST = SUBSTITUICAO_TRIBUTARIA;
    String QTD = String.valueOf(QUANTIDADE);
    String TOTAL_ITEM_VENDA = String.valueOf(QUANTIDADE * VALOR_UNITARIO);

    String TEXTO_ESPERADO_IMPRIMIR_CUPOM = NOME_LOJA + nextLine +
    LOGRADOURO + ", " + NUMERO + " " + COMPLEMENTO + nextLine +
    BAIRRO + " - " + MUNICIPIO + " - " + ESTADO + nextLine +
    "CEP:" + CEP + " Tel " + TELEFONE + nextLine +
    OBSERVACAO + nextLine +
    "CNPJ: " + CPNJ + nextLine +
    "IE: " + INSCRICAO_ESTADUAL + nextLine +
    HIFENS + nextLine +
    DATAHORA + " CCF: " + CCF + " COO: " + COO + nextLine +
    "     CUPOM FISCAL     " + nextLine +
    "ITEM CODIGO DESCRICAO QTD UN VL UNIT(R$) ST VL ITEM(R$)" + nextLine +
    "1 " + CODIGO + " " + DESCRICAO + " " + QTD + " R$ " + VU + " " + ST + TOTAL_ITEM_VENDA + nextLine +
    HIFENS + nextLine +
    "TOTAL: R$ " + TOTAL_ITEM_VENDA;

    @Test
    public void impressaoCupom() {
        Venda venda = vendaSample;
        venda.adicionarItem(produtoSample, QUANTIDADE);
        venda.setDataHora(DATAHORA);

        assertEquals(TEXTO_ESPERADO_IMPRIMIR_CUPOM, venda.imprimirCupom());
    }
}
