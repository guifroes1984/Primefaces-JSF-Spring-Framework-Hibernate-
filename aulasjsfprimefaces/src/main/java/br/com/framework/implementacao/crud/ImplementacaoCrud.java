package br.com.framework.implementacao.crud;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.framework.hibernate.session.HibernateUtil;
import br.com.framework.interfac.crud.InterfaceCrud;
import br.com.project.model.classes.Entidade;

@Component
@Transactional
public class ImplementacaoCrud<T> implements InterfaceCrud<T> {

	private static final long serialVersionUID = 1L;
	
	private static SessionFactory sessionFactory = HibernateUtil.getSessionFactory();/*Tem que ser privado para essa classe não pode ser acessado de fora. E static pq irá carregar apenas uma instância*/
	
	@Autowired/*Injeção de dependencia*/
	private JdbcTemplateImpl jdbcTemplate;
	
	@Autowired/*Injeção de dependencia*/
	private SimpleJdbcTemplateImpl simpleJdbcTemplate;
	
	@Autowired/*Injeção de dependencia*/
	private SimpleJdbcInsertImplents simpleJdbcInsert;
	
	@Autowired/*Injeção de dependencia*/
	private SimpleJdbcTemplateImpl simpleJdbcTemplateImpl;
	
	public SimpleJdbcTemplateImpl getSimpleJdbcTemplateImpl() {
		return simpleJdbcTemplateImpl;
	}
	
	@Override
	public void save(T obj) throws Exception {
		validaSessionFactory();/*Sem a sessonFactor não for válida, vai dar problema. Recebendo o objeto*/
		sessionFactory.getCurrentSession().save(obj);/*Operação no banco de dados, passando o objeto para salvar*/
		executeFlushSession();/*Consegue manter a consistencia do banco de dados. Se inseriu corretamente, ou atualizou corretamente, deletou corretamente*/
	}

	@Override
	public void persist(T obj) throws Exception {
		validaSessionFactory();/*Sem a sessonFactor não for válida, vai dar problema. Recebendo o objeto*/
		sessionFactory.getCurrentSession().persist(obj);/*Operação que vai persistir esse objeto*/
		executeFlushSession();/*Consegue manter a consistencia do banco de dados. Se inseriu corretamente, ou atualizou corretamente, deletou corretamente. Executar no banco de dados*/
	}

	@Override
	public void saveOrUpdate(T obj) throws Exception {
		validaSessionFactory();/*Sem a sessonFactor não for válida, vai dar problema. Recebendo o objeto*/
		sessionFactory.getCurrentSession().saveOrUpdate(obj);/*Salva e atualizando, sempre passando o objeto*/
		executeFlushSession();/*Consegue manter a consistencia do banco de dados. Se inseriu corretamente, ou atualizou corretamente, deletou corretamente. Executar no banco de dados*/
	}

	@Override
	public void update(T obj) throws Exception {
		validaSessionFactory();/*Sem a sessonFactor não for válida, vai dar problema. Recebendo o objeto*/
		sessionFactory.getCurrentSession().update(obj);/*Faz atualização do objeto no banco de dados.*/
		executeFlushSession();/*Consegue manter a consistencia do banco de dados. Se inseriu corretamente, ou atualizou corretamente, deletou corretamente. Executar no banco de dados*/
	}

	@Override
	public void delete(T obj) throws Exception {
		validaSessionFactory();/*Sem a sessonFactor não for válida, vai dar problema. Recebendo o objeto*/
		sessionFactory.getCurrentSession().delete(obj);/*Faz o delete do objeto no banco de dados. Executar no banco de dados*/
		executeFlushSession();/*Consegue manter a consistencia do banco de dados. Se inseriu corretamente, ou atualizou corretamente, deletou corretamente*/
	}

	@Override
	public T merge(T obj) throws Exception {
		validaSessionFactory();/*Sem a sessonFactor não for válida, vai dar problema. Recebendo o objeto, esse objeto*/
		obj = (T) sessionFactory.getCurrentSession().merge(obj);/*Recebendo um objeto pode ser um novo ou não, independentemente se for um objeto novo, ou objeto que esteja sendo atualizado, ou inserindo um novo no BD, se existe ou não o merge faz isso, ele vai salvar, atualizar e retorna os dados persistentes igual está na barra de dados*/
		executeFlushSession();/*Executar no banco de dados*/
		return obj;/*Faz o retorno para o sistema*/
	}

