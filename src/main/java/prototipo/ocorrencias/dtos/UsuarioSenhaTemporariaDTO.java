package prototipo.ocorrencias.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UsuarioSenhaTemporariaDTO {
	
	@NotNull(message = "A matrícula não pode estar nula.")
	private Long matricula;
	
	@NotBlank(message = "A senha não pode estar em branco.")
	@Size(min = 4, message = "A senha deve ter no mínimo 4 caracteres.")
	private String senha;
	
	@NotBlank(message = "A senha não pode estar em branco.")
	@Size(min = 4, message = "A senha deve ter no mínimo 4 caracteres.")
	private String confirmacao;

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

	public String getConfirmacao() {
		return confirmacao;
	}

	public void setConfirmacao(String confirmacao) {
		this.confirmacao = confirmacao;
	}

	@Override
	public String toString() {
		return "TempUsuario [matricula=" + matricula + ", senha=" + senha + ", confirmacao=" + confirmacao + "]";
	}
}
