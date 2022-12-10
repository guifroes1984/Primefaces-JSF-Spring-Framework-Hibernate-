package br.com.project.exception;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

public class CustomExceptionHandleFactory extends ExceptionHandlerFactory {
	
	private ExceptionHandlerFactory parent;/*Temos que ter um objeto privado do ExceptionHandlerFactory*/
	
	/**
	 * Contrutor implicito. E o contrutor vai receber o objeto privado de ExceptionHandlerFactory
	 */
	public CustomExceptionHandleFactory(ExceptionHandlerFactory parent) {
		this.parent = parent;/*Nosso objeto atual vai receber o objeto por parametro*/
	}
	
	/**
	 * Metodo sobrescrever
	 */
	@Override
	public ExceptionHandler getExceptionHandler() {
		ExceptionHandler handler = new CustomExceptionHandler(parent.getExceptionHandler());/*Criamos ExceptionHandler, vai ser igual a nossa classe que criamos CustomExceptionHandler, pegando o objeto pai(parent.getExceptionHandler)*/
		return handler;/*Retorna nosso objeto de mensagem*/
	}

}
