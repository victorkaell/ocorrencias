package prototipo.ocorrencias.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import prototipo.ocorrencias.models.TempUsuario;
import prototipo.ocorrencias.models.Usuario;
import prototipo.ocorrencias.repositories.UsuarioRepository;

@Controller
@SessionAttributes("usuario")
public class IndexController {
	
	@Autowired
	private UsuarioRepository ur;
	
	@GetMapping("/")
	public String index(Usuario usuario) {
		return "redirect:/login";
		
		// if (usuario.getMatricula() == null) {
		//	return "redirect:/login";
		//}
		
		//return "redirect:/home";
	}
	
	@GetMapping("/login") 
	public String login(SessionStatus status, Usuario usuario) {
		status.setComplete();
	
		return "login";
	}
	
	@PostMapping("/login")
	public String verificarLogin(Usuario usuario, Model model, RedirectAttributes attributes) {
		List<Usuario> usuarios = ur.findAll();
		
		System.out.println(usuario);
		
		for (int i = 0; i < usuarios.size(); i++) {
			Usuario usuarioLista = usuarios.get(i);
			
			if (usuario.getMatricula().equals(usuarioLista.getMatricula()) && usuario.getSenha().equals(usuarioLista.getSenha())) {
				model.addAttribute("usuario", usuario);
				
				return "redirect:/home";				
			}
		}
		attributes.addFlashAttribute("error", "Matrícula e/ou senha inválidos.");
		
		return "redirect:/login";
	}
	
	@GetMapping("/home")
	public String home(Usuario usuario) {
		if (usuario.getMatricula() == null) {
			return "redirect:/login";
		}
		
		return "home";
	}
	
	@GetMapping("/login/forgot")
	public String formSenha() {
		return "forgot";
	}
	
	@PostMapping("/login/forgot")
	public String redefinirSenha(TempUsuario tempUsuario, RedirectAttributes attributes) {
		List<Usuario> usuarios = ur.findAll();
		
		if (!(tempUsuario.getSenha().equals(tempUsuario.getConfirmacao()))) {
			attributes.addFlashAttribute("error", "As senhas não coincidem.");
			
			return "redirect:/login/forgot";
		}
		
		for (int i = 0; i < usuarios.size(); i++) {
			Usuario usuarioLista = usuarios.get(i);
			
			if (usuarioLista.getMatricula().equals(tempUsuario.getMatricula())) {
				usuarioLista.setSenha(tempUsuario.getSenha());
				
				System.out.println(tempUsuario);
				
				ur.save(usuarioLista);
				
				attributes.addFlashAttribute("sucess", "Senha redefinida com sucesso!");
				
				return "redirect:/login";
			}
		}
		
		attributes.addFlashAttribute("error", "Matrícula não encontrada.");
		
		return "redirect:/login/forgot";
	}
}
