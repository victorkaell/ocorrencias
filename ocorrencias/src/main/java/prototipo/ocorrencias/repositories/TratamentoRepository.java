package prototipo.ocorrencias.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import prototipo.ocorrencias.models.Ocorrencia;
import prototipo.ocorrencias.models.Tratamento;

public interface TratamentoRepository extends JpaRepository<Tratamento, Long>{
	
	List<Tratamento> findByOcorrencia(Ocorrencia ocorrencia);
	
}
