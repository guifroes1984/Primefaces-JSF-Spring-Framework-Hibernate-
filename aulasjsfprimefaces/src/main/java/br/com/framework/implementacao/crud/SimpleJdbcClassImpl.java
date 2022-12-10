package br.com.framework.implementacao.crud;

import java.io.Serializable;

import javax.sql.DataSource;

import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component/*Gerenciada pelo Spring, pela injeção de dependencia*/
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)/*Quando fizermos uma transação pelo Spring, a anotação "propagation = Propagation.REQUIRED" se não existir ele vai criar uma transação para nós para poder executar as transações do banco. O "rollbackFor = Exception.class", vai dar o rolback na operação do banco se acontecer qualquer erro provido do banco*/
public class SimpleJdbcClassImpl extends SimpleJdbcCall implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public SimpleJdbcClassImpl(DataSource dataSource) {/*Criar um construtor que receber a conexão com banco de dados*/
		super(dataSource);
	}

}
