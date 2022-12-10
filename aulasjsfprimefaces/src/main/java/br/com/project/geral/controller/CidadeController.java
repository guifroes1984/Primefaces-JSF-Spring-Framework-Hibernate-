package br.com.project.geral.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.framework.implementacao.crud.ImplementacaoCrud;
import br.com.framework.interfac.crud.InterfaceCrud;
import br.com.project.model.classes.Cidade;
import br.com.repository.interfaces.RepositoryCidade;
import br.com.srv.interfaces.SrvCidade;

@Controller
public class CidadeController extends ImplementacaoCrud<Cidade> implements InterfaceCrud<Cidade> {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private SrvCidade cidade;
	
	@Autowired
	private RepositoryCidade repositoryCidade;

}
