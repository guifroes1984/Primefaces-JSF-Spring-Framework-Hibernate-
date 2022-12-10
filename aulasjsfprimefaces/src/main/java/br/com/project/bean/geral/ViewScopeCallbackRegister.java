package br.com.project.bean.geral;

import java.util.HashMap;
import java.util.Map;

import javax.faces.component.UIViewRoot;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PostConstructViewMapEvent;
import javax.faces.event.PreDestroyViewMapEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.ViewMapListener;

public class ViewScopeCallbackRegister
		implements ViewMapListener {/*Toda vez que o Scope e o ManageBean forem criados, e forem destruidos, vai passar por essa classe AQUI...*/

	/**
	 * Temos que definir, fazer uma implementa��o colocando o source como passagem
	 * de parametro. E no return temos que definir se o source � uma
	 * instancia(instanceof), da raiz de componentes do JSF(UIViewRoot). O
	 * ViewMapListener vai controlar isso para n�s.
	 */
	@Override
	public boolean isListenerForSource(Object source) {
		return source instanceof UIViewRoot;
	}

	/**
	 * Dentro do processo n�s vamos definir o controle de contexto do ViewScope.
	 * Ent�o no P�s-construct e no Pr�-destroier
	 */
	@Override
	public void processEvent(SystemEvent event) throws AbortProcessingException {
		if (event instanceof PostConstructViewMapEvent) {/*Se o novo evento for do PostConstructViewMapEvent*/
			
			PostConstructViewMapEvent viewMapEvent = (PostConstructViewMapEvent) event;/*Inicia um PostConstructViewMapEvent*/
			UIViewRoot uiViewRoot = (UIViewRoot) viewMapEvent.getComponent();/*Raiz de componente, vamos pegar todos os componentes*/
			uiViewRoot.getViewMap().put(ViewScope.VIEW_SCOPE_CALLBACKS, new HashMap<String, Runnable>());/*Pegando todos os componentes. E vamos instanciar uma lista nova. Quando inciar um novo Scope, faz a verifica��o para n�s, vai colocar os parametros em vazio, pq estamos iniciando uma lista nova.*/
			
		} else if (event instanceof PreDestroyViewMapEvent) {/*Se for para destruir, faz a verifica��o se objeto � de PreDestroyViewMapEvent*/
			
			PreDestroyViewMapEvent viewMapEvent = (PreDestroyViewMapEvent) event;/*Inicia um PreDestroyViewMapEvent*/
			UIViewRoot viewRoot = (UIViewRoot) viewMapEvent.getComponent();/*Pegando a nossa arvore de componentes*/
			Map<String, Runnable> callbacks = (Map<String, Runnable>) viewRoot.getViewMap()
					.get(ViewScope.VIEW_SCOPE_CALLBACKS);/*Inicia o metodo, com os callbacks, o nosso viewRoot com getViewMap, ViewScope.VIEW_SCOPE_CALLBACKS(Mapa de objeto)*/
			
			if (callbacks != null) {/*Se existirem os componentes para serem destruidos*/
				for (Runnable c : callbacks.values()) {/*Inicia um for com os objetos: Runnable e callbacks.values(todos os valores do nosso metodo)*/
					c.run();/*Pega o objeto e executar*/
				}

				callbacks.clear();/*Depois que executar tudo, limpa toda a nossa lista de componentes*/
			}
		}
	}

}
