package prototipo.ocorrencias.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import prototipo.ocorrencias.models.Usuario;
import prototipo.ocorrencias.repositories.UsuarioRepository;

@Controller
@RequestMapping("/usuarios")
@SessionAttributes("usuario")
public class UsuariosController {
	
	@Autowired
	private UsuarioRepository ur;
	
	@GetMapping("/menu")
	public String menuUsuarios(Usuario usuario, RedirectAttributes attributes) {
		if (usuario.getMatricula() == null) {
			attributes.addFlashAttribute("noperm", "Você não tem permissão para isso.");
			
			return "redirect:/login";
		}
		
		return "usuarios/menu";
	}
	
	@GetMapping("/cadastrar_usuario")
	public String formUsuario(Usuario usuario, RedirectAttributes attributes) {
		if (usuario.getMatricula() == null) {
			attributes.addFlashAttribute("noperm", "Você não tem permissão para isso.");
			
			return "redirect:/login";
		}
		
		return "usuarios/form";
	}
	
	@PostMapping("/cadastrar_usuario")
	public String cadastrarUsuario(@ModelAttribute("usuarioForm") Usuario usuarioForm, RedirectAttributes attributes) {
		List<Usuario> usuarios = ur.findAll();
		
		for (int i = 0; i < usuarios.size(); i++) {
			Usuario usuarioLista = usuarios.get(i);
			
			if (usuarioLista.getMatricula().equals(usuarioForm.getMatricula())) {
				attributes.addFlashAttribute("error", "Esta matrícula já foi cadastrada.");
				
				return "redirect:/usuarios/cadastrar_usuario";
			}
		}
		System.out.println("Usuário cadastrado:" + usuarioForm);
		
		ur.save(usuarioForm);
		
		return "redirect:/usuarios/menu";
	}
	
	@GetMapping("/listar_usuarios")
	public ModelAndView listarUsuarios(Usuario usuario, RedirectAttributes attributes) {
		ModelAndView mv = new ModelAndView();
		List<Usuario> usuarios = ur.findAll();
		
		if (usuario.getMatricula() == null) {
			attributes.addFlashAttribute("noperm", "Você não tem permissão para isso.");
			mv.setViewName("redirect:/login");
			
			return mv;
		}
		
		if (usuarios.isEmpty()) {
			mv.setViewName("redirect:/ocorrencias/menu");
			attributes.addFlashAttribute("error", "Não existem usuários cadastrados."); // Tecnicamente impossível de ocorrer
			
			return mv;
		}
		
		mv.setViewName("usuarios/list");
		mv.addObject("usuarios", usuarios);
		
		return mv;
	}
	
	@GetMapping("/listar_usuarios/{id}/excluir")
	public ModelAndView confirmarUsuario(@PathVariable Long id, Usuario usuario, RedirectAttributes attributes) {
		ModelAndView mv = new ModelAndView();
		Optional<Usuario> opt = ur.findById(id);
		
		if (opt.isEmpty()) {
			mv.setViewName("redirect:/home");
			
			return mv;
		}
		
		if (usuario.getMatricula() == null) {
			mv.setViewName("redirect:/login");
			
			attributes.addFlashAttribute("noperm", "Você não tem permissão para isso.");
			
			return mv;
		}
		
		Usuario usuarioLista = opt.get();
		
		mv.setViewName("usuarios/exclusao");
		mv.addObject("usuarioLista", usuarioLista);
		
		return mv;
	}
	
	@GetMapping("/listar_usuarios/{id}/excluir/confirmar")
	public String excluirUsuario(@PathVariable Long id, Usuario usuario, RedirectAttributes attributes) {
		Optional<Usuario> opt = ur.findById(id);
		
		if (opt.isEmpty()) {
			return "redirect:/home";
		}
		
		if (usuario.getMatricula() == null) {
			attributes.addFlashAttribute("noperm", "Você não tem permissão para isso.");
			
			return "redirect:/login";
		}
			
		Usuario usuarioLista = opt.get();
		
		if (usuarioLista.getMatricula().equals(usuario.getMatricula())) {
			attributes.addFlashAttribute("error", "Você não pode excluir seu próprio usuário.");
			
			return "redirect:/usuarios/listar_usuarios";
		}
		
		ur.delete(usuarioLista);
		
		return "redirect:/usuarios/listar_usuarios";
	}
	
}
