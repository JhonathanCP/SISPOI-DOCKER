package com.essalud.sispoi.service.impl;

import com.essalud.sispoi.model.StrategicObjective;
import com.essalud.sispoi.model.StrategicObjectiveFile;
import com.essalud.sispoi.repo.IStrategicObjectiveFileRepo;
import com.essalud.sispoi.repo.IStrategicObjectiveRepo;
import com.essalud.sispoi.repo._IGenericRepo;
import com.essalud.sispoi.service.IStrategicObjectiveFileService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StrategicObjectiveFileServiceImpl extends _CRUDImpl<StrategicObjectiveFile, Integer> implements IStrategicObjectiveFileService{

    @Autowired
    private IStrategicObjectiveFileRepo repo;

	@Autowired
    private IStrategicObjectiveRepo strategicObjectiveRepo;

    @Override
    protected _IGenericRepo<StrategicObjectiveFile, Integer> getRepo() {
        return repo;
    }

    @Override
	public int saveFile(StrategicObjectiveFile strategicObjectiveFile) {
		StrategicObjectiveFile ar = repo.save(strategicObjectiveFile);
		return ar.getIdStrategicObjectiveFile() > 0 ? 1 : 0;
	}

	@Override
	public byte[] getFile(Integer idStrategicObjectiveFile) {		
		Optional<StrategicObjectiveFile> op = repo.findById(idStrategicObjectiveFile);		
		return op.isPresent() ? op.get().getFile() : new byte[0];
	}

	@Override
	public StrategicObjectiveFile findByStrategicObjectiveId(Integer idStrategicObjective) {
		Optional<StrategicObjective> strategicObjective = strategicObjectiveRepo.findById(idStrategicObjective);
		return strategicObjective.map(obj -> repo.findByStrategicObjective(obj)).orElse(null);
	}
}