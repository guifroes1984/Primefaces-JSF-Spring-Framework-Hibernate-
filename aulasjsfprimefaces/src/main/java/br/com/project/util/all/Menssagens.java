package br.com.project.util.all;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;


/**
 * FacesContext-> É a classe que temos acesso a todas a informações do JSF do projeto 
 *
 */
public abstract class Menssagens extends FacesContext implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public Menssagens() {/*Construtor padrão vazio*/
	}
	
	/**
	 * Metodo de mensagem generica
	 */
	public static void msg(String msg) {
		if (facesContextValido()) {/*Vamos valida e ver se facesContext é valido*/
			getFacesContext().addMessage("msg", new FacesMessage(msg));/*Chamando o getFacesContext, e adicionano ao nosso objeto de mensagem da tela, que ainda será criado. Passando uma nova mensagem*/
		}
	}
	
	/**
	 * Metodo de dar mensagem de sucesso de uma vez só
	 */
	public static void sucesso() {
		msgSeverityFatal(Constante.OPERACAO_REALIZADA_COM_SUCESSO);/*Só passa msgSeverityFatal, e a classe Constate com a mensagem: "Operação Realizada com Sucesso"*/
	}
	
	/**
	 * Metodo de status de operação
	 */
	public static void responseOperation(EstatusPersistencia estatusPersistencia) {/*Passando a classe EstatusPersistencia*/
		if (estatusPersistencia != null && 
				estatusPersistencia.equals(EstatusPersistencia.SUCESSO)) {/*Se o nosso estatusPersistencia for válido, e o nosso Objeto EstatusPersistencia for igual a "SUCESSO". Sempre fazer a validação diferente de null, evita muito erro*/
			sucesso();/*Já chamamos o métdo "sucesso()"*/
		}else if (estatusPersistencia != null && estatusPersistencia.equals(EstatusPersistencia.OBJETO_REFERENCIADO)) {/*Senão. O nosso estatusPersistencia é valido é diferente de null e estatusPersistencia é igual ao objeto EstatusPersistencia for o OBJETO_REFERENCIADO*/
			msgSeverityFatal(EstatusPersistencia.OBJETO_REFERENCIADO.toString());/*Da uma mensagem fata, e da uma mensagem pré pronta que é OBJETO_REFERENCIADO dando a mensagem: "Esse objeto não pode ser apagadopor posuir referências ao mesmo.", chamando o enum, ele ia mostrar só o valor de: OBJETO_REFERENCIADO na tela, quando passamos o ToString sobrescrevemos e ele mostra o "name" que é a mensagem de: "Esse objeto não pode ser apagadopor posuir referências ao mesmo."*/
		}else {/*Se não foi "sucesso()", se não foi "msgSeverityFatal"*/
			erroNaOperacao();/*Aí deu erro na operação*/
		}
	}
	
	/**
	 * Metodos direto para auxiliar para nos auxiliar mais ainda
	 */
	public static void erroNaOperacao() {
		if (facesContextValido()) {/*Vamos valida e ver se facesContext é valido*/
			msgSeverityFatal(Constante.ERRO_NA_OPRECAO);/*Passa o metodo msgSeverityFatal, e dps a classe Constante mostrando a mensagem "Não foi possível Executar a Operação". Unindo tudo que estamos criando.*/
		}
	}
	
	/**
	 * Metodo para retornar o FacesContext, que é muito util em toda aplicação
	 */
	public static FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}
	
	
	/*Definir alguns métodos padrões.*/
	 
	/**
	 * Metodo para definir se o FacesContext é valido
	 */
	private static boolean facesContextValido() {
		return getFacesContext() != null;
	}
	
	/**
	 * Método de mensagem WARN
	 */
	public static void msgSeverityWarn(String msg) {
		if (facesContextValido()) {/*Se o facesContext é valido*/
			getFacesContext().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg));/*Se for valido entra na mensagem, adiciona ela ao atributo na pagina JSF chamada "msg" vai ser um atributo de mensagem. Criando um novo objeto FacesMensage, passando a severidade devinindo a cor, no caso SEVERITY_WARN.*/
		}
	}
	
	/**
	 * Metodo de mensagem FATAL
	 */
	public static void msgSeverityFatal(String msg) {
		if (facesContextValido()) {/*Se o facesContext é valido*/
			getFacesContext().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_FATAL, msg, msg));/*Se for valido entra na mensagem, adiciona ela ao atributo na pagina JSF chamada "msg" vai ser um atributo de mensagem. Criando um novo objeto FacesMensage, passando a severidade devinindo a cor, no caso SEVERITY_FATAL.*/
		}
	}
	
	/**
	 * Metodo de mensagem ERROR
	 */
	public static void msgSeverityError(String msg) {
		if (facesContextValido()) {/*Se o facesContext é valido*/
			getFacesContext().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));/*Se for valido entra na mensagem, adiciona ela ao atributo na pagina JSF chamada "msg" vai ser um atributo de mensagem. Criando um novo objeto FacesMensage, passando a severidade devinindo a cor, no caso SEVERITY_ERROR.*/
		}
	}
	
	/**
	 * Metodo de mensagem INFO
	 */
	public static void msgSeverityInfo(String msg) {
		if (facesContextValido()) {/*Se o facesContext é valido*/
			getFacesContext().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg));/*Se for valido entra na mensagem, adiciona ela ao atributo na pagina JSF chamada "msg" vai ser um atributo de mensagem. Criando um novo objeto FacesMensage, passando a severidade devinindo a cor, no caso SEVERITY_INFO.*/
		}
	}

}
