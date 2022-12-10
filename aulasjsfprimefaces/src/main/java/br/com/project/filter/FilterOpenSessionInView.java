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
 *Quando implementamos um filtro na nossa aplica��o , temos que saber que o Spring(nossa caso, ou JSP, ou JSF...), ele tem o seu 
 *filtro, e como aqui vamos trabalhar com Spring, integrado com JSF, quendo dar aten��o maior do lado do Spring, temos que extender
 *a classe de filtro do Spring.
 */

@WebFilter(filterName = "conexaoFilter")/*Vai maperar todas as URL's, todas as requisi��es v�o passar por aqui*/
public class FilterOpenSessionInView extends DelegatingFilterProxy implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	
	private static SessionFactory sf;/*Precisamos levantar o BD, para isso precisamos que o hibernate conecte no BD*/
	
	/**
	 * Nesse filter pecisamos dar aten��o para 2 m�todos que precisamos sobrescrever
	 */
	
	/*Executa apenas uma vez*/
	/*Executa quando a aplica��o est� sendo iniciado*/
	@Override
	protected void initFilterBean() throws ServletException {
		
		sf = HibernateUtil.getSessionFactory();/*Iniciando a nossa transa��o com o banco de dados*/
	}
	
	/**
	 * Esse m�todo vai ser invocado para toda requisi��o, e toda resposta
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		
		/*JDBC Spring*/
		BasicDataSource springBasicDataSource = (BasicDataSource) ContextLoaderListenerCaixakiUtils.getBean("springDataSource");/*Pega o DataSource do Spring*/
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();/*Chama o DefaultTransactionDefinition(defini��o de transa��o). Criar um objeto para criar uma defini��o de transa��o do Spring*/
		PlatformTransactionManager transactionManager = new DataSourceTransactionManager(springBasicDataSource);/*Chamando o gerenciador de transa��o, vai ser DatasourceTransactionMenager, passando o gerenciador springBasicDataSource*/
		TransactionStatus status = transactionManager.getTransaction(def);/*Definindo a nossa transa��o. Para n�s termos um status*/
		
		try {
			request.setCharacterEncoding("UTF-8");/*Define codifica��o. Setar a codifiga��o para utf-8, para n�o ter problema com acentua��o*/
			
			/*Captura usu�rio que faz opera�a*/
			HttpServletRequest request2 = (HttpServletRequest) request;/*Qual usuario que fez a atualiza��o, inser��o...*/
			HttpSession sessao = request2.getSession();/*Agora eu tenho a sess�o*/
			Entidade userLogadoSessao = (Entidade) sessao.getAttribute("userLogadoSessao");/*Temos qual o usuario que est� logado na Sess�o. Sess�o � o navegador aberto, tanto se tiver 2 navegadores na mesma maquina s�o 2 Sess�es abertas*/
			
			if (userLogadoSessao != null) {/*Se o usu�rio realmente existir*/
				UtilFramework.getThreadLocal().set(userLogadoSessao.getEnt_codigo());/*Seta o c�digo para tThreadLocal, que na hora que for gravar no BD o nosso hibernate envers ele vai interceptar, e vai recuperar o codigo do usu�rio e vai setar para o hibernate envers gravar no BD o log de quem fez a opera��o*/
			}
			
			/*Inicia uma transa��o. Sempre inicia uma transa��o do hibernate antes de efetuar qualquer opera��o do lado do servidor*/
			sf.getCurrentSession().beginTransaction();/*Iniciando uma transa��o do hibernate*/
			/*Antes de executar a��o(Request)*/
			
			chain.doFilter(request, response);/*Executa nossa a��o no servidor. Quando ele entrar nessa linha ele vai executar tudo que tem no nosso sistema*/
			/*Ap�s de executar a��o ele volta com a Resposta*/
			
			transactionManager.commit(status);/*Volta, e se estivermos usando o hibernate, temos que comitar, mais como estamos usando al�m do hibernate, estamos usando a transa��o JDBC, temos que pegar o transactionManager e dar o commit passando o status*/
			
			if (sf.getCurrentSession().getTransaction().isActive()) {/*Se a transa��o atual for ativa*/
				sf.getCurrentSession().flush();/*Executa os SQL no banco de dados*/
				sf.getCurrentSession().getTransaction().commit();/*Ent�o comita*/
				
				/*Faz mais uma verifica��o. Lembrando que n�o � a conex�o do Hibernate, � s� a sess� oque foi criada para essa requisi��o*/
				if (sf.getCurrentSession().isOpen()) {/*Se a sess�o estiver abera*/
					sf.getCurrentSession().close();/*Fecha a nossa sess�o*/
				}
				
				response.setCharacterEncoding("UTF-8");/*Nosso objeto resposnse vamos setar a resposta tbm, para ele ficar corrigido os acentos*/
				response.setContentType("text/html; charset=UTF-8");/*Responder em texto HTML, para ele ficar corrigido os acentos*/
			}
			
		}catch (Exception e) {
			
			/*A parte da excess�o, ser der erro*/
			transactionManager.rollback(status);/*Primeira coisa tratar o nosso JDBC(transactionManager), passando o objeto status*/
			
			e.printStackTrace();/*Mostra no console*/
			
			/*Trabalhar o hibernate*/
			if (sf.getCurrentSession().getTransaction().isActive()) {/*Se minha transa��o � ativa, n�o consegue dar o rollback se a transa��o n�o est� ativa*/
				sf.getCurrentSession().getTransaction().rollback();/*Executou*/
			}
			
			/*Vamos para outra execu��o*/
			if (sf.getCurrentSession().isOpen()) {/*Se minha transa��o est� aberta*/
				sf.getCurrentSession().close();/*Ent�o fecha*/
			}
			
		}finally {/*Sempre temos que fazer um finally, independente se deu excess�o ou n�o, se der certo executa o que est� dentro*/
			
			/**
			 * Sempre vai verificar se tanto deu erro ou se deu certo. E vai verificar se transa��o est� ativa  e tem que executar
			 * o que est� no banco e tem que limpar, e dps fechar.
			 */
			
			/*Fazer a verifica��o que tem que fechar a sess�o do hibernate*/
			if (sf.getCurrentSession().isOpen()) {/*Se a sess�o � aberta*/
				if (sf.getCurrentSession().beginTransaction().isActive()) {/*Se a transa��o � ativa*/
					sf.getCurrentSession().flush();/*Executa os SQL no banco de dados*/
					sf.getCurrentSession().clear();/*Limpa a sess�o para n�o ficar nada preso na sess�o*/
				}
				
				/*Apos isso, vamos fazer a verifica��o do SessionFactory*/
				if (sf.getCurrentSession().isOpen()) {/*Se a sees�o � aberta*/
					sf.getCurrentSession().close();/*Fecha a sess�o*/
					
				}
			}
		}
	}

}
