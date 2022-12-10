package br.com.project.model.classes;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

@SuppressWarnings("deprecation")
@Audited /*Como vai ter auditoria. Essa anotação vai fazer que seja criado no BD umatabela de auditoria para cada tabela original*/
@Entity /* Tabela no banco de dados */
@Table(name = "entidade")
@SequenceGenerator(name = "entidade_seq", sequenceName = "entidade_seq", initialValue = 1, allocationSize = 1)
public class Entidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private Long ent_codigo;

	private String ent_login = null;

	private String ent_senha;
	
	private boolean ent_inativo = false;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date ent_ultimoacesso;
	
	public void setEnt_ultimoacesso(Date ent_ultimoacesso) {
		this.ent_ultimoacesso = ent_ultimoacesso;
	}
	
	public Date getEnt_ultimoacesso() {
		return ent_ultimoacesso;
	}
	
	public void setEnt_inativo(boolean ent_inativo) {
		this.ent_inativo = ent_inativo;
	}
	
	public boolean getEnt_inativo() {
		return ent_inativo;
	}

	public String getEnt_login() {
		return ent_login;
	}

	public void setEnt_login(String ent_login) {
		this.ent_login = ent_login;
	}

	public String getEn_senha() {
		return ent_senha;
	}

	public void setEn_senha(String en_senha) {
		this.ent_senha = en_senha;
	}
	
	public void setEnt_codigo(Long ent_codigo) {
		this.ent_codigo = ent_codigo;
	}
	
	public Long getEnt_codigo() {
		return ent_codigo;
	}
}
