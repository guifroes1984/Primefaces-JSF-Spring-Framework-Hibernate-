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
public class ReportUtil implements Serializable {/*Classe responsavel por gerar os relatórios*/

	private static final long serialVersionUID = 1L;
	
	/**
	 * Variaveis pré definidas
	 */
	private static final String UNDERLAINE = "_";/*O UNDERLAINE vai ser usado em varios pontos do código*/
	private static final String FOLDER_RELATORIOS = "/relatorios";/*Essa pasta vai ser onde vou colocar os arquivos do IReport(do relatório)*/
	private static final String SUBREPORT_DIR = "SUBREPORT_DIR";/*É uma variavel responsavel por carregar o caminho do sub-relatórios. É usado para imprimir uma lista, dentro de outro relatório*/
	private static final String EXTENSION_ODS = "ods";/*Relatórios na seguntes extensões*/
	private static final String EXTENSION_XLS = "xls";/*Relatórios na seguntes extensões*/
	private static final String EXTENSION_HTML = "html";/*Relatórios na seguntes extensões*/
	private static final String EXTENSION_PDF = "pdf";/*Relatórios na seguntes extensões*/
	private String SEPARATOR = File.separator;/*Método de separa a barra*/
	private final int RELATORIO_PDF = 1;/*Uma variável para dizer o tipo do relaório, e sua extensão no caso PDF*/
	private final int RELATORIO_EXCEL = 2;/*Uma variável para dizer o tipo do relaório, e sua extensão  no caso EXCEL*/
	private final int RELATORIO_HTML = 3;/*Uma variável para dizer o tipo do relaório, e sua extensão  no caso HTML*/
	private final int RELATORIO_PLANILHA_OPEN_OFFICE = 4;/*Uma variável para dizer o tipo do relaório*/
	private static final String PONTO = ".";/*Segue igual a do UNDERLAINE, se quiser trocar a descrição, pode ser torca a qualquer momento sem impacto no sistema*/
	private StreamedContent arquivoRetorno = null;/*Variável que vai retornar para web*/
	private String caminhoArquivoRelatorio = null;/*Vai carregar o caminho do relatório para nós*/
	private JRExporter tipoArquivoExportado = null;/*E o formulário de relatórios*/
	private String extensaoArquivoExportado = "";/*Uma variavel que vai ser a extensão do arquivo exportado*/
	private String caminhoSubreport_dir = "";
	private File arquivoGerado = null;/*Variavel que vai ser a do arquivo gerado*/
	
	
	/**
	 * Metodo responsavel por fazer TODA a geração do relatório
	 */
	public StreamedContent geraRelatorio(List<?>listDataBeanColletionReport, 
			HashMap parametrosRelatorio, String nomeRelatorioJasper, 
			String nomeRelatorioSaida, int tipoRelatorio) throws Exception {
		
		/*Cria a lista de collectionDataSource de beans que carregam os dados para o relatório*/
		JRBeanCollectionDataSource jrbcds = new JRBeanCollectionDataSource(listDataBeanColletionReport);
		
		/**
		 * Fornece o caminho fisico até a pasta que conte´m os relatórios compilados .jasper
		 */
		FacesContext context = FacesContext.getCurrentInstance();/*Procura o caminho da aplicação*/
		context.responseComplete();/*Para dizer que nossa resposta vai ser completada*/
		ServletContext scontext = (ServletContext) context.getExternalContext().getContext();/*Pegar o caminho do relatório e da aplicação*/
		
		String caminhoRelatorio = scontext.getRealPath(FOLDER_RELATORIOS);/*Pega o caminho do relatório, passando FOLDER_RELATORIOS, aquela variavel que passamos lá em cima. Temos o caminho da pasta de relatórios.*/
		
		
		/**
		 * O que esta linha está fazendo:
		 * 
		 * Temos que carregar o arquivo tbm. Passando o caminho do relatório, mais o SEPARADOR, mais O nomeRelatorioJasper que foi passado 
		 * como parametro, mais o PONTO, mais a extensão "Jasper". A nossa aplicação está montando todo o caminho completo dentro do servidor, 
		 * até a pasta(pacote) de relatórios. 
		 * Ex: "C:/aplicação/relatorio/rel_bairro.jasper"
		 */
		File file = new File(caminhoRelatorio + SEPARATOR + nomeRelatorioJasper + PONTO + "jasper");
		
		/**
		 * Essa é condição:
		 * 
		 * Se o caminho for igual null, ou(||) nosso caminhoRelatorio é diferente de null, só que vazio, ou nosso file não existir. 
		 * 
		 */
		if (caminhoRelatorio == null || (caminhoRelatorio != null && caminhoRelatorio.isEmpty()) || !file.exists()) {
			
			caminhoRelatorio = this.getClass().getResource(FOLDER_RELATORIOS).getPath();/*Retorna o caminho rodando dentro do servidor, fora do eclipse*/
			SEPARATOR = "";/*Nessa caso o SEPARATOR vai ser vazio*/
		}
		
		/*Caminho da imagem*/
		parametrosRelatorio.put("REPORT_PARAMETERS_IMG", caminhoRelatorio);/*Dentro do relatório, cria um parametro chamado REPORT_PARAMETERS_IMG, se tiver esse paramentro dentro do relatório conseguimos captar uma imagem*/
		
		/*Caminho completo até o relatório compliado indicado*/
		String caminhoArquivoJasper = caminhoRelatorio + SEPARATOR + nomeRelatorioJasper + PONTO + "Jasper";
		
		/*Faz o carregamento do relatório indicado*/
		JasperReport relatorioJasper = (JasperReport) JRLoader.loadObjectFromFile(caminhoArquivoJasper);/*Vai carregar o objeto do relatório Jasper e esse objeto vai ser criado a partir do nosso arquivo que encotramos na variável e gerou o relatório*/
		
		/*Seta parametro SUBREPORT_DIR como caminho fisico para sub-reports*/
		caminhoSubreport_dir = caminhoRelatorio + SEPARATOR;/*Então vai ser igual ao caminho do relatório, mais o separador que vai ser a barra(/)*/
		parametrosRelatorio.put(SUBREPORT_DIR, caminhoSubreport_dir);/*Aqui é o nosso parametrosRelatorios, vai ser o SUBREPORT_DIR, vamos passar o parametro caminhoSubreport_dir, que dai ele vai saber carregar o Sub-relatorio*/
		
		/*Carregar o arquivo complilado para a memória*/
		JasperPrint impressoraJasper = JasperFillManager.fillReport(relatorioJasper, parametrosRelatorio, jrbcds);/*Pegar o JasperPrint que é usado para imprimir o relatório, quando fala imprimir é criar o relatório(fazer a montagem), passando relatorioJasper, os parametrosRelatorio e a lista de objetos "jrbcds". Ai faz a montagem.*/
		
		/*Definindo qual o formato vai ser exportado*/
		switch (tipoRelatorio) {/*Recebe o tipo do relatório*/
		case RELATORIO_PDF:
			tipoArquivoExportado = new JRPdfExporter();/*Criando um objeto de exportação*/
			extensaoArquivoExportado = EXTENSION_PDF;/*Colocando a extensão do arquivo, vai ser igual a EXTENSION_PDF*/
			break;
			
		case RELATORIO_HTML:
			tipoArquivoExportado = new JRHtmlExporter();/*Criando um objeto de exportação*/
			extensaoArquivoExportado = EXTENSION_HTML;/*Colocando a extensão do arquivo, vai ser igual a EXTENSION_HTML*/
			
		case RELATORIO_EXCEL:
			tipoArquivoExportado = new JRXlsExporter();/*Criando um objeto de exportação*/
			extensaoArquivoExportado = EXTENSION_XLS;/*Colocando a extensão do arquivo, vai ser igual a EXTENSION_XLS*/
			
		case RELATORIO_PLANILHA_OPEN_OFFICE:
			tipoArquivoExportado = new JROdtExporter();/*Criando um objeto de exportação*/
			extensaoArquivoExportado = EXTENSION_ODS;/*Colocando a extensão do arquivo, vai ser igual a EXTENSION_PDF*/

		default:
			tipoArquivoExportado = new JRPdfExporter();/*Criando um objeto de exportação. Por default sempre vai ser PDF*/
			extensaoArquivoExportado = EXTENSION_PDF;/*Colocando a extensão do arquivo, vai ser igual a EXTENSION_PDF*/
			break;
		}
		
		nomeRelatorioSaida += UNDERLAINE + DateUtils.getDateAtualReportName();/*Montando o nosso nome do relatório de saida, concatenando passando o nome dele por paramentro, concatenando com UNDELINE mais usando o DateUtils que foi feito retornado a data concatenada junto underline, mais o nome do relatório que foi passado. Ex: report_bairro_02112022*/
		
		/*Caminho do relatório exportado*/
		caminhoRelatorio = caminhoRelatorio + SEPARATOR + PONTO + extensaoArquivoExportado;
		
		/*Criar um novo file exportado*/
		arquivoGerado = new File(caminhoRelatorio);/*Que é o PDF ou outra extensão que foi gerado*/
		
		/*Preparar a impressão*/
		tipoArquivoExportado.setParameter(JRExporterParameter.JASPER_PRINT, impressoraJasper);/*Vai ser o tipoArquivoExportado(JRExporter), setando os parametros pra ele que vai ser o JRExporterParameter.JASPER_PRINT, e passamos a impressoraJasper. Nossa impressoraJasper possui todos os dados como: parametros, o proprio relatório e a lista de dados*/
		
		/*Nome do arquivo fisico a ser impresso/exportado*/
		tipoArquivoExportado.setParameter(JRExporterParameter.OUTPUT_FILE, arquivoGerado);
		
		/*Executa a exportação*/
		tipoArquivoExportado.exportReport();
		
		/*Remove o arquivo do servidor após ser feito o download pelo usuário*/
		arquivoGerado.deleteOnExit();
		
		/*Criar um inputStream* para ser usado pelo PrimeFaces*/
		InputStream conteudoRelatorio = new FileInputStream(arquivoGerado);
		
		/*Faz o retorno para a aplicação*/
		arquivoRetorno = new DefaultStreamedContent(conteudoRelatorio, "application/" + extensaoArquivoExportado, nomeRelatorioSaida + PONTO + extensaoArquivoExportado);
		
		/*Retorno para a aplicação do arquivo que foi gerado*/
		return arquivoRetorno;
	}
	
}
