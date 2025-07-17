package com.essalud.sispoi.service.impl;

import com.essalud.sispoi.model.Formulation;
import com.essalud.sispoi.model.FormulationSupportFile;
import com.essalud.sispoi.repo.IFormulationRepo;
import com.essalud.sispoi.repo.IFormulationSupportFileRepo;
import com.essalud.sispoi.repo._IGenericRepo;
import com.essalud.sispoi.service.IFormulationSupportFileService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FormulationSupportFileServiceImpl extends _CRUDImpl<FormulationSupportFile, Integer> implements IFormulationSupportFileService{

    @Autowired
    private IFormulationSupportFileRepo repo;

	@Autowired
	private IFormulationRepo formulationRepo;

    @Override
    protected _IGenericRepo<FormulationSupportFile, Integer> getRepo() {
        return repo;
    }

    @Override
	public int saveFile(FormulationSupportFile formulationSupportFile) {
		FormulationSupportFile ar = repo.save(formulationSupportFile);
		return ar.getIdFormulationSupportFile() > 0 ? 1 : 0;
	}

	@Override
	public byte[] getFile(Integer idFormulationSupportFile) {		
		Optional<FormulationSupportFile> op = repo.findById(idFormulationSupportFile);		
		return op.isPresent() ? op.get().getFile() : new byte[0];
	}

	@Override
	public FormulationSupportFile findByFormulationId(Integer idFormulation) {
		Optional<Formulation> formulation = formulationRepo.findById(idFormulation);
		return formulation.map(obj -> repo.findByFormulation(obj)).orElse(null);
	}

}