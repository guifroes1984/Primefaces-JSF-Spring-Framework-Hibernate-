package br.com.project.report.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.stereotype.Component;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.util.JRLoader;

@Component/*Pq estamos trabalhando com Spring*/
public class ReportUtil implements Serializable {/*Classe responsavel por gerar os relat�rios*/

	private static final long serialVersionUID = 1L;
	
	/**
	 * Variaveis pr� definidas
	 */
	private static final String UNDERLAINE = "_";/*O UNDERLAINE vai ser usado em varios pontos do c�digo*/
	private static final String FOLDER_RELATORIOS = "/relatorios";/*Essa pasta vai ser onde vou colocar os arquivos do IReport(do relat�rio)*/
	private static final String SUBREPORT_DIR = "SUBREPORT_DIR";/*� uma variavel responsavel por carregar o caminho do sub-relat�rios. � usado para imprimir uma lista, dentro de outro relat�rio*/
	private static final String EXTENSION_ODS = "ods";/*Relat�rios na seguntes extens�es*/
	private static final String EXTENSION_XLS = "xls";/*Relat�rios na seguntes extens�es*/
	private static final String EXTENSION_HTML = "html";/*Relat�rios na seguntes extens�es*/
	private static final String EXTENSION_PDF = "pdf";/*Relat�rios na seguntes extens�es*/
	private String SEPARATOR = File.separator;/*M�todo de separa a barra*/
	private final int RELATORIO_PDF = 1;/*Uma vari�vel para dizer o tipo do rela�rio, e sua extens�o no caso PDF*/
	private final int RELATORIO_EXCEL = 2;/*Uma vari�vel para dizer o tipo do rela�rio, e sua extens�o  no caso EXCEL*/
	private final int RELATORIO_HTML = 3;/*Uma vari�vel para dizer o tipo do rela�rio, e sua extens�o  no caso HTML*/
	private final int RELATORIO_PLANILHA_OPEN_OFFICE = 4;/*Uma vari�vel para dizer o tipo do rela�rio*/
	private static final String PONTO = ".";/*Segue igual a do UNDERLAINE, se quiser trocar a descri��o, pode ser torca a qualquer momento sem impacto no sistema*/
	private StreamedContent arquivoRetorno = null;/*Vari�vel que vai retornar para web*/
	private String caminhoArquivoRelatorio = null;/*Vai carregar o caminho do relat�rio para n�s*/
	private JRExporter tipoArquivoExportado = null;/*E o formul�rio de relat�rios*/
	private String extensaoArquivoExportado = "";/*Uma variavel que vai ser a extens�o do arquivo exportado*/
	private String caminhoSubreport_dir = "";
	private File arquivoGerado = null;/*Variavel que vai ser a do arquivo gerado*/
	
	
	/**
	 * Metodo responsavel por fazer TODA a gera��o do relat�rio
	 */
	public StreamedContent geraRelatorio(List<?>listDataBeanColletionReport, 
			HashMap parametrosRelatorio, String nomeRelatorioJasper, 
			String nomeRelatorioSaida, int tipoRelatorio) throws Exception {
		
		/*Cria a lista de collectionDataSource de beans que carregam os dados para o relat�rio*/
		JRBeanCollectionDataSource jrbcds = new JRBeanCollectionDataSource(listDataBeanColletionReport);
		
		/**
		 * Fornece o caminho fisico at� a pasta que conte�m os relat�rios compilados .jasper
		 */
		FacesContext context = FacesContext.getCurrentInstance();/*Procura o caminho da aplica��o*/
		context.responseComplete();/*Para dizer que nossa resposta vai ser completada*/
		ServletContext scontext = (ServletContext) context.getExternalContext().getContext();/*Pegar o caminho do relat�rio e da aplica��o*/
		
		String caminhoRelatorio = scontext.getRealPath(FOLDER_RELATORIOS);/*Pega o caminho do relat�rio, passando FOLDER_RELATORIOS, aquela variavel que passamos l� em cima. Temos o caminho da pasta de relat�rios.*/
		
		
		/**
		 * O que esta linha est� fazendo:
		 * 
		 * Temos que carregar o arquivo tbm. Passando o caminho do relat�rio, mais o SEPARADOR, mais O nomeRelatorioJasper que foi passado 
		 * como parametro, mais o PONTO, mais a extens�o "Jasper". A nossa aplica��o est� montando todo o caminho completo dentro do servidor, 
		 * at� a pasta(pacote) de relat�rios. 
		 * Ex: "C:/aplica��o/relatorio/rel_bairro.jasper"
		 */
		File file = new File(caminhoRelatorio + SEPARATOR + nomeRelatorioJasper + PONTO + "jasper");
		
		/**
		 * Essa � condi��o:
		 * 
		 * Se o caminho for igual null, ou(||) nosso caminhoRelatorio � diferente de null, s� que vazio, ou nosso file n�o existir. 
		 * 
		 */
		if (caminhoRelatorio == null || (caminhoRelatorio != null && caminhoRelatorio.isEmpty()) || !file.exists()) {
			
			caminhoRelatorio = this.getClass().getResource(FOLDER_RELATORIOS).getPath();/*Retorna o caminho rodando dentro do servidor, fora do eclipse*/
			SEPARATOR = "";/*Nessa caso o SEPARATOR vai ser vazio*/
		}
		
		/*Caminho da imagem*/
		parametrosRelatorio.put("REPORT_PARAMETERS_IMG", caminhoRelatorio);/*Dentro do relat�rio, cria um parametro chamado REPORT_PARAMETERS_IMG, se tiver esse paramentro dentro do relat�rio conseguimos captar uma imagem*/
		
		/*Caminho completo at� o relat�rio compliado indicado*/
		String caminhoArquivoJasper = caminhoRelatorio + SEPARATOR + nomeRelatorioJasper + PONTO + "Jasper";
		
		/*Faz o carregamento do relat�rio indicado*/
		JasperReport relatorioJasper = (JasperReport) JRLoader.loadObjectFromFile(caminhoArquivoJasper);/*Vai carregar o objeto do relat�rio Jasper e esse objeto vai ser criado a partir do nosso arquivo que encotramos na vari�vel e gerou o relat�rio*/
		
		/*Seta parametro SUBREPORT_DIR como caminho fisico para sub-reports*/
		caminhoSubreport_dir = caminhoRelatorio + SEPARATOR;/*Ent�o vai ser igual ao caminho do relat�rio, mais o separador que vai ser a barra(/)*/
		parametrosRelatorio.put(SUBREPORT_DIR, caminhoSubreport_dir);/*Aqui � o nosso parametrosRelatorios, vai ser o SUBREPORT_DIR, vamos passar o parametro caminhoSubreport_dir, que dai ele vai saber carregar o Sub-relatorio*/
		
		/*Carregar o arquivo complilado para a mem�ria*/
		JasperPrint impressoraJasper = JasperFillManager.fillReport(relatorioJasper, parametrosRelatorio, jrbcds);/*Pegar o JasperPrint que � usado para imprimir o relat�rio, quando fala imprimir � criar o relat�rio(fazer a montagem), passando relatorioJasper, os parametrosRelatorio e a lista de objetos "jrbcds". Ai faz a montagem.*/
		
		/*Definindo qual o formato vai ser exportado*/
		switch (tipoRelatorio) {/*Recebe o tipo do relat�rio*/
		case RELATORIO_PDF:
			tipoArquivoExportado = new JRPdfExporter();/*Criando um objeto de exporta��o*/
			extensaoArquivoExportado = EXTENSION_PDF;/*Colocando a extens�o do arquivo, vai ser igual a EXTENSION_PDF*/
			break;
			
		case RELATORIO_HTML:
			tipoArquivoExportado = new JRHtmlExporter();/*Criando um objeto de exporta��o*/
			extensaoArquivoExportado = EXTENSION_HTML;/*Colocando a extens�o do arquivo, vai ser igual a EXTENSION_HTML*/
			
		case RELATORIO_EXCEL:
			tipoArquivoExportado = new JRXlsExporter();/*Criando um objeto de exporta��o*/
			extensaoArquivoExportado = EXTENSION_XLS;/*Colocando a extens�o do arquivo, vai ser igual a EXTENSION_XLS*/
			
		case RELATORIO_PLANILHA_OPEN_OFFICE:
			tipoArquivoExportado = new JROdtExporter();/*Criando um objeto de exporta��o*/
			extensaoArquivoExportado = EXTENSION_ODS;/*Colocando a extens�o do arquivo, vai ser igual a EXTENSION_PDF*/

		default:
			tipoArquivoExportado = new JRPdfExporter();/*Criando um objeto de exporta��o. Por default sempre vai ser PDF*/
			extensaoArquivoExportado = EXTENSION_PDF;/*Colocando a extens�o do arquivo, vai ser igual a EXTENSION_PDF*/
			break;
		}
		
		nomeRelatorioSaida += UNDERLAINE + DateUtils.getDateAtualReportName();/*Montando o nosso nome do relat�rio de saida, concatenando passando o nome dele por paramentro, concatenando com UNDELINE mais usando o DateUtils que foi feito retornado a data concatenada junto underline, mais o nome do relat�rio que foi passado. Ex: report_bairro_02112022*/
		
		/*Caminho do relat�rio exportado*/
		caminhoRelatorio = caminhoRelatorio + SEPARATOR + PONTO + extensaoArquivoExportado;
		
		/*Criar um novo file exportado*/
		arquivoGerado = new File(caminhoRelatorio);/*Que � o PDF ou outra extens�o que foi gerado*/
		
		/*Preparar a impress�o*/
		tipoArquivoExportado.setParameter(JRExporterParameter.JASPER_PRINT, impressoraJasper);/*Vai ser o tipoArquivoExportado(JRExporter), setando os parametros pra ele que vai ser o JRExporterParameter.JASPER_PRINT, e passamos a impressoraJasper. Nossa impressoraJasper possui todos os dados como: parametros, o proprio relat�rio e a lista de dados*/
		
		/*Nome do arquivo fisico a ser impresso/exportado*/
		tipoArquivoExportado.setParameter(JRExporterParameter.OUTPUT_FILE, arquivoGerado);
		
		/*Executa a exporta��o*/
		tipoArquivoExportado.exportReport();
		
		/*Remove o arquivo do servidor ap�s ser feito o download pelo usu�rio*/
		arquivoGerado.deleteOnExit();
		
		/*Criar um inputStream* para ser usado pelo PrimeFaces*/
		InputStream conteudoRelatorio = new FileInputStream(arquivoGerado);
		
		/*Faz o retorno para a aplica��o*/
		arquivoRetorno = new DefaultStreamedContent(conteudoRelatorio, "application/" + extensaoArquivoExportado, nomeRelatorioSaida + PONTO + extensaoArquivoExportado);
		
		/*Retorno para a aplica��o do arquivo que foi gerado*/
		return arquivoRetorno;
	}
	
}
