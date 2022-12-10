package br.com.project.bean.geral;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.web.context.request.FacesRequestAttributes;

/**
 * Primeira classe para registrar o Scope de View no Sprinng
 */
@SuppressWarnings("unchecked")
public class ViewScope implements Scope, Serializable {/**/
	
	private static final long serialVersionUID = 1L;
	
	public static final String VIEW_SCOPE_CALLBACKS = "viewScope.callBacks";
	
	/**
	 * Método GET responsavel que vai retornar o objeto de escopo para nós
	 */
	@Override
	public Object get(String name, ObjectFactory<?> objectFactory) {
		Object instance = getViewMap().get(name);/*Pega a instancia, chamando o método que foi criado lá em baixo, tentanto passar passar um key, que é o name do objeto*/
		if (instance == null) {/*condição se estuver nula*/
			instance = objectFactory.getObject();/*instancia vai ser igual objeto que veio como parâmetro*/
			getViewMap().put(name, instance);/*Vamos adicionar o name, e o objeto que é a instance*/
		}
		return instance;/*Retorna a instance*/
	}
	
	/**
	 * Metodo que carrega o ID e identifica nossos escopos
	 */
	@Override
	public String getConversationId() {/*Assim mantendo um ID unico para cada escopo de View*/
		FacesContext facesContext = FacesContext.getCurrentInstance();/*Intancia do nosso JSF*/
		FacesRequestAttributes facesRequestAttributes = new FacesRequestAttributes(facesContext);/*Intanciando um novo objeto*/
		return facesRequestAttributes.getSessionId() + "-" + facesContext.getViewRoot().getViewId();/*Pega o ID da sessão, com o ID dos componentes do JSF*/
	}
	
	/**
	 * Metodo que é utilizado onde ele registra a destruição do Scope
	 * 
	 * Parte que irá fazer o registro da destruição do nosso Scope. O que acontece quando sair da tela, 
	 * em uma tela que exista essa implementação do ViewScope, para cada tela podemos implemetar um tipo de Scope.
	 * ViewScope, RequestScope e assim por diante. Quando sair da nossa tela o scope de view ele morre, então é com
	 * esse metodo ele vai ser encerrado.
	 */
	
	@Override
	public void registerDestructionCallback(String nome, Runnable runnable) {/**/
		Map<String, Runnable> callbacks = (Map<String, Runnable>) getViewMap().get(VIEW_SCOPE_CALLBACKS);
		
		if (callbacks != null) {/*Se ele existir. Estiver registrado*/
			callbacks.put(VIEW_SCOPE_CALLBACKS, runnable);/*passa o proprio objeto, da um nome para ele(VIEW_SCOPE_CALLBACKS), e passa o runnable para fazer tudo por debaixo dos panos*/
		}
	}
	
	/**
	 * Método REMOVE responsavel que vai retornar o objeto de escopo para nós
	 */
	@Override
	public Object remove(String name) {
		Object instance = getViewMap().remove(name);/*Objeto genérico, retorna do metodo que criamos, removendo e retorna o objeto removido*/
		if (instance != null) {/*Condição se instance for diferente de nulo*/
			Map<String, Runnable> callBack = (Map<String, Runnable>) getViewMap().get(VIEW_SCOPE_CALLBACKS);/*se existir no Mapa de parametros, vai entrar na condição, instanciando um Map String, e instancia um objeto Runnable para executar por baixo dos panos*/
			if (callBack != null) {/*Outra verificação se existir no Mapa*/
				callBack.remove(name);/*Dá um callBack.remove passando o objeto */
			}
		}
		return instance;/*Retorna a instancia que foi removida*/
	}
	
	/**
	 * Metodo que resolve a referencia dos objetos para nós do Scope. Por exemplo: Se é um HTTP request retorna um objeto correto para nós
	 */
	@Override
	public Object resolveContextualObject(String name) {
		FacesContext facesContext = FacesContext.getCurrentInstance();/*Intancia do nosso JSF*/
		FacesRequestAttributes facesRequestAttributes = new FacesRequestAttributes(facesContext);/*Intanciando um novo objeto*/
		return facesRequestAttributes.resolveReference(name);/*Retorna os atributos do JSF, passando o que vem por parametro no caso o name*/
	}
	
	/**
	 * Contruir um método que retorna Viem e Map para nós
	 * 
	 * getViewRoot()
	 * Retorna o componente raiz que está associado a esta solicitação(request).
	 * getViewMap-> Retorna um Map que atua como a interface para o armazenamento de dados
	 */
	private Map<String, Object> getViewMap() {
		return FacesContext.getCurrentInstance() != null ? 
				FacesContext.getCurrentInstance().getViewRoot().getViewMap() : new HashMap<String, Object>();
	}

}