	@Override
	public List<T> findList(Class<T> entidade) throws Exception {
		validaSessionFactory();/*Sem a sessonFactor não for válida, vai dar problema. Recebendo o objeto, esse objeto. No caso aqui um lista*/
		
		StringBuilder query = new StringBuilder();/*Aqui vai ser generico*/
		query.append(" select distinct(entity) from ").append(entidade.getSimpleName()).append(" entity ");/*A query vai servir para todas as classe. "distinct"=> Vai destinguir, não vai trazer objetos repitidos. getSimpleName=> Vai retornar a classe pessoa, o nome da pessoa*/
		
		List<T> lista = sessionFactory.getCurrentSession().createQuery(query.toString()).list();/*Para poder retonar, como é generico, tem que implementar uma lista generica. Passando uma Query*/
		
		return lista;/*Retorna a lista*/
	}

	@Override
	public Object findById(Class<T> entidade, Long id) throws Exception {
		
		validaSessionFactory();/*Sem a sessonFactor não for válida, vai dar problema. Recebendo o objeto, esse objeto. No caso aqui um lista*/
		Object obj = sessionFactory.getCurrentSession().load(getClass(), id);/*Faz o carregamento do objeto por carregamento, passando a classe e o id*/
		return obj;/*Retornado o objeto*/
	}

	@Override
	public T findByPorId(Class<T> entidade, Long id) throws Exception {
		validaSessionFactory();/*Sem a sessonFactor não for válida, vai dar problema. Recebendo o objeto, esse objeto. No caso aqui um lista*/
		T obj = (T)sessionFactory.getCurrentSession().load(getClass(), id);/*Faz o carregamento do objeto por carregamento, passando a classe e o id*/
		
		return obj;/*Retornado o objeto*/
	}

	@Override
	public List<T> findListByQueryDinamica(String s) throws Exception {
		validaSessionFactory();/*Sem a sessonFactor não for válida, vai dar problema. Recebendo o objeto, esse objeto. No caso aqui um lista*/
		
		List<T> lista = new ArrayList<T>();/*Criar a lista evita alguns erros no sistema*/
		lista = sessionFactory.getCurrentSession().createQuery(s).list();/*Podendo passar qualquer Query por ele, vai retornar a lista, vai executar no banco de dados*/
		
		return lista;/*Retorna a lista*/
	}

	@Override
	public void executeUpdateQueryDinamica(String s) throws Exception {/*Podemos passar qualquer instrução HQL no hibernate JPA, que vai ser executado*/
		validaSessionFactory();/*Sem a sessonFactor não for válida, vai dar problema. Recebendo o objeto*/
		sessionFactory.getCurrentSession().createQuery(s).executeUpdate();/*Para poder retonar, como é generico, tem que implementar uma lista generica.*/
		executeFlushSession();/*Executar no banco de dados*/
	}

	@Override
	public void executeUpdateSQLDinamica(String s) throws Exception {/*Podemos passar qualquer instrução SQL puro. É como se fosse um SQL orientado a objeto*/
		validaSessionFactory();/*Sem a sessonFactor não for válida, vai dar problema. Recebendo o objeto*/
		sessionFactory.getCurrentSession().createSQLQuery(s).executeUpdate();/*Aqui é passando a instrução SQL, e vai executar no Banco de dados*/
		executeFlushSession();/*Executar no banco de dados*/

	}

	@Override
	public void clearSession() throws Exception {/*Para fazer o hibernate limpar a sessão, as vezes pode ficar objetos na sessão,e pode atrapalhar as transações*/
		sessionFactory.getCurrentSession().clear();/*Pronto limpou os objetos da sessão*/
		
	}

	@Override
	public void evict(Object objs) throws Exception {/*Sabe qual objeto está dando problema na memória pode usar o metódo "evict"*/
		validaSessionFactory();/*Sem a sessonFactor não for válida, vai dar problema. Recebendo o objeto*/
		sessionFactory.getCurrentSession().evict(objs);/*Vai retirar o objeto da sessão, e se tiver algum problema de transação retira o objeto e faz a operação no banco de dados novamente*/
	}

	@Override
	public Session getSession() throws Exception {/*Usar dentro dentro da implementação, ou para alguma classe extender ele, podemos retornar a sessão do hibernate para retornar algum outro metodo que não esteja implementado*/
		validaSessionFactory();/*Sem a sessonFactor não for válida, vai dar problema. Recebendo o objeto*/
		return sessionFactory.getCurrentSession();/*Retorna para a sessão*/
	}

	@Override
	public List<?> getListSQLDinamica(String sql) throws Exception {/*Retornar objetos atraves de um SQL*/
		validaSessionFactory();/*Sem a sessonFactor não for válida, vai dar problema. Recebendo o objeto*/
		List<?> lista = sessionFactory.getCurrentSession().createSQLQuery(sql).list();/*Retorna uma lista generica.*/
		return lista;
	}

