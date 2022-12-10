package br.com.project.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.FIELD)/**/
@Retention(value = RetentionPolicy.RUNTIME)/**/
@Documented/**/
public abstract @interface IdentificaCampoPesquisa {/*Definindo como abstract para poder reutilizar em outros lugares*/
	
	String descricaoCampo();/*Descrição do campo para a tela*/
	String campoConsulta();/*campo do banco de dados*/
	int principal() default 10000;/*Posição que irá aparecer no combo*/

}
