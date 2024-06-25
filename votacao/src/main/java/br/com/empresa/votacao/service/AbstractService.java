package br.com.empresa.votacao.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class AbstractService {
	
	@Autowired
	protected ModelMapper modelMapper;
	
    public <E, D> List<D> convertToDTO(JpaRepository<E, Long> repository, Class<D> dtoClass) {
        List<D> dtos = new ArrayList<>();
        List<E> entities = repository.findAll();
        for (E entity : entities) {
            dtos.add(modelMapper.map(entity, dtoClass));
        }
        return dtos;
    }
	
}
