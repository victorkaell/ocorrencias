package prototipo.ocorrencias.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import prototipo.ocorrencias.models.Ocorrencia;
import prototipo.ocorrencias.repositories.OcorrenciaRepository;

@RestController
@RequestMapping("/api/ocorrencias")
public class OcorrenciasRestController {
	
	@Autowired
	private OcorrenciaRepository ocorrenciaRepository;
	
	@GetMapping
	public List<Ocorrencia> listarTodasAsOcorrencias() {
		return ocorrenciaRepository.findAll();
	}
	
	@PostMapping
	public Ocorrencia criarNovaOcorrencia(@Valid @RequestBody Ocorrencia novaOcorrencia) {
		return ocorrenciaRepository.save(novaOcorrencia);
	}
	
	@PutMapping("/{id}")
    public Ocorrencia atualizarOcorrencia(@PathVariable Long id, @Valid @RequestBody Ocorrencia ocorrenciaAtualizada) {
        return ocorrenciaRepository.findById(id)
            .map(ocorrencia -> {
                ocorrencia.setMatricula(ocorrenciaAtualizada.getMatricula());
                ocorrencia.setNome(ocorrenciaAtualizada.getNome());
                ocorrencia.setData(ocorrenciaAtualizada.getData());
                ocorrencia.setHorario(ocorrenciaAtualizada.getHorario());
                ocorrencia.setTempo(ocorrenciaAtualizada.getTempo());
                ocorrencia.setCategoria(ocorrenciaAtualizada.getCategoria());
                ocorrencia.setGatilho(ocorrenciaAtualizada.getGatilho());
                return ocorrenciaRepository.save(ocorrencia);
            }).orElseThrow(() -> new RuntimeException("Ocorrência não encontrada com o id " + id));
    }

    @DeleteMapping("/{id}")
    public void apagarOcorrencia(@PathVariable Long id) {
        ocorrenciaRepository.deleteById(id);
    }
}
