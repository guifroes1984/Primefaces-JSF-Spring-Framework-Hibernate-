package br.com.framework.implementacao.crud;

import java.io.Serializable;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component/*Gerenciada pelo Spring, pela inje��o de dependencia*/
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)/*Quando fizermos uma transa��o pelo Spring, a anota��o "propagation = Propagation.REQUIRED" se n�o existir ele vai criar uma transa��o para n�s para poder executar as transa��es do banco. O "rollbackFor = Exception.class", vai dar o rolback na opera��o do banco se acontecer qualquer erro provido do banco*/
public class JdbcTemplateImpl extends JdbcTemplate implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public JdbcTemplateImpl(DataSource dataSource) {/*Criar um construtor que receber a conex�o com banco de dados*/
		super(dataSource);
	}

}
