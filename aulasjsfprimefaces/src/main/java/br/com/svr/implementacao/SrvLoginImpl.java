package br.com.svr.implementacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.repository.interfaces.RepositoryLogin;
import br.com.srv.interfaces.SrvLogin;

@Service
public class SrvLoginImpl implements SrvLogin {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private RepositoryLogin repositoryLogin;/*A partir desse método temos acesso a toda a camada do DaoLogin*/

	@Override
	public boolean autentico(String login, String senha) throws Exception {
		return repositoryLogin.autentico(login, senha);
	}

}
