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
import prototipo.ocorrencias.dtos.OcorrenciaFiltroDTO;
import prototipo.ocorrencias.models.Categoria;
import prototipo.ocorrencias.models.Ocorrencia;
import prototipo.ocorrencias.models.Tratamento;
import prototipo.ocorrencias.models.Usuario;
import prototipo.ocorrencias.repositories.CategoriaRepository;
import prototipo.ocorrencias.repositories.OcorrenciaRepository;
import prototipo.ocorrencias.repositories.TratamentoRepository;

@Controller
@SessionAttributes("usuario")
@RequestMapping("/ocorrencias")
public class OcorrenciasController {
	
	@Autowired
	private CategoriaRepository cr;
	@Autowired
	private OcorrenciaRepository or;
	@Autowired
	private TratamentoRepository tr;
	
	@GetMapping("/menu")
	public String menuOcorrencias(HttpServletRequest request, Usuario usuario, RedirectAttributes attributes) {
		if (usuario.getMatricula() == null) {
			attributes.addFlashAttribute("noperm", "Você não tem permissão para isso.");
			
			return "redirect:/login";
		}
		
		List<Ocorrencia> ocorrencias = or.findAll();
	
		request.getSession().setAttribute("ocorrenciasFiltradas", ocorrencias);
		
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
	public String cadastrarOcorrencia(Ocorrencia ocorrencia, Tratamento tratamento, @RequestParam String metodo, @RequestParam Categoria categoria) {
		ocorrencia.setCategoria(categoria.getNome());
		ocorrencia.setTempo(LocalDateTime.of(ocorrencia.getData(), ocorrencia.getHorario()));
		
		or.saveAndFlush(ocorrencia);
		
		tratamento.setOcorrencia(ocorrencia);
		tratamento.setMetodo(metodo);
		System.out.println(tratamento);
		
		tr.save(tratamento);
		
		System.out.println(ocorrencia);
		
		return "redirect:/ocorrencias/menu";
	}
	
	@GetMapping("/listar_ocorrencias")
	public ModelAndView listarOcorrencias(HttpServletRequest request, Usuario usuario, RedirectAttributes attributes, @RequestParam("page") Integer page) {
		ModelAndView mv = new ModelAndView();
		
		List<Ocorrencia> ocorrencias = or.findAll();
		List<Categoria> categorias = cr.findAll();
		
		@SuppressWarnings("unchecked")
		List<Ocorrencia> ocorrenciasFiltradas = (List<Ocorrencia>) request.getSession().getAttribute("ocorrenciasFiltradas");
		ArrayList<Ocorrencia> ocorrenciasPag = new ArrayList<>();
		
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
		
		int contagem = 1;
		int pages;
		
		if (ocorrenciasFiltradas == null) {
			if (ocorrencias.size() % 10 == 0) {
				pages = ocorrencias.size() / 10;
			} else {
				pages = (ocorrencias.size() / 10) + 1;
			}
			
			if (page < 1 || page > pages) {
				mv.setViewName("redirect:/ocorrencias/listar_ocorrencias?page=1");
				
				return mv;
			}
			
			for (int i = (page - 1) * 10; i < ocorrencias.size(); i++) {
				Ocorrencia ocorrencia = ocorrencias.get(i);
				ocorrenciasPag.add(ocorrencia);
				
				if (contagem == 10) {
					break;
				} else {
					contagem++;
				}
			}
		} else {
			if (ocorrenciasFiltradas.size() % 10 == 0) {
				pages = ocorrenciasFiltradas.size() / 10;
			} else {
				pages = (ocorrenciasFiltradas.size() / 10) + 1;
			}
			
			if (page < 1 || page > pages) {
				mv.setViewName("redirect:/ocorrencias/listar_ocorrencias?page=1");
				
				return mv;
			}
			
			for (int i = (page - 1) * 10; i < ocorrenciasFiltradas.size(); i++) {
				Ocorrencia ocorrencia = ocorrenciasFiltradas.get(i);
				ocorrenciasPag.add(ocorrencia);
				
				if (contagem == 10) {
					break;
				} else {
					contagem++;
				}
			}
		}
		
		String URLsite = request.getRequestURI();
		request.getSession().setAttribute("ocorrenciasPag", ocorrenciasPag);
		request.getSession().setAttribute("ocorrenciasFiltradas", ocorrenciasFiltradas);
		
		mv.setViewName("ocorrencias/list");
		mv.addObject("ocorrencias", ocorrenciasPag);
		mv.addObject("categorias", categorias);
		mv.addObject("pages", pages);
		mv.addObject("page", page);
		mv.addObject("pageSize", ocorrenciasPag.size());
		
		if (ocorrenciasFiltradas == null) {
			mv.addObject("totalSize", ocorrencias.size());
		} else {
			mv.addObject("totalSize", ocorrenciasFiltradas.size());
		}
		
		mv.addObject("requestURI", URLsite);
				
		return mv;
	}
	
	@GetMapping("/listar_ocorrencias/{id}/detalhes")
	public ModelAndView detalharOcorrencia(@PathVariable Long id, Usuario usuario, RedirectAttributes attributes) {
		ModelAndView mv = new ModelAndView();
		Optional<Ocorrencia> opt = or.findById(id);
		
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
		mv.addObject("ocorrencia", ocorrencia);
		
		List<Tratamento> tratamentos = tr.findByOcorrencia(ocorrencia);
		mv.addObject("tratamentos", tratamentos);
		
		mv.setViewName("ocorrencias/detalhes");
		
		return mv;
	}
	
	@PostMapping("/listar_ocorrencias/{id}/detalhes/adicionar")
	public String adicionarTratamento(@PathVariable Long id, Tratamento tratamento, Usuario usuario, RedirectAttributes attributes) {
		Optional<Ocorrencia> opt = or.findById(id);
		
		if (opt.isEmpty()) {
			return "redirect:/home";
		}
		
		if (usuario.getMatricula() == null) {
			attributes.addFlashAttribute("noperm", "Você não tem permissão para isso.");
			
			return "redirect:/login";
		}
		
		Ocorrencia ocorrencia = opt.get();
		
		tratamento.setOcorrencia(ocorrencia);
		
		tr.save(tratamento);
		
		return "redirect:/ocorrencias/listar_ocorrencias/" + id + "/detalhes";
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
		
		List<Tratamento> tratamentos = tr.findByOcorrencia(ocorrencia);
		
		mv.setViewName("ocorrencias/edicao");
		mv.addObject("ocorrencia", ocorrencia);
		mv.addObject("tratamentos", tratamentos);
		mv.addObject("categorias", categorias);
		
		System.out.println(ocorrencia);
		
		return mv;
	}
	
	@PostMapping("/listar_ocorrencias/{id}/editar")
	public ModelAndView editarOcorrencia(@PathVariable Long id, Usuario usuario, Ocorrencia ocorrencia, Tratamento tratamento, RedirectAttributes attributes, HttpServletRequest request, @RequestParam Long categoria) {
		ModelAndView mv = new ModelAndView();
		
		tr.save(tratamento);
		
		Optional<Categoria> opt = cr.findById(categoria);
		
		if (!opt.isEmpty()) {
			Categoria categoriaParam = opt.get();
			
			ocorrencia.setCategoria(categoriaParam.getNome());
			or.saveAndFlush(ocorrencia);
		}
		
		System.out.println(tratamento);
		System.out.println(ocorrencia);
		
		List<Ocorrencia> ocorrencias = or.findAll();
		
		request.getSession().setAttribute("ocorrenciasFiltradas", ocorrencias);
		
		mv.setViewName("redirect:/ocorrencias/listar_ocorrencias?page=1");
		
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
	
	@GetMapping("/listar_ocorrencias/{id}/detalhes/{idTratamento}/excluir")
	public ModelAndView excluirTratamento(@PathVariable Long id, @PathVariable Long idTratamento, Usuario usuario, RedirectAttributes attributes) {
		ModelAndView mv = new ModelAndView();
		Optional<Ocorrencia> opt = or.findById(id);
		Optional<Tratamento> optTratamento = tr.findById(idTratamento);
		
		if (opt.isEmpty() || optTratamento.isEmpty()) {
			mv.setViewName("redirect:/home");
			
			return mv;
		}
		
		if (usuario.getMatricula() == null) {
			mv.setViewName("redirect:/login");
			
			attributes.addFlashAttribute("noperm", "Você não tem permissão para isso.");
			
			return mv;
		}
		
		Tratamento tratamento = optTratamento.get();
		tr.delete(tratamento);
		
		mv.setViewName("redirect:/ocorrencias/listar_ocorrencias/" + id + "/detalhes");
		
		return mv;
	}
	
	@GetMapping("/listar_ocorrencias/{id}/excluir/confirmar")
	public String excluirOcorrencia(HttpServletRequest request, @PathVariable Long id, Usuario usuario, RedirectAttributes attributes) {
		Optional<Ocorrencia> opt = or.findById(id);
		
		if (opt.isEmpty()) {
			return "redirect:/home";
		}
		
		if (usuario.getMatricula() == null) {
			attributes.addFlashAttribute("noperm", "Você não tem permissão para isso.");
			
			return "redirect:/login";
		}
		
		Ocorrencia ocorrencia = opt.get();
		
		List<Tratamento> tratamentos = tr.findByOcorrencia(ocorrencia);
		
		tr.deleteAll(tratamentos);
		or.delete(ocorrencia);
		
		List<Ocorrencia> ocorrencias = or.findAll();
		
		request.getSession().setAttribute("ocorrenciasFiltradas", ocorrencias);
		
		return "redirect:/ocorrencias/listar_ocorrencias?page=1";
	}
	
	@PostMapping("/listar_ocorrencias")
	public ModelAndView filtrarOcorrencia(OcorrenciaFiltroDTO filtro, HttpServletRequest request, RedirectAttributes attributes, @RequestParam("page") Integer page) {
		ModelAndView mv = new ModelAndView();
		
		List<Ocorrencia> ocorrencias = or.findAll();
		List<Categoria> categorias = cr.findAll();
		
		ArrayList<Ocorrencia> ocorrenciasFiltradas = new ArrayList<>();
		ArrayList<Ocorrencia> ocorrenciasPag = new ArrayList<>();
		
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
			mv.setViewName("redirect:/ocorrencias/listar_ocorrencias?page=1");
			
			attributes.addFlashAttribute("error", "Nenhuma ocorrência correspondente foi encontrada.");
			
			return mv;
		}
		
		int contagem = 1;
		int pages;
		
		if (ocorrenciasFiltradas.size() % 10 == 0) {
			pages = ocorrenciasFiltradas.size() / 10;
		} else {
			pages = (ocorrenciasFiltradas.size() / 10) + 1;
		}
		
		if (page < 1 || page > pages) {
			mv.setViewName("redirect:/ocorrencias/listar_ocorrencias?page=1");
			
			return mv;
		}
		
		for (int i = (page - 1) * 10; i < ocorrenciasFiltradas.size(); i++) {
			Ocorrencia ocorrencia = ocorrenciasFiltradas.get(i);
			ocorrenciasPag.add(ocorrencia);
			
			if (contagem == 10) {
				break;
			} else {
				contagem++;
			}
		}
		
		request.getSession().setAttribute("ocorrenciasFiltradas", ocorrenciasFiltradas);
		request.getSession().setAttribute("ocorrenciasPag", ocorrenciasPag);
		
		String URLsite = request.getRequestURI();
		
		mv.setViewName("ocorrencias/list");
		mv.addObject("ocorrencias", ocorrenciasPag);
		mv.addObject("categorias", categorias);
		mv.addObject("pages", pages);
		mv.addObject("page", page);
		mv.addObject("pageSize", ocorrenciasPag.size());
		mv.addObject("totalSize", ocorrenciasFiltradas.size());
		mv.addObject("requestURI", URLsite);
		
		return mv;
	}
	
	@GetMapping("/listar_ocorrencias/{filtro}")
	public ModelAndView ordenarPorCategoria(Usuario usuario, RedirectAttributes attributes, HttpServletRequest request, @PathVariable String filtro, @RequestParam("page") Integer page) {
		ModelAndView mv = new ModelAndView();
		
		@SuppressWarnings("unchecked")
		List<Ocorrencia> ocorrenciasFiltradas = (List<Ocorrencia>) request.getSession().getAttribute("ocorrenciasFiltradas");
		ArrayList<Ocorrencia> ocorrenciasPag = new ArrayList<>();
		
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
			mv.setViewName("redirect:/ocorrencias/listar_ocorrencias?page=1");
			
			return mv;
		}
		
		int pages, contagem = 1;
		
		if (ocorrenciasFiltradas.size() % 10 == 0) {
			pages = ocorrenciasFiltradas.size() / 10;
		} else {
			pages = (ocorrenciasFiltradas.size() / 10) + 1;
		}
		
		if (page < 1 || page > pages) {
			mv.setViewName("redirect:/ocorrencias/listar_ocorrencias?page=1");
			
			return mv;
		}
		
		for (int i = (page - 1) * 10; i < ocorrenciasFiltradas.size(); i++) {
			Ocorrencia ocorrencia = ocorrenciasFiltradas.get(i);
			ocorrenciasPag.add(ocorrencia);
			
			if (contagem == 10) {
				break;
			} else {
				contagem++;
			}
		}
		
		String URLsite = request.getRequestURI();
		
		request.getSession().setAttribute("ocorrenciasFiltradas", ocorrenciasFiltradas);
		request.getSession().setAttribute("ocorrenciasPag", ocorrenciasPag);
		
	    mv.setViewName("/ocorrencias/list");
		mv.addObject("ocorrencias", ocorrenciasPag);
		mv.addObject("categorias", categorias);
		mv.addObject("pages", pages);
		mv.addObject("page", page);
		mv.addObject("filtro", filtro);
		mv.addObject("pageSize", ocorrenciasPag.size());
		mv.addObject("totalSize", ocorrenciasFiltradas.size());
		mv.addObject("requestURI", URLsite);
		
		return mv;
	}
}
