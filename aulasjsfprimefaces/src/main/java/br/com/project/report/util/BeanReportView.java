package br.com.project.report.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.primefaces.model.StreamedContent;
import org.springframework.stereotype.Component;

import br.com.project.util.all.BeanViewAbstract;

@Component/*Spring controla a injeção de dependencia*/
public abstract class BeanReportView extends BeanViewAbstract {
	
	/**
	 * Essa classe vai ser responsavel por trabalhar com ReportUtils, vai receber os dados e passar para o nosso ReportUtils de uma 
	 * forma mais facil de uma forma mais controlada e tbm vai fazer algum trabalho por nós.
	 */
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Porque estamos colocando esses metodos como protegidos, esses atributos, essas variaveis?
	 * 
	 *  Pq quem apenas extender da classe BeanReportView vai poder ter acesso.
	 */
	protected StreamedContent arquivoResport;/*É o responsavel por retornar o arquivo pronto para web para nós*/
	protected int tipoRelatorio;
	protected List<?> listDataBeanCollectionReport;/*Uma lista generica de dados. Ex: uma lista de dados de objetos de bairros, cidade, pessoas, etc...*/
	protected HashMap<Object, Object> parametrosRelatorio;/*HashMap-> Que a chave pode ser qualquer coisa, e o valor pode ser qualquer coisa*. E serão nossos parametrosRelatorio*/
	protected String nomeRelatorioJasper = "default";/*Temos um nome padrão "default", pq nome padrão se acasso venha errar o nome do relatório ou então esquecer de colocar o arquivo do relatório no projeto, ele pega um arquivo padrão que vai criar, chamando ele de "default".*/
	protected String nomeRelatorioSaida = "default";/*Temos um nome padrão "default", pq nome padrão se acasso venha errar o nome do relatório ou então esquecer de colocar o arquivo do relatório no projeto, ele pega um arquivo padrão que vai criar, chamando ele de "default".*/
	
	@Resource/*Para injeção de dependencia*/
	private ReportUtil reportUtil;/*Aqui faz a declaração do ReportUtil*/
	
	@SuppressWarnings("rawtypes")
	public BeanReportView() {/*Inicializa um construtor default*/
		parametrosRelatorio = new HashMap<Object, Object>();/*Inicia a lista de paramentros*/
		listDataBeanCollectionReport = new ArrayList();/*Inicia a lista de coleção de dados*/
	}
	
	/**
	 * O getReportUtil e setReportUtil é para injeção de dependencia
	 */
	public ReportUtil getReportUtil() {/*Temos que ter um retorno para ReportUtil*/
		return reportUtil;
	}
	
	public void setReportUtil(ReportUtil reportUtil) {
		this.reportUtil = reportUtil;
	}
	
	/**
	 * Metodo responsavel por retornar o arquivo para Web
	 */
	public StreamedContent getArquivoReport() throws Exception {/*Quando chamar qualquer um dos metodos Get listado abaixo, ele ja vai fazer tudo para nós, invocando nosso criador de relatório(classe ReportUtil), vai fazer tudo que definimos, retornando o relatório para nós. Facilitando nosso trabalho mais ainda*/
		return getReportUtil().geraRelatorio(getListDataBeanCollectionReport(), 
				getParametrosRelatorio(), getNomeRelatorioJasper(), 
				getNomeRelatorioSaida(), getTipoRelatorio());
	}

	public int getTipoRelatorio() {
		return tipoRelatorio;
	}

	public void setTipoRelatorio(int tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
	}

	public List<?> getListDataBeanCollectionReport() {
		return listDataBeanCollectionReport;
	}

	public void setListDataBeanCollectionReport(List<?> listDataBeanCollectionReport) {
		this.listDataBeanCollectionReport = listDataBeanCollectionReport;
	}

	public HashMap<Object, Object> getParametrosRelatorio() {
		return parametrosRelatorio;
	}

	public void setParametrosRelatorio(HashMap<Object, Object> parametrosRelatorio) {
		this.parametrosRelatorio = parametrosRelatorio;
	}

	public String getNomeRelatorioJasper() {
		return nomeRelatorioJasper;
	}

	public void setNomeRelatorioJasper(String nomeRelatorioJasper) {/*Se errar no nome do relatórios Jasper tem a verificação*/
		
		if (nomeRelatorioJasper == null || nomeRelatorioJasper.isEmpty()) {/* Se o nomeRelatorioJasper for igual null ou(||) nomeRelatorioJasper for vazio.*/
			nomeRelatorioJasper = "default";/*Definimos que o nomeRelatorioJasper igual a "default", pegando o arquivo padrão e sempre gerar o relatório*/
		}
		
		this.nomeRelatorioJasper = nomeRelatorioJasper;
	}

	public String getNomeRelatorioSaida() {
		return nomeRelatorioSaida;
	}

	public void setNomeRelatorioSaida(String nomeRelatorioSaida) {
		
		if (nomeRelatorioSaida == null || nomeRelatorioSaida.isEmpty()) {/* Se o nomeRelatorioSaida for igual null ou(||) nomeRelatorioSaida for vazio.*/
			nomeRelatorioSaida = "default";/*Definimos que o nomeRelatorioSaida igual a "default", pegando o arquivo padrão e sempre gerar o relatório*/
		}
		
		this.nomeRelatorioSaida = nomeRelatorioSaida;
	}

}
