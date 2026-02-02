package com.pdv.lalapan.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Categoria {

    PADARIA("Padaria e Sobremesas"),
    LATICINIOS("Laticínios"),
    HORTIFRUTI("Hortifruti"),
    BEBIDAS("Bebidas"),
    LIMPEZA("Bazar e Limpeza"),
    MERCEARIA("Mercearia"),
    HIGIENE("Higiene"),
    CARNES("Carnes"),
    CONGELADOS("Comidas Prontas e Congeladas"),
    FRIOS("Frios e Derivados"),
    FRUTAS_E_VERDURAS("Frutas, Ovos e Verduras"),
    IMPORTADOS("Importados"),
    SAUDE_E_BELEZA("Saúde e Beleza");

    private final String descricao;

    Categoria(String descricao) {
        this.descricao = descricao;
    }

    @JsonValue
    public String getDescricao() {
        return descricao;
    }

    @JsonCreator
    public static Categoria fromDescricao(String valor) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.name().equals(valor)) {
                return categoria;
            }
        }

        for (Categoria categoria : Categoria.values()) {
            if (categoria.descricao.equals(valor)) {
                return categoria;
            }
        }

        throw new IllegalArgumentException("Categoria inválida: " + valor);
    }
}

