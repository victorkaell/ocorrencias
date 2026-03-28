package prototipo.ocorrencias.controllers;

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
import prototipo.ocorrencias.models.Categoria;
import prototipo.ocorrencias.repositories.CategoriaRepository;

@RestController
@RequestMapping("/api/categorias")
public class CategoriasRestController {
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@GetMapping
	public List<Categoria> listarTodasAsCategorias() {
		return categoriaRepository.findAll();
	}
	
	@PostMapping
	public Categoria criarNovaCategoria(@Valid @RequestBody Categoria novaCategoria) {
		return categoriaRepository.save(novaCategoria);
	}
	
	@PutMapping("/{id}")
    public Categoria atualizarCategoria(@PathVariable Long id, @Valid @RequestBody Categoria categoriaAtualizada) {
        return categoriaRepository.findById(id)
            .map(categoria -> {
                categoria.setNome(categoriaAtualizada.getNome());
                return categoriaRepository.save(categoria);
            }).orElseThrow(() -> new RuntimeException("Categoria não encontrada com o id " + id));
    }

    @DeleteMapping("/{id}")
    public void apagarCategoria(@PathVariable Long id) {
        categoriaRepository.deleteById(id);
    }
}
