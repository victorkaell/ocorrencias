package prototipo.ocorrencias.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Tratamento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "O gatilho não pode estar em branco.")
	@Size(min = 5, max = 500, message = "O gatilho precisa ter entre 5 e 500 caracteres.")
	private String metodo;

	@ManyToOne
	private Ocorrencia ocorrencia;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMetodo() {
		return metodo;
	}

	public void setMetodo(String metodo) {
		this.metodo = metodo;
	}

	public Ocorrencia getOcorrencia() {
		return ocorrencia;
	}

	public void setOcorrencia(Ocorrencia ocorrencia) {
		this.ocorrencia = ocorrencia;
	}

	@Override
	public String toString() {
		return "Tratamento [id=" + id + ", metodo=" + metodo + ", ocorrencia=" + ocorrencia + "]";
	}
}
