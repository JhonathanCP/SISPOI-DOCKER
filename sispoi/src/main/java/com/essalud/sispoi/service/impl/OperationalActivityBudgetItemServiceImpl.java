package com.essalud.sispoi.service.impl;

import com.essalud.sispoi.model.OperationalActivityBudgetItem;
import com.essalud.sispoi.model.OperationalActivityBudgetItemPK;
import com.essalud.sispoi.repo.IOperationalActivityBudgetItemRepo;
import com.essalud.sispoi.repo._IGenericRepo;
import com.essalud.sispoi.service.IOperationalActivityBudgetItemService;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OperationalActivityBudgetItemServiceImpl extends _CRUDImpl<OperationalActivityBudgetItem, OperationalActivityBudgetItemPK> implements IOperationalActivityBudgetItemService{

    @Autowired
    private IOperationalActivityBudgetItemRepo repo;

    @Override
    protected _IGenericRepo<OperationalActivityBudgetItem, OperationalActivityBudgetItemPK> getRepo() {
        return repo;
    }

    @Override
    public List<OperationalActivityBudgetItem> findByOperationalActivity(Integer idOperationalActivity) {
        return repo.findByOperationalActivity_IdOperationalActivity(idOperationalActivity);
    }

    @Override
    public OperationalActivityBudgetItem findByIds(Integer idOperationalActivity, Integer idBudgetItem, Integer orderItem) {
        return repo.findByOperationalActivity_IdOperationalActivityAndBudgetItem_IdBudgetItemAndOrderItem(idOperationalActivity, idBudgetItem, orderItem);
    }

    @Override
    @Transactional
    public void deleteByIds(Integer idOperationalActivity, Integer idBudgetItem, Integer orderItem) {
        repo.deleteByOperationalActivity_IdOperationalActivityAndBudgetItem_IdBudgetItemAndOrderItem(idOperationalActivity, idBudgetItem, orderItem);
    }

    @Override
    public void deleteByOperationalActivity(Integer idOperationalActivity) {
        repo.deleteByOperationalActivity_IdOperationalActivity(idOperationalActivity);
    }

}