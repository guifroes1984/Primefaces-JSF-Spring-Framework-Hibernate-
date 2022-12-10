package br.com.project.util.all;

import org.springframework.stereotype.Component;

@Component/*Inje��o de dependencias do spring*/
public abstract class BeanViewAbstract implements ActionViewPadrao {/*Manter essa classe como abstract, pq dai obrigamos que as outras classes implementem*/
	
	/**
	 * Aqui funciona da seguinte forma. Se escrevermos alguma regra de neg�cio dentro desses metodos, qunado for chamado o nosso sistema
	 * e esses metodos n�o tiverem reemplementados, vai ser chamado aqui o da abstra��o. Caso contr�rio se sobrescrever o metodo ele vai
	 * vai chamar onde agente sobrescreveu o metodo.
	 */
	
	@Override
	public void limparLista() throws Exception {

	}

	@Override
	public String save() throws Exception {
		return null;

	}

	@Override
	public void saveNotReturn() throws Exception {

	}

	@Override
	public void saveEdit() throws Exception {

	}

	@Override
	public void excluir() throws Exception {

	}

	@Override
	public String ativar() throws Exception {
		return null;
	}

	@Override
	public String novo() throws Exception {
		return null;
	}

	@Override
	public String editar() throws Exception {
		return null;
	}

	@Override
	public void setarVariaveisNulas() throws Exception {

	}

	@Override
	public void consultarEntidade() throws Exception {

	}

	@Override
	public void statusOperation(EstatusPersistencia a) throws Exception {
		Menssagens.responseOperation(a);/*"Deixando definido". Pega a classe de Menssagens, e da um responseOperation, passando o nosso EstatusPersistencia, que a� ele vai dar a resposta. Se passarmos SUCESSO, ele vai dar FATAL*/
		
	}
	
	/*Deixando definido um sucesso. Deixando protegido, s� quem sobrescrever vai ter acesso*/
	protected void sucesso() throws Exception {
		statusOperation(EstatusPersistencia.SUCESSO);/*Iniciando metodo o nosso statusOperation, passando EstatusPersistencia.SUCESSO. E Vamos s� chamando os metodos*/
	}
	
	/*Deixando definido um error. Deixando protegido, s� quem sobrescrever vai ter acesso*/
	protected void error() throws Exception {
		statusOperation(EstatusPersistencia.ERRO);/*Iniciando metodo o nosso statusOperation, passando EstatusPersistencia.ERRO. E Vamos s� chamando os metodos*/
	}

	@Override
	public String redirecionarNewEntidade() throws Exception {
		return null;
	}

	@Override
	public String redirecionarFindEntidade() throws Exception {
		return null;
	}

	@Override
	public void addMsg(String msg) {
		Menssagens.msg(msg);/*"Deixando definido". Quando chamar ele, s� passa a mensagem e vai ser impressa na tela para n�s*/

	}

}
