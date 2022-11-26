package com.valdir.helpdesk.services;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.valdir.helpdesk.domain.Pessoa;
import com.valdir.helpdesk.domain.Cliente;
import com.valdir.helpdesk.domain.dtos.ClienteDTO;
import com.valdir.helpdesk.repositories.PessoaRepository;
import com.valdir.helpdesk.repositories.ClienteRepository;
import com.valdir.helpdesk.services.exceptions.ObjectnotfoundException;
import com.valdir.helpdesk.services.exceptions.DataIntegrityViolationException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	
	public Cliente findById(Long id) {
		Optional<Cliente> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectnotfoundException("Objeto não encontrado! Id: " + id));
	}

	public List<Cliente> findAll() {
		return repository.findAll();
	
	}

	public Cliente create(ClienteDTO objDTO) {
		objDTO.setId(null); //Para assegurar que o id será nulo.
		validaPorCpfEEmail(objDTO);
		Cliente newObj = new Cliente(objDTO);
		return repository.save(newObj);
	}

	public Cliente update(Long id, @Valid ClienteDTO objDTO) {
		objDTO.setId(id); // necessário atribuir id para evitar (falha de segurança) que o framework atribua um id diferente.
		Cliente oldObj = findById(id);
		validaPorCpfEEmail(objDTO);
		oldObj = new Cliente(objDTO);
		return repository.save(oldObj);

	}

	public void delete(Long id) {
		Cliente obj = findById(id);
		
		if (obj.getChamados().size() > 0) {
			throw new DataIntegrityViolationException("Cliente possui ordens de serviço e não pode ser deletado");
		}
		
		repository.deleteById(id);
		
	}	
	
	private void validaPorCpfEEmail(ClienteDTO objDTO) {
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