	@Override
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Override
	public SimpleJdbcTemplate getSimpleJdbcTemplate() {
		return simpleJdbcTemplate;
	}

	@Override
	public SimpleJdbcInsert getSimpleJdbcInsert() {
		return simpleJdbcInsert;
	}

	@Override
	public Long totalRegistro(String table) throws Exception {
		StringBuilder sql = new StringBuilder();/*Usando o JDBC para um recurso tão simples*/
		sql.append(" select count(1) from ").append(table);/*Passando o SQL, total de registros de uma tabela. count(1)=> contra pela primeira coluna*/
		return jdbcTemplate.queryForLong(sql.toString());/*É usado para SQL igual estamos usando, select que vai retornar um numero, pori isso queryForLong*/
	}

	@Override
	public Query obterQuery(String query) throws Exception {/*Quando criamos a tabela generica de consulta. Vai ter uma tabela que vai consultar os registros do BD*/
		validaSessionFactory();/*Sem a sessonFactor não for válida, vai dar problema. Recebendo o objeto*/
		Query queryReturn = sessionFactory.getCurrentSession().createQuery(query.toString());/*Passar uma instrução para o hibernate, e retornar um objeto query para passarmos parametros ou outras rotinas avançadas com query preparado antes de mandar para o BD*/
		return queryReturn;/*Retorno para Query*/
	}
	
	/**
	 * Realiza consulta no banco de dados, iniciand o carregamento a partir do
	 * registro passado no parametro -> iniciaNoRegistro e obtendo maximo de 
	 * resultados passados em -> maximoResultado.
	 * 
	 * @param query
	 * @param iniciaNoRegistro
	 * @param maximoResultado
	 * @return List<T>
	 * @throws Exception
	 */
	@Override
	public List<T> findListByQueryDinamica(String query, int iniciaNoRegistro, int maximoResultado) throws Exception {
		validaSessionFactory();/*Sem a sessonFactor não for válida, vai dar problema. Recebendo o objeto*/
		List<T> lista = new ArrayList<T>();/*Vai trazer uma lista generica*/
		lista = sessionFactory.getCurrentSession().createQuery(query).setFirstResult(iniciaNoRegistro).setMaxResults(maximoResultado).list();/*Vai ser usado no carregamento por demanda do hibernate, do inicioo do  registro, até o final do registro*/
		return lista;
	}
	
	/*Metodo que valida a SessionFactory*/
	private void validaSessionFactory() {
		if (sessionFactory == null) {
			sessionFactory = HibernateUtil.getSessionFactory();
		}
		validaTransaction();
	}
	
	/*Metodo para validade a transação com o banco de dados*/
	private void validaTransaction() {
		if (sessionFactory.getCurrentSession().getTransaction().isActive()) {
			sessionFactory.getCurrentSession().beginTransaction();
		}
	}
	
	/*Metodo que faz transações por Ajax*/
	public void commitProcessoAjax() {
		sessionFactory.getCurrentSession().beginTransaction().commit();/*Inicia a sessão corrente, pegando a trasação, e dar commit*/
	}
	
	/*Metódo de RollBack*/
	private void rollBackProcessoAjax() {
		sessionFactory.getCurrentSession().beginTransaction().rollback();;/*Inicia a sessão corrente, pegando a trasação, e o rollback*/
	}
	
	/*Metodo que roda instantaneamente a instrução o SQL no banco de dados*/
	private void executeFlushSession() {
		sessionFactory.getCurrentSession().flush();
	}
	
	/*Metodo que retorna uma lista de Array de objetos*/
	public List<Object[]> getListSQLDinamicaArray(String sql) throws Exception {/*Podia ser implementado tanto na interface, quanto aqui dentro*/
		validaSessionFactory();
		List<Object[]> lista = (List<Object[]>) sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		
		return lista;
	}
	
	/*Método que vai retornar um usuário, uma pessoa*/
	public List<T> findUniqueByQueryDinamica(String s) throws Exception {
		validaSessionFactory();
		List<T> lista = new ArrayList<T>();
		lista = sessionFactory.getCurrentSession().createQuery(s).list();
		return lista;
	}
	
	/**/
	public T findInuqueByProperty(Class<T> entidade, Object valor, String atributo, String condicao) throws Exception {
		
		validaSessionFactory();
		StringBuilder query = new StringBuilder();
		query.append(" select entity from ").append(entidade.getSimpleName())
			.append(" entity where entity.").append(atributo)
			.append(" = '").append(valor).append("'  ").append(condicao);
		
		T obj = (T) this.findUniqueByQueryDinamica(query.toString());
		
		return obj;
	}

}
