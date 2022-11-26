package com.valdir.helpdesk.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.valdir.helpdesk.domain.Chamado;
import com.valdir.helpdesk.repositories.ChamadoRepository;
import com.valdir.helpdesk.services.exceptions.ObjectnotfoundException;

@Service
public class ChamadoService {
	
	@Autowired
	private ChamadoRepository repository;
	
	public Chamado findById(Long id) {
		Optional<Chamado> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectnotfoundException("Objeto não encontrado! ID:" + id));
	}

}
