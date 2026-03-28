package prototipo.ocorrencias.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Usuario {
	
	@Id
	@NotNull(message = "A matrícula não pode estar nula.")
	private Long matricula;
	
	@NotBlank(message = "O campo senha não pode estar em branco.")
	@Size(min = 4, message = "A senha precisa ter no mínimo 4 caracteres.")
	@JsonProperty(access = Access.WRITE_ONLY)
	private String senha;

	public Long getMatricula() {
		return matricula;
	}

	public void setMatricula(Long matricula) {
		this.matricula = matricula;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	@Override
	public String toString() {
		return "Usuario [matricula=" + matricula + ", senha=" + senha + "]";
	}
}
