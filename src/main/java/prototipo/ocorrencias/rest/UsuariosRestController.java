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
import prototipo.ocorrencias.models.Usuario;
import prototipo.ocorrencias.repositories.UsuarioRepository;

@RestController
@RequestMapping("/api/usuarios")
public class UsuariosRestController {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@GetMapping
	public List<Usuario> listarTodosOsUsuarios() {
		return usuarioRepository.findAll();
	}
	
	@PostMapping
	public Usuario criarNovoUsuario(@Valid @RequestBody Usuario novoUsuario) {
		return usuarioRepository.save(novoUsuario);
	}
	
	@PutMapping("/{id}")
    public Usuario atualizarOcorrencia(@PathVariable Long id, @Valid @RequestBody Usuario usuarioAtualizado) {
        return usuarioRepository.findById(id)
            .map(usuario -> {
                usuario.setSenha(usuarioAtualizado.getSenha());
                return usuarioRepository.save(usuario);
            }).orElseThrow(() -> new RuntimeException("Usuário não encontrado com a matrícula " + id));
    }

    @DeleteMapping("/{id}")
    public void apagarUsuario(@PathVariable Long id) {
        usuarioRepository.deleteById(id);
    }
}
