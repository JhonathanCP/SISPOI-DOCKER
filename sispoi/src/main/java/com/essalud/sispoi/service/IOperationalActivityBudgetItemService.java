package com.essalud.sispoi.service;

import java.util.List;

import com.essalud.sispoi.model.OperationalActivityBudgetItem;
import com.essalud.sispoi.model.OperationalActivityBudgetItemPK;


public interface IOperationalActivityBudgetItemService extends _ICRUD<OperationalActivityBudgetItem, OperationalActivityBudgetItemPK> {
    
    List<OperationalActivityBudgetItem> findByOperationalActivity(Integer idOperationalActivity);

    OperationalActivityBudgetItem findByIds(Integer idOperationalActivity, Integer idBudgetItem, Integer orderItem);

    void deleteByIds(Integer idOperationalActivity, Integer idBudgetItem, Integer orderItem);

    void deleteByOperationalActivity(Integer idOperationalActivity);

}
