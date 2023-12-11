package prototipo.ocorrencias.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import prototipo.ocorrencias.models.Categoria;
import prototipo.ocorrencias.models.Usuario;
import prototipo.ocorrencias.repositories.CategoriaRepository;

@Controller
@RequestMapping("/categorias")
@SessionAttributes("usuario")
public class CategoriasController {
	
	@Autowired
	private CategoriaRepository cr;
	
	@GetMapping("/menu")
	public String menuCategorias(Usuario usuario, RedirectAttributes attributes) {
		if (usuario.getMatricula() == null) {
			attributes.addFlashAttribute("noperm", "Você não tem permissão para isso.");
			
			return "redirect:/login";
		}
		
		return "categorias/menu";
	}
	
	@GetMapping("/adicionar_categoria")
	public String formCategoria(Usuario usuario, RedirectAttributes attributes) {
		if (usuario.getMatricula() == null) {
			attributes.addFlashAttribute("noperm", "Você não tem permissão para isso.");
			
			return "redirect:/login";
		}
		
		return "categorias/form";
	}
	
	@PostMapping("/adicionar_categoria")
	public String adicionarCategoria(Categoria categoria, RedirectAttributes attributes) {
		List<Categoria> categorias = cr.findAll();
		
		for (int i = 0; i < categorias.size(); i++) {
			Categoria categoriaLista = categorias.get(i);
			
			if (categoriaLista.getNome().equals(categoria.getNome())) {
				attributes.addFlashAttribute("error", "Esta categoria já existe.");
				
				return "redirect:/categorias/adicionar_categoria";
			}
		}
		
		cr.save(categoria);
		
		return "redirect:/categorias/menu";
	}
	
	@GetMapping("/listar_categorias")
	public ModelAndView listarCategorias(Usuario usuario, RedirectAttributes attributes) {
		ModelAndView mv = new ModelAndView();
		List<Categoria> categorias = cr.findAll();
		
		if (usuario.getMatricula() == null) {
			attributes.addFlashAttribute("noperm", "Você não tem permissão para isso.");
			mv.setViewName("redirect:/login");
			
			return mv;
		}
		
		if (categorias.isEmpty()) {
			mv.setViewName("redirect:/categorias/menu");
			attributes.addFlashAttribute("error", "Não existem categorias cadastradas.");
			
			return mv;
		}
		
		mv.setViewName("categorias/list");
		mv.addObject("categorias", categorias);
		
		return mv;
	}
	
	@GetMapping("/listar_categorias/{id}/mover_para_cima")
	public String moverParaCima(Usuario usuario, RedirectAttributes attributes, @PathVariable Long id) {
		Optional<Categoria> opt = cr.findById(id);
		List<Categoria> categorias = cr.findAll();
		
		if (usuario.getMatricula() == null) {
			attributes.addFlashAttribute("noperm", "Você não tem permissão para isso.");
			
			return "redirect:/login";
		}
		
		if (opt.isEmpty()) {
			return "redirect:/home";
		}
		
		Categoria categoria = opt.get();
		
		int iEscolhida = categorias.indexOf(categoria);
		System.out.println(iEscolhida);
		
		int iAcima = iEscolhida - 1;
		System.out.println(iAcima);
		
		if (!(iAcima < 0)) {
			Categoria categoriaAcima = categorias.get(iAcima);
			
			System.out.println(categorias);
			
			String temp = categoriaAcima.getNome();
			categoriaAcima.setNome(categoria.getNome());
			categoria.setNome(temp);
			
			System.out.println(categorias);
			cr.saveAll(categorias);
		}
		
		return "redirect:/categorias/listar_categorias";
	}
	
	@GetMapping("/listar_categorias/{id}/mover_para_baixo")
	public String moverParaBaixo(Usuario usuario, RedirectAttributes attributes, @PathVariable Long id) {
		Optional<Categoria> opt = cr.findById(id);
		List<Categoria> categorias = cr.findAll();
		
		if (usuario.getMatricula() == null) {
			attributes.addFlashAttribute("noperm", "Você não tem permissão para isso.");
			
			return "redirect:/login";
		}
		
		if (opt.isEmpty()) {
			return "redirect:/home";
		}
		
		Categoria categoria = opt.get();
		
		int iEscolhida = categorias.indexOf(categoria);
		System.out.println(iEscolhida);
		
		int iAbaixo = iEscolhida + 1;
		System.out.println(iAbaixo);
		
		if (!(iAbaixo >= categorias.size())) {
			Categoria categoriaAbaixo = categorias.get(iAbaixo);
			
			System.out.println(categorias);
			
			String temp = categoriaAbaixo.getNome();
			categoriaAbaixo.setNome(categoria.getNome());
			categoria.setNome(temp);
			
			System.out.println(categorias);
			cr.saveAll(categorias);
		}
		
		return "redirect:/categorias/listar_categorias";
	}
	
	@GetMapping("/listar_categorias/{id}/excluir")
	public ModelAndView confirmarCategoria(@PathVariable Long id, Usuario usuario, RedirectAttributes attributes) {
		ModelAndView mv = new ModelAndView();
		Optional<Categoria> opt = cr.findById(id);
		
		if (opt.isEmpty()) {
			mv.setViewName("redirect:/home");
			
			return mv;
		}
		
		if (usuario.getMatricula() == null) {
			mv.setViewName("redirect:/login");
			
			attributes.addFlashAttribute("noperm", "Você não tem permissão para isso.");
			
			return mv;
		}
		
		Categoria categoria = opt.get();
		
		mv.setViewName("categorias/exclusao");
		mv.addObject("categoria", categoria);
		
		return mv;
	}
	
	@GetMapping("/listar_categorias/{id}/excluir/confirmar")
	public String excluirCategoria(@PathVariable Long id, Usuario usuario, RedirectAttributes attributes) {
		Optional<Categoria> opt = cr.findById(id);
		
		if (opt.isEmpty()) {
			return "redirect:/home";
		}
		
		if (usuario.getMatricula() == null) {
			attributes.addFlashAttribute("noperm", "Você não tem permissão para isso.");
			
			return "redirect:/login";
		}
		
		Categoria categoria = opt.get();
		
		cr.delete(categoria);
		
		return "redirect:/categorias/listar_categorias";
	}
}
