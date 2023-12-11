package prototipo.ocorrencias.dtos;

public class UsuarioSenhaTemporariaDTO {

	private Long matricula;
	private String senha;
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
