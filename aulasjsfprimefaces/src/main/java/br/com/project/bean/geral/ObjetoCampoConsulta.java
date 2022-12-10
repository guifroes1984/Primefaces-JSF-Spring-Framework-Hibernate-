package br.com.project.bean.geral;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

/**
 * Temos o objeto que vai carregar os dados da anotação até chegar na tela, mais já temos o objeto definido para saber como trazer os dados 
 * para a tela
 * @author Guilherme
 *
 */
public class ObjetoCampoConsulta implements Serializable, Comparator<ObjetoCampoConsulta> {/*Comparator=> É usado para ordenar lista, passando o objeto que eu quero que ele trabalhe para mim*/

	private static final long serialVersionUID = 1L;

	private String descricao;/*Vai pegar da anotação*/
	private String campoBanco;/*vai receber da anotação*/
	private Object tipoClass;/*Tipo da classe, see é uma classe de cidade, de estado, de venda de produto*/
	private Integer principal;/*É o numero para saber se elee é o principal, se é para mostrar por primeiro ou não*/

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getCampoBanco() {
		return campoBanco;
	}

	public void setCampoBanco(String campoBanco) {
		this.campoBanco = campoBanco;
	}

	public Object getTipoClass() {
		return tipoClass;
	}

	public void setTipoClass(Object tipoClass) {
		this.tipoClass = tipoClass;
	}

	public Integer getPrincipal() {
		return principal;
	}

	public void setPrincipal(Integer principal) {
		this.principal = principal;
	}
	
	/**
	 * Faz apenas pelo campoBanco, a classe pode ser igual, posso ter um objeto com o mesmo código, a mesma descrição também, e o unico que 
	 * não pode se repetir é o campo do banco.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(campoBanco);
	}
	
	/**
	 * Para diferenciar os objetos para não ter objetos iguais
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ObjetoCampoConsulta other = (ObjetoCampoConsulta) obj;
		return Objects.equals(campoBanco, other.campoBanco);
	}
	
	/**
	 * Vai mostrar a descrição do objeto na tela, se não especificar uma. Senão está especificando qual propriedade tá a descrição do objeto, 
	 * sempre chama o ToString
	 */
	@Override
	public String toString() {
		return getDescricao();
	}
	
	/**
	 * Implementado o metodo comparetor, é onde ele vai comparar. Quando tivermos uma lista com o objeto e mandar ordenar a lista, 
	 * os metodos do Comparator que estamos implementado da interface comparator estão sendo executados, e vão comparar qual o objeto 
	 * principal que tenha o código 1, ou 2 ou 3 por exemplo, e vai ordenar na lista.
	 */
	@Override
	public int compare(ObjetoCampoConsulta o1, ObjetoCampoConsulta o2) {
		return ((ObjetoCampoConsulta)o1).getPrincipal().compareTo(((ObjetoCampoConsulta)o2).getPrincipal());/*Tem que ser feito um cash no objeto o1, e no objeto o2.*/
	}

}
