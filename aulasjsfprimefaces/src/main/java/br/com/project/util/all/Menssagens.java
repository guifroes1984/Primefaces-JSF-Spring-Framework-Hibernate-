package br.com.project.util.all;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;


/**
 * FacesContext-> � a classe que temos acesso a todas a informa��es do JSF do projeto 
 *
 */
public abstract class Menssagens extends FacesContext implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public Menssagens() {/*Construtor padr�o vazio*/
	}
	
	/**
	 * Metodo de mensagem generica
	 */
	public static void msg(String msg) {
		if (facesContextValido()) {/*Vamos valida e ver se facesContext � valido*/
			getFacesContext().addMessage("msg", new FacesMessage(msg));/*Chamando o getFacesContext, e adicionano ao nosso objeto de mensagem da tela, que ainda ser� criado. Passando uma nova mensagem*/
		}
	}
	
	/**
	 * Metodo de dar mensagem de sucesso de uma vez s�
	 */
	public static void sucesso() {
		msgSeverityFatal(Constante.OPERACAO_REALIZADA_COM_SUCESSO);/*S� passa msgSeverityFatal, e a classe Constate com a mensagem: "Opera��o Realizada com Sucesso"*/
	}
	
	/**
	 * Metodo de status de opera��o
	 */
	public static void responseOperation(EstatusPersistencia estatusPersistencia) {/*Passando a classe EstatusPersistencia*/
		if (estatusPersistencia != null && 
				estatusPersistencia.equals(EstatusPersistencia.SUCESSO)) {/*Se o nosso estatusPersistencia for v�lido, e o nosso Objeto EstatusPersistencia for igual a "SUCESSO". Sempre fazer a valida��o diferente de null, evita muito erro*/
			sucesso();/*J� chamamos o m�tdo "sucesso()"*/
		}else if (estatusPersistencia != null && estatusPersistencia.equals(EstatusPersistencia.OBJETO_REFERENCIADO)) {/*Sen�o. O nosso estatusPersistencia � valido � diferente de null e estatusPersistencia � igual ao objeto EstatusPersistencia for o OBJETO_REFERENCIADO*/
			msgSeverityFatal(EstatusPersistencia.OBJETO_REFERENCIADO.toString());/*Da uma mensagem fata, e da uma mensagem pr� pronta que � OBJETO_REFERENCIADO dando a mensagem: "Esse objeto n�o pode ser apagadopor posuir refer�ncias ao mesmo.", chamando o enum, ele ia mostrar s� o valor de: OBJETO_REFERENCIADO na tela, quando passamos o ToString sobrescrevemos e ele mostra o "name" que � a mensagem de: "Esse objeto n�o pode ser apagadopor posuir refer�ncias ao mesmo."*/
		}else {/*Se n�o foi "sucesso()", se n�o foi "msgSeverityFatal"*/
			erroNaOperacao();/*A� deu erro na opera��o*/
		}
	}
	
	/**
	 * Metodos direto para auxiliar para nos auxiliar mais ainda
	 */
	public static void erroNaOperacao() {
		if (facesContextValido()) {/*Vamos valida e ver se facesContext � valido*/
			msgSeverityFatal(Constante.ERRO_NA_OPRECAO);/*Passa o metodo msgSeverityFatal, e dps a classe Constante mostrando a mensagem "N�o foi poss�vel Executar a Opera��o". Unindo tudo que estamos criando.*/
		}
	}
	
	/**
	 * Metodo para retornar o FacesContext, que � muito util em toda aplica��o
	 */
	public static FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}
	
	
	/*Definir alguns m�todos padr�es.*/
	 
	/**
	 * Metodo para definir se o FacesContext � valido
	 */
	private static boolean facesContextValido() {
		return getFacesContext() != null;
	}
	
	/**
	 * M�todo de mensagem WARN
	 */
	public static void msgSeverityWarn(String msg) {
		if (facesContextValido()) {/*Se o facesContext � valido*/
			getFacesContext().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg));/*Se for valido entra na mensagem, adiciona ela ao atributo na pagina JSF chamada "msg" vai ser um atributo de mensagem. Criando um novo objeto FacesMensage, passando a severidade devinindo a cor, no caso SEVERITY_WARN.*/
		}
	}
	
	/**
	 * Metodo de mensagem FATAL
	 */
	public static void msgSeverityFatal(String msg) {
		if (facesContextValido()) {/*Se o facesContext � valido*/
			getFacesContext().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_FATAL, msg, msg));/*Se for valido entra na mensagem, adiciona ela ao atributo na pagina JSF chamada "msg" vai ser um atributo de mensagem. Criando um novo objeto FacesMensage, passando a severidade devinindo a cor, no caso SEVERITY_FATAL.*/
		}
	}
	
	/**
	 * Metodo de mensagem ERROR
	 */
	public static void msgSeverityError(String msg) {
		if (facesContextValido()) {/*Se o facesContext � valido*/
			getFacesContext().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));/*Se for valido entra na mensagem, adiciona ela ao atributo na pagina JSF chamada "msg" vai ser um atributo de mensagem. Criando um novo objeto FacesMensage, passando a severidade devinindo a cor, no caso SEVERITY_ERROR.*/
		}
	}
	
	/**
	 * Metodo de mensagem INFO
	 */
	public static void msgSeverityInfo(String msg) {
		if (facesContextValido()) {/*Se o facesContext � valido*/
			getFacesContext().addMessage("msg", new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg));/*Se for valido entra na mensagem, adiciona ela ao atributo na pagina JSF chamada "msg" vai ser um atributo de mensagem. Criando um novo objeto FacesMensage, passando a severidade devinindo a cor, no caso SEVERITY_INFO.*/
		}
	}

}
