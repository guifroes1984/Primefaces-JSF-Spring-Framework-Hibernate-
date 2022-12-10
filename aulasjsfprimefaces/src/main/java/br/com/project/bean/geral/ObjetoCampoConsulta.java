package br.com.project.bean.geral;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

/**
 * Temos o objeto que vai carregar os dados da anota��o at� chegar na tela, mais j� temos o objeto definido para saber como trazer os dados 
 * para a tela
 * @author Guilherme
 *
 */
public class ObjetoCampoConsulta implements Serializable, Comparator<ObjetoCampoConsulta> {/*Comparator=> � usado para ordenar lista, passando o objeto que eu quero que ele trabalhe para mim*/

	private static final long serialVersionUID = 1L;

	private String descricao;/*Vai pegar da anota��o*/
	private String campoBanco;/*vai receber da anota��o*/
	private Object tipoClass;/*Tipo da classe, see � uma classe de cidade, de estado, de venda de produto*/
	private Integer principal;/*� o numero para saber se elee � o principal, se � para mostrar por primeiro ou n�o*/

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
	 * Faz apenas pelo campoBanco, a classe pode ser igual, posso ter um objeto com o mesmo c�digo, a mesma descri��o tamb�m, e o unico que 
	 * n�o pode se repetir � o campo do banco.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(campoBanco);
	}
	
	/**
	 * Para diferenciar os objetos para n�o ter objetos iguais
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
	 * Vai mostrar a descri��o do objeto na tela, se n�o especificar uma. Sen�o est� especificando qual propriedade t� a descri��o do objeto, 
	 * sempre chama o ToString
	 */
	@Override
	public String toString() {
		return getDescricao();
	}
	
	/**
	 * Implementado o metodo comparetor, � onde ele vai comparar. Quando tivermos uma lista com o objeto e mandar ordenar a lista, 
	 * os metodos do Comparator que estamos implementado da interface comparator est�o sendo executados, e v�o comparar qual o objeto 
	 * principal que tenha o c�digo 1, ou 2 ou 3 por exemplo, e vai ordenar na lista.
	 */
	@Override
	public int compare(ObjetoCampoConsulta o1, ObjetoCampoConsulta o2) {
		return ((ObjetoCampoConsulta)o1).getPrincipal().compareTo(((ObjetoCampoConsulta)o2).getPrincipal());/*Tem que ser feito um cash no objeto o1, e no objeto o2.*/
	}

}
