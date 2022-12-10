package teste.junit;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.Test;

import br.com.project.report.util.DateUtils;

/**
 * JUnit é para testes unitários, 
 */
public class TesteData {

	@Test/*Teste unitário tem que ter @Test*/
	public void testData() {
		
		try {/*Sempre trabalha colocando um try-catch*/
		
		/*System.out.println(DateUtils.getDateAtualReportName()); Teste Unitário*/
		assertEquals("27102022", DateUtils.getDateAtualReportName());/*Por aqui eu consigo estabelecer e testar o metodo para saber se está executando perfeitamente*/
		
		assertEquals("'2022-10-27'", DateUtils.formatDateSql(Calendar.getInstance().getTime()));/*Espera esse valor aqui Ex: "'2022-10-27'"*/
		
		assertEquals("2022-10-27", DateUtils.formatDateSqlSimple(Calendar.getInstance().getTime()));/*Espera esse valor aqui Ex: "2022-10-27" sem as aspas simples*/
		
		}catch (Exception e) {/*Qualquer exeção*/
			e.printStackTrace();/*Imprime no console*/
			fail(e.getMessage());/*E da um fail, passando a mensagem aqui dentro*/
		}
	}

}
