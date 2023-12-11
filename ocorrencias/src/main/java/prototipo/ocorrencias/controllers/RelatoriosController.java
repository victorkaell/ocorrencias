package prototipo.ocorrencias.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import prototipo.ocorrencias.models.Usuario;

@Controller
@SessionAttributes("usuario")
@RequestMapping("/relatorios")
public class RelatoriosController {
	
	@GetMapping("/menu")
	public String menuRelatorios(HttpServletRequest request, Usuario usuario, RedirectAttributes attributes) {
		if (usuario.getMatricula() == null) {
			attributes.addFlashAttribute("noperm", "Você não tem permissão para isso.");
			
			return "redirect:/login";
		}
		
		return "relatorios/menu";
	}
}
