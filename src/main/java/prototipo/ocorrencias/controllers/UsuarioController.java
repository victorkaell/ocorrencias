package prototipo.ocorrencias.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import prototipo.ocorrencias.models.Usuario;
import prototipo.ocorrencias.repositories.UsuarioRepository;

@Controller
public class UsuarioController {
	
	@Autowired
	private UsuarioRepository ur; // SERÁ USADO
	
	@PostMapping("/login")
	public String verificarLogin(Usuario usuario) {
		List<Usuario> usuarios = ur.findAll();
		
		System.out.println(usuario);
		
		for (int i = 0; i < usuarios.size(); i++) {
			Usuario usuarioLista = usuarios.get(i);
			
			if (usuario.getMatricula().equals(usuarioLista.getMatricula()) && usuario.getSenha().equals(usuarioLista.getSenha())) {
				System.out.println("Aprovado!");
			} else {
				System.out.println("Recusado!");
			}
		}
		
		return "login"; // TEMPORÁRIO
	}
}
