package br.com.project.acessos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public enum Permissao {/*enum nada mais do que um Array, ou uma lista de dados pré-definidos*/
	
	ADMIN("ADMIN", "Administrador"),
	USER("USER", "Usuário Padrão" ),
	CADASTRO_ACESSAR("CADASTRO_ACESSAR", "Cadastro - Acessar"),
	FINANCEIRO_ACESSAR("FINANCEIRO_ACESSAR", "Financeiro - Acessar"),
	MENSAGEM_ACESSAR("MENSAGEM_ACESSAR", "Mensagem recebida - Acessar"),
	
	BAIRRO_ACESSAR("BAIRRO_ACESSAR", "Bairro - Acessar"),
	BAIRRO_NOVO("BAIRRO_NOVO", "Bairro - Novo"),
	BAIRRO_EDITAR("BAIRRO_EDITAR", "Bairro - Editar"),
	BAIRRO_EXCLUIR("BAIRRO_EXCLUIR", "Bairro - Excluir");
	
	private String valor = "";/*Valor do enum*/
	private String descricao = "";/*Descrição do enum*/
	
	private Permissao() {/*Construtor implicito*/
		
	}

	private Permissao(String name, String descricao) {/*Construtor cheio*/
		this.valor = name;
		this.descricao = descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setValor(String valor) {
		this.valor = valor;
	}
	
	public String getValor() {
		return valor;
	}
	
	@Override
	public String toString() {
		return getValor();
	}
	
	public static List<Permissao> getListPermissao() {/*Lista para carregar todos os enuns*/
		List<Permissao> permissoes = new ArrayList<Permissao>();/*Lista de permissao*/
		for (Permissao permissao : Permissao.values()) {/*Vai varrer todos os valores, e retorna um objeto permissao, retornando um Array*/
			permissoes.add(permissao);/*Vai adicionar as permissoes, a lista de permissao*/
		}
		
		Collections.sort(permissoes, new Comparator<Permissao>() {/*Ordenando o objeto fazendo comparações*/

			@Override
			public int compare(Permissao o1, Permissao o2) {/*Adiciona um metodo interno de comparações, Onde define do jeito que ele vai ordenar para nós*/
				
				//return new Integer(o1.ordinal()).compareTo(new Integer(o2.ordinal()));/*Está depreciado*/
				return Integer.valueOf(o1.ordinal()).compareTo(Integer.valueOf(o2.ordinal()));/*Vai comparar o codigo de ordenação do primeiro objeto, com o segundo objeto. E vai ordenar para nós, jogando na tela certinho.*/
			}
			
		});
		
		return permissoes;/*Retorno da lista de permissoes*/
	}
	
}
