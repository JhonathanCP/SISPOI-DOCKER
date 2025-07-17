package com.essalud.sispoi.service.impl;

import com.essalud.sispoi.model.StrategicAction;
import com.essalud.sispoi.model.StrategicActionFile;
import com.essalud.sispoi.repo.IStrategicActionFileRepo;
import com.essalud.sispoi.repo.IStrategicActionRepo;
import com.essalud.sispoi.repo._IGenericRepo;
import com.essalud.sispoi.service.IStrategicActionFileService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StrategicActionFileServiceImpl extends _CRUDImpl<StrategicActionFile, Integer> implements IStrategicActionFileService{

    @Autowired
    private IStrategicActionFileRepo repo;

	@Autowired
    private IStrategicActionRepo strategicActionRepo;

    @Override
    protected _IGenericRepo<StrategicActionFile, Integer> getRepo() {
        return repo;
    }

    @Override
	public int saveFile(StrategicActionFile strategicActionFile) {
		StrategicActionFile ar = repo.save(strategicActionFile);
		return ar.getIdStrategicActionFile() > 0 ? 1 : 0;
	}

	@Override
	public byte[] getFile(Integer idStrategicActionFile) {		
		Optional<StrategicActionFile> op = repo.findById(idStrategicActionFile);		
		return op.isPresent() ? op.get().getFile() : new byte[0];
	}

	@Override
	public StrategicActionFile findByStrategicActionId(Integer idStrategicAction) {
		Optional<StrategicAction> strategicAction = strategicActionRepo.findById(idStrategicAction);
		return strategicAction.map(obj -> repo.findByStrategicAction(obj)).orElse(null);
	}

}