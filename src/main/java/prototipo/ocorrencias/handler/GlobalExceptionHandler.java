package prototipo.ocorrencias.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> tratarErrosValidacao(MethodArgumentNotValidException ex) {
		Map<String, String> erros = new HashMap<>();
		
		ex.getBindingResult().getAllErrors().forEach((erro) -> {
			String nomeCampo = ((FieldError) erro).getField();
			String mensagemErro = erro.getDefaultMessage();
			erros.put(nomeCampo, mensagemErro);
		});
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erros);
	}	
}
