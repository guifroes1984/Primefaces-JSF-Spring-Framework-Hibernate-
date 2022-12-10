package br.com.repository.interfaces;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

@Repository/*Identifica que é uma interface de conexão com Banco de dados*/
public interface RepositoryLogin extends Serializable {
	
	boolean autentico(String login, String senha) throws Exception; 
		
	

}
