package prototipo.ocorrencias.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import prototipo.ocorrencias.models.Categoria;
import prototipo.ocorrencias.models.Filtro;
import prototipo.ocorrencias.models.Ocorrencia;
import prototipo.ocorrencias.models.Usuario;
import prototipo.ocorrencias.repositories.CategoriaRepository;
import prototipo.ocorrencias.repositories.OcorrenciaRepository;

@Controller
@SessionAttributes("usuario")
@RequestMapping("/ocorrencias")
public class OcorrenciasController {
	
	@Autowired
	private CategoriaRepository cr;
	@Autowired
	private OcorrenciaRepository or;
	
	@GetMapping("/menu")
	public String menuOcorrencias(Usuario usuario, RedirectAttributes attributes) {
		if (usuario.getMatricula() == null) {
			attributes.addFlashAttribute("noperm", "Você não tem permissão para isso.");
			
			return "redirect:/login";
		}
		
		return "ocorrencias/menu";
	}
	
	@GetMapping("/cadastrar_ocorrencia")
	public ModelAndView formOcorrencia(Usuario usuario, RedirectAttributes attributes) {
		ModelAndView mv = new ModelAndView();
		List<Categoria> categorias = cr.findAll();
		
		if (usuario.getMatricula() == null) {
			attributes.addFlashAttribute("noperm", "Você não tem permissão para isso.");
			mv.setViewName("redirect:/login");
			
			return mv;
		}
		
		mv.addObject("categorias", categorias);
		mv.setViewName("ocorrencias/form");
		
		return mv;
	}
	
	@PostMapping("/cadastrar_ocorrencia")
	public String cadastrarOcorrencia(Ocorrencia ocorrencia, @RequestParam Categoria categoria) {
		ocorrencia.setCategoria(categoria.getNome());
		ocorrencia.setTempo(LocalDateTime.of(ocorrencia.getData(), ocorrencia.getHorario()));
		System.out.println(ocorrencia);
		
		or.saveAndFlush(ocorrencia);
		
		return "redirect:/ocorrencias/menu";
	}
	
	@GetMapping("/listar_ocorrencias")
	public ModelAndView listarOcorrencias(HttpServletRequest request, Usuario usuario, RedirectAttributes attributes) {
		ModelAndView mv = new ModelAndView();
		List<Ocorrencia> ocorrencias = or.findAll();
		List<Categoria> categorias = cr.findAll();
		
		if (usuario.getMatricula() == null) {
			attributes.addFlashAttribute("noperm", "Você não tem permissão para isso.");
			mv.setViewName("redirect:/login");
			
			return mv;
		}
		
		if (ocorrencias.isEmpty()) {
			mv.setViewName("redirect:/ocorrencias/menu");
			attributes.addFlashAttribute("error", "Não existem ocorrências cadastradas.");
			
			return mv;
		}
		
		String URLsite = request.getRequestURI();
		request.getSession().setAttribute("ocorrenciasFiltradas", ocorrencias);
		
		mv.setViewName("ocorrencias/list");
		mv.addObject("ocorrencias", ocorrencias);
		mv.addObject("categorias", categorias);
		mv.addObject("requestURI", URLsite);
		
		return mv;
	}
	
	@GetMapping("/listar_ocorrencias/{id}/editar")
	public ModelAndView selecionarOcorrencia(@PathVariable Long id, Usuario usuario, RedirectAttributes attributes) {
		ModelAndView mv = new ModelAndView();
		Optional<Ocorrencia> opt = or.findById(id);
		List<Categoria> categorias = cr.findAll();
		
		if (opt.isEmpty()) {
			mv.setViewName("redirect:/home");
			
			return mv;
		}
		
		if (usuario.getMatricula() == null) {
			attributes.addFlashAttribute("noperm", "Você não tem permissão para isso.");
			mv.setViewName("redirect:/login");
			
			return mv;
		}
		
		Ocorrencia ocorrencia = opt.get();
		
		mv.setViewName("ocorrencias/edicao");
		mv.addObject("ocorrencia", ocorrencia);
		mv.addObject("categorias", categorias);
		
		System.out.println(ocorrencia);
		
		return mv;
	}
	
	@GetMapping("/listar_ocorrencias/{id}/excluir")
	public ModelAndView confirmarOcorrencia(@PathVariable Long id, Usuario usuario, RedirectAttributes attributes) {
		ModelAndView mv = new ModelAndView();
		Optional<Ocorrencia> opt = or.findById(id);
		
		if (opt.isEmpty()) {
			mv.setViewName("redirect:/home");
			
			return mv;
		}
		
		if (usuario.getMatricula() == null) {
			mv.setViewName("redirect:/login");
			
			attributes.addFlashAttribute("noperm", "Você não tem permissão para isso.");
			
			return mv;
		}
		
		Ocorrencia ocorrencia = opt.get();
		
		mv.setViewName("ocorrencias/exclusao");
		mv.addObject("ocorrencia", ocorrencia);
		
		return mv;
	}
	
