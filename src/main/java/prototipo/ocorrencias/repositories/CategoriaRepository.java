package prototipo.ocorrencias.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import prototipo.ocorrencias.models.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>{

}
