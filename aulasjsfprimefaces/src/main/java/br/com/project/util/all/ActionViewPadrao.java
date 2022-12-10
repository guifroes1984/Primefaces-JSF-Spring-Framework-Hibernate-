package br.com.project.util.all;

import java.io.Serializable;

import javax.annotation.PostConstruct;

public interface ActionViewPadrao extends Serializable {/*Interface se extende, não tem como implementar um interface de interface. Uma interface extende de outra no caso da Serializable*/
	
	/**
	 * Metodos abstratos
	 */
	
	abstract void limparLista() throws Exception;
	
	abstract String save() throws Exception;
	
	abstract void saveNotReturn() throws Exception;
	
	abstract void saveEdit() throws Exception;
	
	abstract void excluir() throws Exception;
	
	abstract String ativar() throws Exception;
	
	/**
	 * @PostConstruct -> Realiza inicialização de metodos, valores ou variaveis. 
	 * 
	 * Quando abrirmos uma tela o ManageBean ele foi instanciado, o JSF vai entender que agente quer trabalhar com aquela tela pela
	 * primeira vez. E o metodo que tiver anotado com "@PostConstruct" ele vai executar, vai inicializar, podendo carregar uma lista
	 * como padrão e fazer varias outras coisas.
	 */
	@PostConstruct
	abstract String novo() throws Exception;
	
	abstract String editar() throws Exception;
	
	abstract void setarVariaveisNulas() throws Exception;
	
	abstract void consultarEntidade() throws Exception;/*Vai auxiliar a consultar os registros cadastrados*/
	
	abstract void statusOperation(EstatusPersistencia a) throws Exception;
	
	abstract String redirecionarNewEntidade() throws Exception;/*Pode criar um novo registro e redirecionar para um novo lugar*/
	
	abstract String redirecionarFindEntidade() throws Exception;/*Fazer uma consultar e redirecionarpara algum local*/
	
	abstract void addMsg(String msg);/*Pode adicionar uma mensagem ao MenageBean*/

}
