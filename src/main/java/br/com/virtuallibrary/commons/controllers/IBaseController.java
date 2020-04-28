package br.com.virtuallibrary.commons.controllers;

import java.io.Serializable;

import org.springframework.hateoas.RepresentationModel;

import br.com.virtuallibrary.commons.entities.BaseEntity;
import br.com.virtuallibrary.commons.repositories.IBaseRepository;
import br.com.virtuallibrary.commons.services.IBaseService;

public interface IBaseController<E extends BaseEntity, K extends Serializable, R extends IBaseRepository<E, K>, S extends IBaseService<E, K, R>, M extends RepresentationModel<M>> {

}
