package br.com.project.listener;

import java.io.Serializable;

import javax.faces.bean.ApplicationScoped;

import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Essa classe vai prover todas as informa��es do ambiente de inform��o do Spring. � unica para todo o sistema
 *
 */

@ApplicationScoped/*Para dizer para o JSF que � uma instancia unica para todo o projeto*/
public class ContextLoaderListenerCaixakiUtils extends ContextLoaderListener implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Esse m�todo vai retornar todo o contexto do Spring pra gente, do ambiente de execu��o
	 */
	private static WebApplicationContext getWac() {
		return WebApplicationContextUtils.getWebApplicationContext(getCurrentWebApplicationContext().getServletContext());
	}
	
	/**
	 * Esse m�todo passa o "Id"
	 */
	public static Object getBean(String idNomeBean) {
		return getWac().getBean(idNomeBean);
	}
	
	/**
	 * Esse m�todo se passar a Classe ele retorna a inst�ncia do objeto que est� sendo manupulado pelo contexto do Spring
	 */
	public static Object getBean(Class<?> classe) {
		return getWac().getBean(classe);
	}

}
