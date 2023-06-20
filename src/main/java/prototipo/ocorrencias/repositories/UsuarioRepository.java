package prototipo.ocorrencias.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import prototipo.ocorrencias.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

}