	@GetMapping("/listar_ocorrencias/{id}/excluir/confirmar")
	public String excluirOcorrencia(@PathVariable Long id, Usuario usuario, RedirectAttributes attributes) {
		Optional<Ocorrencia> opt = or.findById(id);
		
		if (opt.isEmpty()) {
			return "redirect:/home";
		}
		
		if (usuario.getMatricula() == null) {
			attributes.addFlashAttribute("noperm", "Você não tem permissão para isso.");
			
			return "redirect:/login";
		}
		
		Ocorrencia ocorrencia = opt.get();
		
		or.delete(ocorrencia);
		
		return "redirect:/ocorrencias/listar_ocorrencias";
	}
	
	@PostMapping("/listar_ocorrencias")
	public ModelAndView filtrarOcorrencia(Filtro filtro, HttpServletRequest request, RedirectAttributes attributes) {
		ModelAndView mv = new ModelAndView();
		
		List<Ocorrencia> ocorrencias = or.findAll();
		List<Categoria> categorias = cr.findAll();
		
		ArrayList<Ocorrencia> ocorrenciasFiltradas = new ArrayList<>();
		
		System.out.println("FILTRO: " + filtro);
		
		for (int i = 0; i < ocorrencias.size(); i++) {
			Ocorrencia ocorrencia = ocorrencias.get(i);
			String ocorrenciaNome = ocorrencia.getNome();
			String ocorrenciaCategoria = ocorrencia.getCategoria();
			
			if (ocorrenciaNome.toUpperCase().contains(filtro.getFiltro().toUpperCase()) && ocorrenciaCategoria.equals(filtro.getCategoria())) {
				ocorrenciasFiltradas.add(ocorrencia);
			} else if (ocorrenciaNome.toUpperCase().contains(filtro.getFiltro().toUpperCase()) && filtro.getCategoria().equals("novalue")) {
				ocorrenciasFiltradas.add(ocorrencia);
			}
		}
		
		if (ocorrenciasFiltradas.isEmpty()) {
			mv.setViewName("redirect:/ocorrencias/listar_ocorrencias");
			
			attributes.addFlashAttribute("error", "Nenhuma ocorrência correspondente foi encontrada.");
			
			return mv;
		}
		
		request.getSession().setAttribute("ocorrenciasFiltradas", ocorrenciasFiltradas);
		
		String URLsite = request.getRequestURI();
		
		mv.addObject("ocorrencias", ocorrenciasFiltradas);
		mv.addObject("categorias", categorias);
		mv.addObject("requestURI", URLsite);
		mv.setViewName("/ocorrencias/list");
		
		return mv;
	}
	
	@GetMapping("/listar_ocorrencias/{filtro}")
	public ModelAndView ordenarPorCategoria(Usuario usuario, RedirectAttributes attributes, HttpServletRequest request, @PathVariable String filtro) {
		ModelAndView mv = new ModelAndView();
		
		@SuppressWarnings("unchecked")
		List<Ocorrencia> ocorrenciasFiltradas = (List<Ocorrencia>) request.getSession().getAttribute("ocorrenciasFiltradas");
		List<Categoria> categorias = cr.findAll();
		
		if (ocorrenciasFiltradas == null) {
			List<Ocorrencia> ocorrencias = or.findAll();
			
			ocorrenciasFiltradas = ocorrencias;
		}
		
		if (usuario.getMatricula() == null) {
			attributes.addFlashAttribute("noperm", "Você não tem permissão para isso.");
			
			mv.setViewName("redirect:/login");
			return mv;
		}
		
		if (filtro.equals("dataDecrescente")) {
			ocorrenciasFiltradas.sort(Comparator.comparing(Ocorrencia::getTempo).reversed());
		} else if (filtro.equals("dataCrescente")) {
			ocorrenciasFiltradas.sort(Comparator.comparing(Ocorrencia::getTempo));
		} else if (filtro.equals("nomeDecrescente")) {
			ocorrenciasFiltradas.sort(Comparator.comparing(Ocorrencia::getNome).reversed());
		} else if (filtro.equals("nomeCrescente")) {
			ocorrenciasFiltradas.sort(Comparator.comparing(Ocorrencia::getNome));
		} else {
			mv.setViewName("redirect:/ocorrencias/listar_ocorrencias");
			
			return mv;
		}
		
		String URLsite = request.getRequestURI();
		
	    mv.setViewName("/ocorrencias/list");
		mv.addObject("ocorrencias", ocorrenciasFiltradas);
		mv.addObject("categorias", categorias);
		mv.addObject("requestURI", URLsite);
		
		return mv;
	}
}
