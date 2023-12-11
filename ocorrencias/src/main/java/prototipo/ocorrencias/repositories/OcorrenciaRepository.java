package prototipo.ocorrencias.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import prototipo.ocorrencias.models.Ocorrencia;

public interface OcorrenciaRepository extends JpaRepository<Ocorrencia, Long>{

}
