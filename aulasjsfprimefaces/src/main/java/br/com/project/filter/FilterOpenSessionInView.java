package br.com.project.filter;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.filter.DelegatingFilterProxy;

import br.com.framework.hibernate.session.HibernateUtil;
import br.com.framework.util.UtilFramework;
import br.com.project.listener.ContextLoaderListenerCaixakiUtils;
import br.com.project.model.classes.Entidade;

/**
 *Quando implementamos um filtro na nossa aplicação , temos que saber que o Spring(nossa caso, ou JSP, ou JSF...), ele tem o seu 
 *filtro, e como aqui vamos trabalhar com Spring, integrado com JSF, quendo dar atenção maior do lado do Spring, temos que extender
 *a classe de filtro do Spring.
 */

@WebFilter(filterName = "conexaoFilter")/*Vai maperar todas as URL's, todas as requisições vão passar por aqui*/
public class FilterOpenSessionInView extends DelegatingFilterProxy implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	
	private static SessionFactory sf;/*Precisamos levantar o BD, para isso precisamos que o hibernate conecte no BD*/
	
	/**
	 * Nesse filter pecisamos dar atenção para 2 métodos que precisamos sobrescrever
	 */
	
	/*Executa apenas uma vez*/
	/*Executa quando a aplicação está sendo iniciado*/
	@Override
	protected void initFilterBean() throws ServletException {
		
		sf = HibernateUtil.getSessionFactory();/*Iniciando a nossa transação com o banco de dados*/
	}
	
	/**
	 * Esse método vai ser invocado para toda requisição, e toda resposta
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		
		/*JDBC Spring*/
		BasicDataSource springBasicDataSource = (BasicDataSource) ContextLoaderListenerCaixakiUtils.getBean("springDataSource");/*Pega o DataSource do Spring*/
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();/*Chama o DefaultTransactionDefinition(definição de transação). Criar um objeto para criar uma definição de transação do Spring*/
		PlatformTransactionManager transactionManager = new DataSourceTransactionManager(springBasicDataSource);/*Chamando o gerenciador de transação, vai ser DatasourceTransactionMenager, passando o gerenciador springBasicDataSource*/
		TransactionStatus status = transactionManager.getTransaction(def);/*Definindo a nossa transação. Para nós termos um status*/
		
		try {
			request.setCharacterEncoding("UTF-8");/*Define codificação. Setar a codifigação para utf-8, para não ter problema com acentuação*/
			
			/*Captura usuário que faz operaça*/
			HttpServletRequest request2 = (HttpServletRequest) request;/*Qual usuario que fez a atualização, inserção...*/
			HttpSession sessao = request2.getSession();/*Agora eu tenho a sessão*/
			Entidade userLogadoSessao = (Entidade) sessao.getAttribute("userLogadoSessao");/*Temos qual o usuario que está logado na Sessão. Sessão é o navegador aberto, tanto se tiver 2 navegadores na mesma maquina são 2 Sessões abertas*/
			
			if (userLogadoSessao != null) {/*Se o usuário realmente existir*/
				UtilFramework.getThreadLocal().set(userLogadoSessao.getEnt_codigo());/*Seta o código para tThreadLocal, que na hora que for gravar no BD o nosso hibernate envers ele vai interceptar, e vai recuperar o codigo do usuário e vai setar para o hibernate envers gravar no BD o log de quem fez a operação*/
			}
			
			/*Inicia uma transação. Sempre inicia uma transação do hibernate antes de efetuar qualquer operação do lado do servidor*/
			sf.getCurrentSession().beginTransaction();/*Iniciando uma transação do hibernate*/
			/*Antes de executar ação(Request)*/
			
			chain.doFilter(request, response);/*Executa nossa ação no servidor. Quando ele entrar nessa linha ele vai executar tudo que tem no nosso sistema*/
			/*Após de executar ação ele volta com a Resposta*/
			
			transactionManager.commit(status);/*Volta, e se estivermos usando o hibernate, temos que comitar, mais como estamos usando além do hibernate, estamos usando a transação JDBC, temos que pegar o transactionManager e dar o commit passando o status*/
			
			if (sf.getCurrentSession().getTransaction().isActive()) {/*Se a transação atual for ativa*/
				sf.getCurrentSession().flush();/*Executa os SQL no banco de dados*/
				sf.getCurrentSession().getTransaction().commit();/*Então comita*/
				
				/*Faz mais uma verificação. Lembrando que não é a conexão do Hibernate, é só a sessã oque foi criada para essa requisição*/
				if (sf.getCurrentSession().isOpen()) {/*Se a sessão estiver abera*/
					sf.getCurrentSession().close();/*Fecha a nossa sessão*/
				}
				
				response.setCharacterEncoding("UTF-8");/*Nosso objeto resposnse vamos setar a resposta tbm, para ele ficar corrigido os acentos*/
				response.setContentType("text/html; charset=UTF-8");/*Responder em texto HTML, para ele ficar corrigido os acentos*/
			}
			
		}catch (Exception e) {
			
			/*A parte da excessão, ser der erro*/
			transactionManager.rollback(status);/*Primeira coisa tratar o nosso JDBC(transactionManager), passando o objeto status*/
			
			e.printStackTrace();/*Mostra no console*/
			
			/*Trabalhar o hibernate*/
			if (sf.getCurrentSession().getTransaction().isActive()) {/*Se minha transação é ativa, não consegue dar o rollback se a transação não está ativa*/
				sf.getCurrentSession().getTransaction().rollback();/*Executou*/
			}
			
			/*Vamos para outra execução*/
			if (sf.getCurrentSession().isOpen()) {/*Se minha transação está aberta*/
				sf.getCurrentSession().close();/*Então fecha*/
			}
			
		}finally {/*Sempre temos que fazer um finally, independente se deu excessão ou não, se der certo executa o que está dentro*/
			
			/**
			 * Sempre vai verificar se tanto deu erro ou se deu certo. E vai verificar se transação está ativa  e tem que executar
			 * o que está no banco e tem que limpar, e dps fechar.
			 */
			
			/*Fazer a verificação que tem que fechar a sessão do hibernate*/
			if (sf.getCurrentSession().isOpen()) {/*Se a sessão é aberta*/
				if (sf.getCurrentSession().beginTransaction().isActive()) {/*Se a transação é ativa*/
					sf.getCurrentSession().flush();/*Executa os SQL no banco de dados*/
					sf.getCurrentSession().clear();/*Limpa a sessão para não ficar nada preso na sessão*/
				}
				
				/*Apos isso, vamos fazer a verificação do SessionFactory*/
				if (sf.getCurrentSession().isOpen()) {/*Se a seesão é aberta*/
					sf.getCurrentSession().close();/*Fecha a sessão*/
					
				}
			}
		}
	}

}
