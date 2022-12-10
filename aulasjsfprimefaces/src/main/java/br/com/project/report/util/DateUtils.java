package br.com.project.report.util;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Metodo que vai retornar a dada atual
	 */
	public static String getDateAtualReportName() {
		DateFormat df = new SimpleDateFormat("ddMMyyyy");/*Trabalhando com o DateFormat. Passando o formato da data que queremos ex: 24/09/1984*/
		return df.format(Calendar.getInstance().getTime());/*Retornar a data formatada para nós*/
	}
	
	/**
	 * Método que recebe uma data
	 */
	public static String formatDateSql(Date data) {
		StringBuffer retorno = new StringBuffer();/*Instanciando um novo objeto*/
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");/*Formato para gravar no Banco de daos*/
		retorno.append("'");/*Vai colocar uma aspas simples*/
		retorno.append(df.format(data));/*Vai formatar a data para nós*/
		retorno.append("'");/*Vai colocar outra aspas simples*/
		return retorno.toString();/*Pega o retorno.ToString*/
	}
	
	/**
	 * 
	 */
	public static String formatDateSqlSimple(Date data) {
		StringBuffer retorno = new StringBuffer();/*Instanciando um novo objeto*/
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");/*Formato para gravar no Banco de daos*/
		retorno.append(df.format(data));/*Vai formatar a data para nós*/
		return retorno.toString();/*Pega o retorno.ToString*/
	}
}
