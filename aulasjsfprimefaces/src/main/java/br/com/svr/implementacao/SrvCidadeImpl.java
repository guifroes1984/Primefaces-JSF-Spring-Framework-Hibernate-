package br.com.svr.implementacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.repository.interfaces.RepositoryCidade;
import br.com.srv.interfaces.SrvCidade;

@Service
public class SrvCidadeImpl implements SrvCidade {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private RepositoryCidade repositoryCidade;

}
