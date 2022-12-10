package br.com.repository.interfaces;

import java.io.Serializable;
import java.util.Date;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional /* � a parte que vai fazer as inser��es, update, delete no banco de dados, � parte mais proxima do banco de dados*/
@Repository
public interface RepositoryEntidade extends Serializable {

	Date getUltimoAcessoEntidadeLogada(String name);

	void updateUltimoAcessoUser(String login);

	boolean existeUsuario(String ent_login);

}
