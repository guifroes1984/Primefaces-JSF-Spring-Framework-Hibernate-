package br.com.project.util.all;

public enum EstatusPersistencia {/**/

	ERRO("Erro"), SUCESSO("Sucesso"),
	OBJETO_REFERENCIADO("Esse objeto n?o pode ser apagadopor posuir refer?ncias ao mesmo.");

	private String name;

	private EstatusPersistencia(String s) {
		this.name = s;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.name;
	}

}
