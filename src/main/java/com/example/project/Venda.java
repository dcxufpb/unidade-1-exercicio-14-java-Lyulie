package com.example.project;

import java.text.DateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.Locale;

public class Venda {
    private Loja loja;
    private String datahora;
    private String ccf;
    private String coo;
    private ItensVendaService itens;
    public static final String LN = System.lineSeparator();

    public Venda(Loja loja, String ccf, String coo) {
        this.loja = loja;
        this.ccf = ccf;
        this.coo = coo;
        this.datahora = getDataAtual();
        this.itens = new ItensVendaService();
    }

    public void adicionarItem(Produto produto, int quantidade) {
        this.itens.adicionarItem(produto, quantidade);
    }

    public Loja getLoja() {
        return this.loja;
    }

    public void setLoja(Loja loja) {
        this.loja = loja;
    }

    public String getDataHora() {
        return this.datahora;
    }

    public void setDataHora() {
        this.datahora = getDataAtual();
    }

    public void setDataHora(String datahora) {
        this.datahora = datahora;
    }

    public static String getDataAtual() {
        LocalTime hora = java.time.LocalTime.now();
        String horaFormat = hora.toString()
                                .substring(0,8);
                                
        Date d = new Date();
        Locale brasil = new Locale("pt", "BR");

        DateFormat data = DateFormat.getDateInstance(
            DateFormat.SHORT,
            brasil
        );

        String dataFormat = data.format(d);
        return dataFormat + " " + horaFormat + "V";
    }

    public String getCcf() {
        return this.ccf;
    }

    public void setCcf(String ccf) {
        this.ccf = ccf;
    }

    public String getCoo() {
        return this.coo;
    }

    public void setCoo(String coo) {
        this.coo = coo;
    }

    public ItensVendaService getItens() {
        return itens;
    }

    public String  dadosItens() {
        return "ITEM CODIGO DESCRICAO QTD UN VL UNIT(R$) ST VL ITEM(R$)" + LN +
               this.itens.toString();
    }

    public String dadosVenda() {

        validarCamposObrigatorios();

        String _ccf = " CCF: " + getCcf();
        String _coo = " COO: " + getCoo();
        return getDataHora() + _ccf + _coo;
    }

    public String imprimirCupom() {
        if(this.itens.numeroDeItens() == 0) {
            throw new RuntimeException("Não há itens para imprimir.");
        }

        String dadosLoja = this.loja.dadosLoja();
        String dadosVenda = dadosVenda();
        String dadosItens = dadosItens();

        String output;
        output = "";
        for(int k = 0; k<30; k++) output += "-";
        output += LN + dadosVenda + LN;
        output += "     CUPOM FISCAL     ";
        output += LN + dadosItens + LN;
        for(int k = 0; k<30; k++) output += "-";
        output += LN + "TOTAL: R$ " + this.itens.calcularTotal();

        return dadosLoja + output;
    }

    public void validarCamposObrigatorios() {
        if(getCcf().length() == 0) {
            throw new RuntimeException("O Contador de Cupom Fiscal (CCF) é obrigatório.");
        }

        else if(getCcf().length() < 6) {
            throw new RuntimeException("O CCF inserido não é válido.");
        }

        if(getCoo().length() == 0) {
            throw new RuntimeException("O Contador de Ordem de Operação (COO) é obrigatório.");
        }
        
        else if(getCoo().length() < 6) {
            throw new RuntimeException("O COO inserido não é válido.");
        }

        try {
            String strLoja = getLoja().dadosLoja();
        } catch (RuntimeException re) {
            throw new RuntimeException("Loja é um campo obrigatório. Insira uma loja válida.");
        }
    }
}
