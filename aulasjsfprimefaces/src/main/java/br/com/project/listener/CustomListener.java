package br.com.project.listener;

import java.io.Serializable;

import org.hibernate.envers.RevisionListener;
import org.springframework.stereotype.Service;

import br.com.framework.util.UtilFramework;
import br.com.project.model.classes.Entidade;
import br.com.project.model.classes.InformacaoRevisao;

/**
 * É o CustomListener que vai ficar responsavel por disparar o evento de gravar o log para nós
 */
@Service/*É um um serviço*/
public class CustomListener implements RevisionListener, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	@Override
	public void newRevision(Object revisionEntity) {
		InformacaoRevisao informacaoRevisao = (InformacaoRevisao) revisionEntity;/*Converter para *informacaoRevisao, fazendo um cash, ele é objeto do tipo informacaoRevisao*/
		Long coduser = UtilFramework.getThreadLocal().get();/*Pegamos o codigo do usuaário, pegando da classe UtilFramework. Pegamos para carregar o código do usuario para nós*/
		
		Entidade entidade = new Entidade();/*Criando um novo objeto de entidade*/
		
		/*Verificação*/
		if (coduser != null && coduser !=0L) {/*Se o coduser realmente existir e coduser for diferente de zero(converter para Long)*/
			entidade.setEnt_codigo(coduser);/*Seta o coduset*/
			informacaoRevisao.setEntidade(entidade);/*Passa para o objeto de log, setar a Entidade de entidade*/
		}
		
	}

}
