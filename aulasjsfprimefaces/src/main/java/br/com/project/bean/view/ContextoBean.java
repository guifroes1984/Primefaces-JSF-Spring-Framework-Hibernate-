package br.com.project.bean.view;

import java.io.Serializable;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import br.com.project.geral.controller.EntidadeController;
import br.com.project.geral.controller.SessionController;
import br.com.project.model.classes.Entidade;

@Scope(value = "session")
@Component(value = "contextoBean")/*Pode ser usada em qualquer lugar do sistema*/
public class ContextoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String USER_LOGADO_SESSAO = "userLogadoSessao";
	
	@Autowired
	private EntidadeController entidadeController;
	
	@Autowired
	private SessionController sessionController;

	/**
	 * Retorna todas as informações do usuário logado
	 * @return
	 */
	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();/*Retorna o usuário que está logado*/
	}

	public Entidade getEntidadeLogada() throws Exception {
		Entidade entidade = (Entidade) getExternalContext().getSessionMap().get(USER_LOGADO_SESSAO);/*Está recuperando e verificando se ele foi adicionado e pegando algumas informações dele*/
		
		if (entidade == null || (entidade != null && 
				!entidade.getEnt_login().equals(getUserPrincipal()))) {
			
			if (getAuthentication().isAuthenticated()) {/*Condição se o usuário foi autenticado*/
				entidadeController.updateUltimoAcessoUser(getAuthentication().getName());/*Atualiza a data de login*/
				entidade = entidadeController.findUserLogado(getAuthentication().getName());/*Consultar a minha entidade, qual é o meu usuario logado*/
				getExternalContext().getSessionMap().put(USER_LOGADO_SESSAO, entidade);/*Adiciona na sessão ou logou com outro usuário ou fez a troca*/
				sessionController.addSession(entidade.getEnt_login(), 
						(HttpSession) getExternalContext().getSession(true));
			}
		}
		
		return entidade;
	}
	
	private String getUserPrincipal() {
		return getExternalContext().getUserPrincipal().getName();
	}

	/**
	 * Consegue trazer várias informações de dados da aplicação
	 * @return
	 */
	public ExternalContext getExternalContext() {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		return externalContext;
	}

}
