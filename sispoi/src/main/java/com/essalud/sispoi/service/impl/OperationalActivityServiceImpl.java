package com.essalud.sispoi.service.impl;

import com.essalud.sispoi.exception.ModelNotFoundException;
import com.essalud.sispoi.model.CostCenter;
import com.essalud.sispoi.model.Formulation;
import com.essalud.sispoi.model.OperationalActivity;
import com.essalud.sispoi.repo.ICostCenterRepo;
import com.essalud.sispoi.repo.IOperationalActivityBudgetItemRepo;
import com.essalud.sispoi.repo.IOperationalActivityRepo;
import com.essalud.sispoi.repo._IGenericRepo;
import com.essalud.sispoi.service.IOperationalActivityService;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OperationalActivityServiceImpl extends _CRUDImpl<OperationalActivity, Integer> implements IOperationalActivityService{

    @Autowired
    private IOperationalActivityRepo repo;

    @Autowired
    private IOperationalActivityBudgetItemRepo operationalActivityBudgetItemRepository; // Inject the new repository

    @Autowired
    private ICostCenterRepo costCenterRepo;

    @Override
    protected _IGenericRepo<OperationalActivity, Integer> getRepo() {
        return repo;
    }

    @Override
    public List<OperationalActivity> findByFormulation(Formulation formulation) {
        return repo.findByFormulation(formulation);
    }

    @Override
    @Transactional(readOnly = true) // This is a read-only operation
    public String getHigherCorrelativeCodeByCostCenter(Integer idCostCenter) {
        CostCenter costCenter = costCenterRepo.findById(idCostCenter)
                .orElseThrow(() -> new ModelNotFoundException("CostCenter ID DOES NOT EXIST: " + idCostCenter));

        List<OperationalActivity> activities = repo.findByCostCenter(costCenter);

        if (activities.isEmpty()) {
            return "000"; // Or "001" if correlative codes start from 1 and there are no existing activities
                          // "000" seems more appropriate if it means "no higher code found yet".
        }

        // Find the maximum correlative code by parsing them as integers
        Optional<String> higherCorrelativeCode = activities.stream()
                .map(OperationalActivity::getCorrelativeCode)
                .filter(code -> code != null && !code.trim().isEmpty()) // Filter out null or empty codes
                .max(Comparator.comparingInt(Integer::parseInt)); // Parse as int for comparison

        return higherCorrelativeCode.orElse("000"); // Return the highest or "000" if none found (e.g., all were null/empty)
    }

    @Transactional // Ensures the whole operation is a single transaction
    @Override // Assuming this is an override of an interface method
    public void delete(Integer id) {
        OperationalActivity obj = repo.findById(id)
                                    .orElseThrow(() -> new ModelNotFoundException("ID NO EXISTE: " + id));

        // 1. Eliminar todos los OperationalActivityBudgetItem asociados
        //    Esto es CRÍTICO antes de eliminar la OperationalActivity principal.
        operationalActivityBudgetItemRepository.deleteByOperationalActivity(obj);

        // 2. Eliminar la OperationalActivity principal.
        //    Gracias a cascade = CascadeType.ALL y orphanRemoval = true en 'goals',
        //    los objetivos asociados también se eliminarán automáticamente.
        repo.delete(obj);
    }

}