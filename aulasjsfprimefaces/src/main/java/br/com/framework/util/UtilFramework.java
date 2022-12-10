package br.com.framework.util;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component
public class UtilFramework implements Serializable {/*Classe responsavel por cadastrar o usuario, que est� inserindo, excluindo, e editando, para saber o que est� acontecendo com os registros no banco de dados*/

	private static final long serialVersionUID = 1L;
	
	private static ThreadLocal<Long> threadLocal = new ThreadLocal<Long>();/*ThreadLocal=> � para n�o haver concorrencia*/
	
	public synchronized static ThreadLocal<Long> getThreadLocal() {/*Quando se tem um metodo estatico e sicronizado, dois lugares no sistema n�o conseguem acessar ao mesmo tempo*/
		return threadLocal;
	}

}
