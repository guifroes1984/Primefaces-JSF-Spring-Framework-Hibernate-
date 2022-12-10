package br.com.project.model.classes;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.envers.Audited;

import br.com.project.annotation.IdentificaCampoPesquisa;

@Audited
@Entity
@Table(name = "pais")
@SequenceGenerator(name = "pais_seq", sequenceName = "pais_seq", initialValue = 1, allocationSize = 1)
public class Pais implements Serializable {

	private static final long serialVersionUID = 1L;

	@IdentificaCampoPesquisa(descricaoCampo = "Código", campoConsulta = "pai_id")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pais_seq")
	private Long pai_idLong;

	@IdentificaCampoPesquisa(descricaoCampo = "Nome", campoConsulta = "pai_nome", principal = 1)
	@Column(nullable = false, length = 80)
	private String pai_nome;

	@Column(nullable = true, length = 15)
	private String pai_sigla;

	@Version
	@Column(name = "versionNum")
	private int versionNum;

	public Long getPai_idLong() {
		return pai_idLong;
	}

	public void setPai_idLong(Long pai_idLong) {
		this.pai_idLong = pai_idLong;
	}

	public String getPai_nome() {
		return pai_nome;
	}

	public void setPai_nome(String pai_nome) {
		this.pai_nome = pai_nome;
	}

	public String getPai_sigla() {
		return pai_sigla;
	}

	public void setPai_sigla(String pai_sigla) {
		this.pai_sigla = pai_sigla;
	}

	public int getVersionNum() {
		return versionNum;
	}

	public void setVersionNum(int versionNum) {
		this.versionNum = versionNum;
	}

	@Override
	public int hashCode() {
		return Objects.hash(pai_idLong);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pais other = (Pais) obj;
		return Objects.equals(pai_idLong, other.pai_idLong);
	}

}
