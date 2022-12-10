package br.com.framework.interfac.crud;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component/*Injeção de dependencias do spring*/
@Transactional/*Vai fazer transações com o banco de dados. Também do spring*/
public interface InterfaceCrud<T> extends Serializable {/*<T> vai receber uma classe generica. Está dizendo que essa classe vai trabalhar com qualquer objeto*/
	
	/*Toda Interface os metodos são publicos, por defalt ele já é public*/
	
	/*Algumas rotinas definir padrões como: Salvar, persistir, atualizar, deletar, dar um merge...etc */
	
	/*Salva os dados*/
	void save(T obj) throws Exception;
	
	/*Faz a mesma coisa, salvando o objeto*/
	void persist(T obj) throws Exception;
	
	/*Salva ou atualiza o objeto*/
	void saveOrUpdate(T obj) throws Exception;
	
	/*Realiza o update/ atualização de dados*/
	void update(T obj) throws Exception;
	
	/*Realiza o delete de dados*/
	void delete(T obj) throws Exception;
	
	/*Salva ou atualiza e retorna objeto em estado persistente*/
	T merge(T obj) throws Exception;
	
	/*Carrega a lista de dados de determinada classe*/
	List<T> findList(Class<T> obj) throws Exception;
	
	/*Lista*/
	Object findById(Class<T> entidade, Long id) throws Exception;
	
	T findByPorId(Class<T> entidade, Long id) throws Exception;
	
	List<T> findListByQueryDinamica(String s) throws Exception;
	
	/*Executar update com HQL*/
	void executeUpdateQueryDinamica(String s) throws Exception;
	
	/*Executar update com SQL*/
	void executeUpdateSQLDinamica(String s) throws Exception;
	
	/*Fazer o hibernate limpar a sessão*/
	void clearSession() throws Exception;
	
	/*Retira um objeto da sessão do hibernate*/
	void evict(Object objs) throws Exception;
	
	/*Metodo que retorna a sessão dentro da interface*/
	Session getSession() throws Exception;
	
	/*Podemos passar um SQL que hibernate retorna a lista*/
	List<?> getListSQLDinamica(String sql) throws Exception;
	
	/*JDBC do Spring*/
	JdbcTemplate getJdbcTemplate();

	SimpleJdbcTemplate getSimpleJdbcTemplate();
	
	SimpleJdbcInsert getSimpleJdbcInsert();
	
	/*Metodo para saber a quantidade de registros em determinada tabela*/
	Long totalRegistro(String table) throws Exception;
	
	/*Metodos de motagem dinamica de SQL ou de Query*/
	Query obterQuery(String query) throws Exception;
	
	/*Metodo de carregamento por demanda. Carregamento com JSF e PrimeFaces*/
	List<T> findListByQueryDinamica(String query, int iniciaNoRegistro, int maximoResultado) throws Exception;

}
