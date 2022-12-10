package br.com.project.exception;

import java.util.Iterator;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

import org.hibernate.SessionFactory;
import org.primefaces.context.RequestContext;

import br.com.framework.hibernate.session.HibernateUtil;

/**
 * Essa classe é responsável por interceptar os erros para nós
 */
public class CustomExceptionHandler extends ExceptionHandlerWrapper {
	
	
	private ExceptionHandler wrapperd;/*Precisamos de uma classe do ExceptionHandler*/
	
	final FacesContext facesContext = FacesContext.getCurrentInstance();/*Precisamos obter o mapa da requisição do FacesContext*/
	
	final Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();/*Temos todos os parametros da requisição*/
	
	final NavigationHandler navigationHandler = facesContext.getApplication().getNavigationHandler();/*Saber o estado atual da navegasção entre as paginas. Pegar o estado da navegação*/
	
	/**
	 * Estamos obrigando toda vez que existir uma instanciação dessa classe tem que ter um construtor, passando 
	 */
	public CustomExceptionHandler(ExceptionHandler exceptionHandler) {/*Precisamos de um construtor, passando ExceptionHandler*/
		this.wrapperd = exceptionHandler;/*Pegando o wrapperd, vai ser igual a exceptionHandler*/
	}
	
	/**
	 * Para tratar os erros precisamos sobrescrever esse dois metódos
	 * 
	 * Sobrescreve o metodo ExceptionHandler que retona a "pilha" de exceções
	 */
	@Override
	public ExceptionHandler getWrapped() {
		return wrapperd;/*Retorna a classe de erro*/
	}
	
	/**
	 * Sobrescreve o metodo handle que é responsavel por manipular as exceções
	 * do JSF 
	 */
	@Override
	public void handle() throws FacesException {
		final Iterator<ExceptionQueuedEvent> iterator = getUnhandledExceptionQueuedEvents().iterator();/*Instanciando um objeto, que pode ter uma lista de erros, e temos que pegar calssed nossa do JSF e conterar os erros*/
		
		/*Sempre trabalharmos com o Iterator sempre trabalhamos com um While*/
		
		while (iterator.hasNext()) {/*Enquanto tiver dados nessa lista, vamos "iterar", percorrer ela*/
			ExceptionQueuedEvent event = iterator.next();/*Precisamos da classe ExceptionQueuedEvent temos event, que vai ser igual iterator.next, que ele vai retornar o objeto de erro pra nós*/
			ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();/*Precisamos da outra classe ExceptionQueuedEventContext temos o context, fazendo um cash(ExceptionQueuedEventContext), e o event.getSource vai retornar esse objeto do evento*/
			
			/**
			 * Recuperar a exceção do contexto. A exceção trabalhamos com a Classe do Java Throwable
			 */
			Throwable exeption = context.getException();
			
			/**
			 * Aqui trabalhamos a exceção
			 */
			try {
				
				requestMap.put("excptionMessage", exeption.getMessage());/*Podemos adicionar um código com o valor da mensagem*/
				
				if (exeption != null && exeption.getMessage().indexOf("ConstraintViolationException") != -1) {/*Se a nossa exeção é diferente de null e(&&) o nosso exeption.getMessage tbm é diferente de null e(&&) a nossa exeption.getMessage(se a nossa mensagem tiver uma violação de chave estrangeira), ela for diferente de -1 se realmente existir*/
					
					FacesContext.getCurrentInstance().
					addMessage("msg", new FacesMessage(FacesMessage.
							SEVERITY_WARN, "Registro não pode ser removido por" + " estar associdado.",""));
					
				}else if (exeption != null && exeption.getMessage().indexOf("org.hibernate.StaleObjectStateException") != -1) {/*Senão se verifica se a exeção é diferente de null, a mensagem é diferente de nula, e seu meu objeto de exeçãoa minha mensagem.indexOf se a nossa msg conter "org.hibernate.StaleObjectStateException"(essa exeção aqui), diferente de -1*/
					
					FacesContext.getCurrentInstance().
					addMessage("msg", new FacesMessage(FacesMessage.
							SEVERITY_WARN, "Registro foi atualizado ou excluido por outro usuário." + " Consulte novamente",""));/*Ai ela vai iniciar pegar o contexto do JSF, vai adicionar a "msg" para esse objeto, new Message iniciando um objeto de mensagem, passando a severidade(SEVERITY_WARN), e dando a mensagem*/
					
				}else {
					
					/**
					 * Esse objeto de mensagem, essa forma de dar a mensagem em JSF é extremamente util a todo momento. Essa forma é quando
					 * estiver utilizando um Ajax e ele vai exibir.
					 */
					
					/*Avisa o usuario do erro*/
					FacesContext.getCurrentInstance().
					addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_FATAL, "O sistema se recuperou de um erro inesperado.", ""));
					
					/*Tranquiliza o usuário para que ele continue usando o sistema*/
					FacesContext.getCurrentInstance().
					addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_INFO, "Você pode continuar usando o sistema normalmente!", ""));
					
					/**/
					FacesContext.getCurrentInstance().
					addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_FATAL, "O erro foi causado por:\n" + exeption.getMessage(), ""));
					
					/**
					 * Esse RequestContext é do PrimeFaces
					 * Esse alert apenas é exibido se a pagina não redirecionar
					 */
					RequestContext.getCurrentInstance().execute("alert('O sistema se recuperou de um erro inesperado.')");
					
					/**
					 * É um objeto do PrimeFces que pula na tela é showMessageInDialog que é uma janelinha do PrimeFaces
					 */
					RequestContext.getCurrentInstance()
					.showMessageInDialog(new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro", "O sistema se recuperou de um erro inesperado."));
					
					/**
					 * Redireciona para a página de erro
					 * 
					 * Se quiser fazer o redirecionamento, tem que pegar o objeto navegação ,dar um handleNavigation, passando o facesContext,
					 * e passar a nossa pagina de erro.jsf que ainda vamos criar. E tem que falar parao JSF que redirecionar ele e expirar o 
					 * contexto.
					 */
					navigationHandler.handleNavigation(facesContext, null, 
							"/error/error.jsf?faces-redirect=true&expired=true");
				}
				
				/*Redireciona a pagina de erro e exibe as mensagens*/
				facesContext.renderResponse();
				
			} finally {/*Se acontecer algum erro*/
				SessionFactory sf = HibernateUtil.getSessionFactory();
				if (sf.getCurrentSession().getTransaction().isActive()) {/*Se tiver uma transação ativa, vamos pegar.*/
					sf.getCurrentSession().getTransaction().rollback();/*E dar um rollback*/
				}
				
				/*Imprime o erro no console*/
				exeption.printStackTrace();
				
				iterator.remove();/*Trabalhamos o objeto de exceção e vamos remover ele*/
			}
		}
		
		/*Apos manipular todos os erros, de nós darmos as msgs de erros, finalizamos a manipulação*/
		getWrapped().handle();/*Finaliza para nós*/
		
	}

}
