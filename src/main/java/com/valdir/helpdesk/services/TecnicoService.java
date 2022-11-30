package com.valdir.helpdesk.services;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.valdir.helpdesk.domain.Pessoa;
import com.valdir.helpdesk.domain.Tecnico;
import com.valdir.helpdesk.domain.dtos.TecnicoDTO;
import com.valdir.helpdesk.repositories.PessoaRepository;
import com.valdir.helpdesk.repositories.TecnicoRepository;
import com.valdir.helpdesk.services.exceptions.ObjectnotfoundException;
import com.valdir.helpdesk.services.exceptions.DataIntegrityViolationException;

@Service
public class TecnicoService {
	
	@Autowired
	private TecnicoRepository repository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private BCryptPasswordEncoder encoder;	
	
	public Tecnico findById(Long id) {
		Optional<Tecnico> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectnotfoundException("Objeto não encontrado! Id: " + id));
	}

	public List<Tecnico> findAll() {
		return repository.findAll();
	
	}

	public Tecnico create(TecnicoDTO objDTO) {
		objDTO.setId(null); //Para assegurar que o id será nulo.
		objDTO.setSenha( encoder.encode(objDTO.getSenha()) );
		validaPorCpfEEmail(objDTO);
		Tecnico newObj = new Tecnico(objDTO);
		return repository.save(newObj);
	}

	public Tecnico update(Long id, @Valid TecnicoDTO objDTO) {
		objDTO.setId(id); // necessário atribuir id para evitar (falha de segurança) que o framework atribua um id diferente.
		Tecnico oldObj = findById(id);
		validaPorCpfEEmail(objDTO);
		oldObj = new Tecnico(objDTO);
		return repository.save(oldObj);

	}

	public void delete(Long id) {
		Tecnico obj = findById(id);
		
		if (obj.getChamados().size() > 0) {
			throw new DataIntegrityViolationException("Técnico possui ordens de serviço e não pode ser deletado");
		}
		
		repository.deleteById(id);
		
	}	
	
	private void validaPorCpfEEmail(TecnicoDTO objDTO) {
		Optional<Pessoa> obj = pessoaRepository.findByCpf(objDTO.getCpf());
		if(obj.isPresent() && obj.get().getId() != objDTO.getId()) {
			throw new DataIntegrityViolationException("CPF já cadastrado no sistema!");
			
		}
		
		obj = pessoaRepository.findByEmail(objDTO.getEmail());
		if(obj.isPresent() && obj.get().getId() != objDTO.getId()) {
			throw new DataIntegrityViolationException("E-mail já cadastrado no sistema!");
			
		}		
	}




	
	
	
}
