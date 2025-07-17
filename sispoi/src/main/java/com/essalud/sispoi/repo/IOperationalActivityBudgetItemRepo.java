package com.essalud.sispoi.repo;

import java.util.List;
import java.util.Optional;

import com.essalud.sispoi.model.BudgetItem;
import com.essalud.sispoi.model.OperationalActivity;
import com.essalud.sispoi.model.OperationalActivityBudgetItem;
import com.essalud.sispoi.model.OperationalActivityBudgetItemPK;


public interface IOperationalActivityBudgetItemRepo extends _IGenericRepo<OperationalActivityBudgetItem, OperationalActivityBudgetItemPK>{

    List<OperationalActivityBudgetItem> findByOperationalActivity_IdOperationalActivity(Integer idOperationalActivity);

    void deleteByOperationalActivity_IdOperationalActivity(Integer idOperationalActivity);

    void deleteByOperationalActivity(OperationalActivity operationalActivity);

    OperationalActivityBudgetItem findByOperationalActivity_IdOperationalActivityAndBudgetItem_IdBudgetItemAndOrderItem(
        Integer idOperationalActivity,
        Integer idBudgetItem,
        Integer orderItem
    );

    void deleteByOperationalActivity_IdOperationalActivityAndBudgetItem_IdBudgetItemAndOrderItem(
        Integer idOperationalActivity,
        Integer idBudgetItem,
        Integer orderItem
    );

    List<OperationalActivityBudgetItem> findByOperationalActivity(OperationalActivity operationalActivity);

    // You might also need methods to find by both parts of the composite key if you're not using save/delete directly.
    Optional<OperationalActivityBudgetItem> findByOperationalActivityAndBudgetItem(OperationalActivity operationalActivity, BudgetItem budgetItem);

}