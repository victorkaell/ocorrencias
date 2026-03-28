package prototipo.ocorrencias.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import prototipo.ocorrencias.models.Tratamento;
import prototipo.ocorrencias.repositories.TratamentoRepository;

@RestController
@RequestMapping("/api/tratamentos")
public class TratamentosRestController {
	
	@Autowired
	private TratamentoRepository tratamentoRepository;
	
	@GetMapping
	public List<Tratamento> listarTodosOsTratamentos() {
		return tratamentoRepository.findAll();
	}
	
	@PostMapping
	public Tratamento criarNovoTratamento(@Valid @RequestBody Tratamento novoTratamento) {
		return tratamentoRepository.save(novoTratamento);
	}
	
	@PutMapping("/{id}")
    public Tratamento atualizarTratamento(@PathVariable Long id, @Valid @RequestBody Tratamento tratamentoAtualizado) {
        return tratamentoRepository.findById(id)
            .map(tratamento -> {
                tratamento.setMetodo(tratamentoAtualizado.getMetodo());
                return tratamentoRepository.save(tratamento);
            }).orElseThrow(() -> new RuntimeException("Tratamento não encontrado com o id " + id));
    }

    @DeleteMapping("/{id}")
    public void apagarTratamento(@PathVariable Long id) {
        tratamentoRepository.deleteById(id);
    }
}
