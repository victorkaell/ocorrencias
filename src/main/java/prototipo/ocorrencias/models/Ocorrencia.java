package prototipo.ocorrencias.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

@Entity
public class Ocorrencia {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "A categoria não pode estar em branco.")
	private String categoria;
	
	@NotNull(message = "A matrícula não pode estar nula.")
	private Long matricula;
	
	@NotBlank(message = "O campo nome não pode estar em branco.")
	@Size(min = 3, max = 50, message = "O nome precisa ter entre 3 e 50 caracteres.")
	private String nome;
	
	@NotNull(message = "A data não pode ser nula.")
	@PastOrPresent(message = "A data não pode estar no futuro.")
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private LocalDate data;
	
	@NotNull(message = "O horário não pode ser nulo.")
	@DateTimeFormat
	private LocalTime horario;
	
	@PastOrPresent(message = "O tempo não pode estar no futuro.")
	private LocalDateTime tempo;
	
	@NotBlank(message = "O gatilho não pode estar em branco.")
	@Size(min = 5, max = 500, message = "O gatilho precisa ter entre 5 e 500 caracteres.")
	private String gatilho;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public Long getMatricula() {
		return matricula;
	}

	public void setMatricula(Long matricula) {
		this.matricula = matricula;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public LocalTime getHorario() {
		return horario;
	}

	public void setHorario(LocalTime horario) {
		this.horario = horario;
	}

	public LocalDateTime getTempo() {
		return tempo;
	}

	public void setTempo(LocalDateTime tempo) {
		this.tempo = tempo;
	}

	public String getGatilho() {
		return gatilho;
	}

	public void setGatilho(String gatilho) {
		this.gatilho = gatilho;
	}

	@Override
	public String toString() {
		return "Ocorrencia [id=" + id + ", categoria=" + categoria + ", matricula=" + matricula + ", nome=" + nome
				+ ", data=" + data + ", horario=" + horario + ", tempo=" + tempo + ", gatilho=" + gatilho + "]";
	}
}
