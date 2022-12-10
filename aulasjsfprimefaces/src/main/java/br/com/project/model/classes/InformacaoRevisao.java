package br.com.project.model.classes;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

import br.com.project.listener.CustomListener;

@Entity/*Tabela no banco de dados*/
@Table(name = "revinfo")/*Nome da tabela de revisão*/
@RevisionEntity(CustomListener.class)/*Qual o Listener responsavel por trabalahr com ele*/
public class InformacaoRevisao extends DefaultRevisionEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne/*Muitos registros para uma Entidade*/
	@ForeignKey(name = "entidade_fk")/**/
	@JoinColumn(nullable = false, name = "entidade")/*Para Unir(@JoinColumn). Não pode ser nula, e o nome da entidade*/
	private Entidade entidade;/*Chave estrangeira para o nosso usuário, para saber quem fez as mudanças*/
	
	public Entidade getEntidade() {
		return entidade;
	}
	
	public void setEntidade(Entidade entidade) {
		this.entidade = entidade;
	}
}
