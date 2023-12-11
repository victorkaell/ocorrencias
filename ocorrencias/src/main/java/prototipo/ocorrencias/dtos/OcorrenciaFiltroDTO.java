package prototipo.ocorrencias.dtos;

public class OcorrenciaFiltroDTO {

	private String filtro;
	private String categoria;

	public String getFiltro() {
		return filtro;
	}

	public void setFiltro(String filtro) {
		this.filtro = filtro;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	@Override
	public String toString() {
		return "Filtro [filtro=" + filtro + ", categoria=" + categoria + "]";
	}
}
