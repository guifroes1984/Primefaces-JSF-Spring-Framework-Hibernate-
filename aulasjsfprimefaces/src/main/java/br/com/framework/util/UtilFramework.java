package br.com.framework.util;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component
public class UtilFramework implements Serializable {/*Classe responsavel por cadastrar o usuario, que está inserindo, excluindo, e editando, para saber o que está acontecendo com os registros no banco de dados*/

	private static final long serialVersionUID = 1L;
	
	private static ThreadLocal<Long> threadLocal = new ThreadLocal<Long>();/*ThreadLocal=> É para não haver concorrencia*/
	
	public synchronized static ThreadLocal<Long> getThreadLocal() {/*Quando se tem um metodo estatico e sicronizado, dois lugares no sistema não conseguem acessar ao mesmo tempo*/
		return threadLocal;
	}

}
