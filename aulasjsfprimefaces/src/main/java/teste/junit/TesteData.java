package teste.junit;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.Test;

import br.com.project.report.util.DateUtils;

/**
 * JUnit � para testes unit�rios, 
 */
public class TesteData {

	@Test/*Teste unit�rio tem que ter @Test*/
	public void testData() {
		
		try {/*Sempre trabalha colocando um try-catch*/
		
		/*System.out.println(DateUtils.getDateAtualReportName()); Teste Unit�rio*/
		assertEquals("27102022", DateUtils.getDateAtualReportName());/*Por aqui eu consigo estabelecer e testar o metodo para saber se est� executando perfeitamente*/
		
		assertEquals("'2022-10-27'", DateUtils.formatDateSql(Calendar.getInstance().getTime()));/*Espera esse valor aqui Ex: "'2022-10-27'"*/
		
		assertEquals("2022-10-27", DateUtils.formatDateSqlSimple(Calendar.getInstance().getTime()));/*Espera esse valor aqui Ex: "2022-10-27" sem as aspas simples*/
		
		}catch (Exception e) {/*Qualquer exe��o*/
			e.printStackTrace();/*Imprime no console*/
			fail(e.getMessage());/*E da um fail, passando a mensagem aqui dentro*/
		}
	}

}
