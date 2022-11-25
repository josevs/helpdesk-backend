package com.valdir.helpdesk.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.valdir.helpdesk.domain.Tecnico;
import com.valdir.helpdesk.domain.dtos.TecnicoDTO;
import com.valdir.helpdesk.repositories.TecnicoRepository;
import com.valdir.helpdesk.services.exceptions.ObjectnotfoundException;

@Service
public class TecnicoService {
	
	@Autowired
	private TecnicoRepository repository;
	
	
	public Tecnico findById(Long id) {
		Optional<Tecnico> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectnotfoundException("Objeto não encontrado! Id: " + id));
	}


	public List<Tecnico> findAll() {
		return repository.findAll();
	
	}


	public Tecnico create(TecnicoDTO objDTO) {
		objDTO.setId(null); //Para assegurar que o id será nulo.
		Tecnico newObj = new Tecnico(objDTO);
		return repository.save(newObj);
	}
	
}
