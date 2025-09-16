package prototipo.ocorrencias;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import prototipo.ocorrencias.models.Ocorrencia;
import prototipo.ocorrencias.models.Usuario;
import prototipo.ocorrencias.repositories.CategoriaRepository;
import prototipo.ocorrencias.repositories.OcorrenciaRepository;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class OcorrenciasApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private OcorrenciaRepository ocorrenciaRepository;

	@MockBean
	private CategoriaRepository categoriaRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void testFiltrarOcorrenciaWithNulls() throws Exception {
		// 1. Setup mock data
		Ocorrencia o1 = new Ocorrencia();
		o1.setNome("Test Ocorrencia 1");
		o1.setCategoria("Category A");

		Ocorrencia o2 = new Ocorrencia();
		o2.setNome("Test Ocorrencia 2");
		o2.setCategoria(null); // Null category

		Ocorrencia o3 = new Ocorrencia();
		o3.setNome(null); // Null name
		o3.setCategoria("Category B");

		Ocorrencia o4 = new Ocorrencia();
		o4.setNome("Another one");
		o4.setCategoria("Category A");


		List<Ocorrencia> allOcorrencias = new ArrayList<>();
		allOcorrencias.add(o1);
		allOcorrencias.add(o2);
		allOcorrencias.add(o3);
		allOcorrencias.add(o4);

		when(ocorrenciaRepository.findAll()).thenReturn(allOcorrencias);
		when(categoriaRepository.findAll()).thenReturn(new ArrayList<>());

		// 2. Setup user in session
		Usuario usuario = new Usuario();
		usuario.setMatricula(12345L);

		// 3. Perform POST request and assert

		// Test case 1: filter by name, no category filter. Should not throw NPE.
		mockMvc.perform(post("/ocorrencias/listar_ocorrencias")
						.param("page", "1")
						.param("filtro", "Test")
						.param("categoria", "novalue")
						.sessionAttr("usuario", usuario))
				.andExpect(status().isOk())
				.andExpect(view().name("ocorrencias/list"))
				.andExpect(model().attributeExists("ocorrencias"))
				.andExpect(model().attribute("totalSize", 2)); // o1 and o2 should match "Test"

		// Test case 2: filter by name and category. Should not throw NPE on o2 (null category).
		mockMvc.perform(post("/ocorrencias/listar_ocorrencias")
						.param("page", "1")
						.param("filtro", "Test")
						.param("categoria", "Category A")
						.sessionAttr("usuario", usuario))
				.andExpect(status().isOk())
				.andExpect(view().name("ocorrencias/list"))
				.andExpect(model().attributeExists("ocorrencias"))
				.andExpect(model().attribute("totalSize", 1)); // only o1 should match

		// Test case 3: filter with empty name. Should not throw NPE on o3 (null name).
		mockMvc.perform(post("/ocorrencias/listar_ocorrencias")
						.param("page", "1")
						.param("filtro", "")
						.param("categoria", "Category B")
						.sessionAttr("usuario", usuario))
				.andExpect(status().isOk())
				.andExpect(view().name("ocorrencias/list"))
				.andExpect(model().attributeExists("ocorrencias"))
				.andExpect(model().attribute("totalSize", 1)); // only o3 should match

		// Test case 4: filter with empty name and no category. Should return all.
		mockMvc.perform(post("/ocorrencias/listar_ocorrencias")
						.param("page", "1")
						.param("filtro", "")
						.param("categoria", "novalue")
						.sessionAttr("usuario", usuario))
				.andExpect(status().isOk())
				.andExpect(view().name("ocorrencias/list"))
				.andExpect(model().attributeExists("ocorrencias"))
				.andExpect(model().attribute("totalSize", 4)); // all should match
	}
}
