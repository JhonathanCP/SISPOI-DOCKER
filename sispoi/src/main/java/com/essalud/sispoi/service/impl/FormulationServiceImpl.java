package com.essalud.sispoi.service.impl;

import com.essalud.sispoi.exception.ModelNotFoundException;
import com.essalud.sispoi.model.Dependency;
import com.essalud.sispoi.model.ExecutedGoal;
import com.essalud.sispoi.model.Formulation;
import com.essalud.sispoi.model.FormulationSupportFile;
import com.essalud.sispoi.model.Goal;
import com.essalud.sispoi.model.OperationalActivity;
import com.essalud.sispoi.model.OperationalActivityBudgetItem;
import com.essalud.sispoi.repo.IExecutedGoalRepo;
import com.essalud.sispoi.repo.IFormulationRepo;
import com.essalud.sispoi.repo.IFormulationSupportFileRepo;
import com.essalud.sispoi.repo.IGoalRepo;
import com.essalud.sispoi.repo.IOperationalActivityBudgetItemRepo;
import com.essalud.sispoi.repo.IOperationalActivityRepo;
import com.essalud.sispoi.repo._IGenericRepo;
import com.essalud.sispoi.service.IFormulationService;

import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FormulationServiceImpl extends _CRUDImpl<Formulation, Integer> implements IFormulationService{

    @Autowired
    private IFormulationRepo repo;

    @Autowired
    private IFormulationSupportFileRepo formulationSupportFileRepo;

    @Autowired
    private IOperationalActivityRepo operationalActivityRepo;

    @Autowired
    private IGoalRepo goalRepo;

    @Autowired
    private IExecutedGoalRepo executedGoalRepo;

    @Autowired
    private IOperationalActivityBudgetItemRepo operationalActivityBudgetItemRepo;

    @Override
    protected _IGenericRepo<Formulation, Integer> getRepo() {
        return repo;
    }

    @Override
    public List<Formulation> findByDependencyAndYear(Dependency dependency, Integer year) {
        return repo.findByDependencyAndYear(dependency, year);
    }

    @Override
    @Transactional
    public Formulation addModification(Integer originalFormulationId, Integer newQuarter) {
        Formulation originalFormulation = repo.findById(originalFormulationId)
                .orElseThrow(() -> new ModelNotFoundException("Original Formulation ID DOES NOT EXIST: " + originalFormulationId));

        // 1. Create a new Formulation entity
        Formulation newFormulation = new Formulation();
        newFormulation.setActive(originalFormulation.getActive());
        newFormulation.setDependency(originalFormulation.getDependency());
        newFormulation.setFormulationState(originalFormulation.getFormulationState());
        newFormulation.setCreateTime(LocalDateTime.now());
        newFormulation.setYear(originalFormulation.getYear());
        newFormulation.setModification(originalFormulation.getModification() + 1);
        newFormulation.setQuarter(newQuarter);

        Formulation savedNewFormulation = repo.save(newFormulation);

        // 2. Replicate FormulationSupportFile
        if (originalFormulation.getFormulationSupportFile() != null) {
            FormulationSupportFile originalSupportFile = originalFormulation.getFormulationSupportFile();
            FormulationSupportFile newSupportFile = new FormulationSupportFile();
            newSupportFile.setName(originalSupportFile.getName());
            newSupportFile.setFileExtension(originalSupportFile.getFileExtension());
            newSupportFile.setActive(originalSupportFile.getActive());
            newSupportFile.setCreateTime(LocalDateTime.now());
            newSupportFile.setFile(originalSupportFile.getFile() != null ? originalSupportFile.getFile().clone() : null);
            newSupportFile.setFormulation(savedNewFormulation);
            formulationSupportFileRepo.save(newSupportFile);

            savedNewFormulation.setFormulationSupportFile(newSupportFile);
            repo.save(savedNewFormulation);
        }

        // 3. Replicate OperationalActivities
        List<OperationalActivity> originalOperationalActivities = operationalActivityRepo.findByFormulation(originalFormulation);
        List<OperationalActivity> newOperationalActivities = new ArrayList<>();

        for (OperationalActivity originalOpActivity : originalOperationalActivities) {
            OperationalActivity newOpActivity = new OperationalActivity();
            newOpActivity.setSapCode(originalOpActivity.getSapCode());
            newOpActivity.setCorrelativeCode(originalOpActivity.getCorrelativeCode());
            newOpActivity.setName(originalOpActivity.getName());
            newOpActivity.setDescription(originalOpActivity.getDescription());
            newOpActivity.setActive(originalOpActivity.getActive());
            newOpActivity.setStrategicAction(originalOpActivity.getStrategicAction());
            newOpActivity.setFormulation(savedNewFormulation);
            newOpActivity.setFinancialFund(originalOpActivity.getFinancialFund());
            newOpActivity.setManagementCenter(originalOpActivity.getManagementCenter());
            newOpActivity.setCostCenter(originalOpActivity.getCostCenter());
            newOpActivity.setMeasurementType(originalOpActivity.getMeasurementType());
            newOpActivity.setMeasurementUnit(originalOpActivity.getMeasurementUnit());
            newOpActivity.setPriority(originalOpActivity.getPriority());
            newOpActivity.setGoods(originalOpActivity.getGoods());
            newOpActivity.setRemuneration(originalOpActivity.getRemuneration());
            newOpActivity.setServices(originalOpActivity.getServices());
            newOpActivity.setCreateTime(LocalDateTime.now());

            OperationalActivity savedNewOpActivity = operationalActivityRepo.save(newOpActivity);
            newOperationalActivities.add(savedNewOpActivity);

            // 4.A Replicate Goals for each OperationalActivity
            List<Goal> originalGoals = goalRepo.findByOperationalActivity(originalOpActivity);
            for (Goal originalGoal : originalGoals) {
                Goal newGoal = new Goal();
                newGoal.setActive(originalGoal.getActive());
                newGoal.setOperationalActivity(savedNewOpActivity);
                newGoal.setGoalOrder(originalGoal.getGoalOrder());
                newGoal.setValue(originalGoal.getValue());
                newGoal.setCreateTime(LocalDateTime.now());
                goalRepo.save(newGoal);
            }

            // 4.B Replicate ExecutedGoals for each OperationalActivity
            List<ExecutedGoal> originalExecutedGoals = executedGoalRepo.findByOperationalActivity(originalOpActivity);
            for (ExecutedGoal originalExecutedGoal : originalExecutedGoals) {
                ExecutedGoal newExecutedGoal = new ExecutedGoal();
                newExecutedGoal.setActive(originalExecutedGoal.getActive());
                newExecutedGoal.setOperationalActivity(savedNewOpActivity);
                newExecutedGoal.setGoalOrder(originalExecutedGoal.getGoalOrder());
                newExecutedGoal.setValue(originalExecutedGoal.getValue());
                newExecutedGoal.setCreateTime(LocalDateTime.now());
                executedGoalRepo.save(newExecutedGoal);
            }

            // 5. Replicate OperationalActivityBudgetItems for each OperationalActivity
            // Use the corrected method name for fetching based on the operational activity.
            List<OperationalActivityBudgetItem> originalBudgetItems = operationalActivityBudgetItemRepo.findByOperationalActivity(originalOpActivity);
            for (OperationalActivityBudgetItem originalBudgetItem : originalBudgetItems) {
                OperationalActivityBudgetItem newBudgetItem = new OperationalActivityBudgetItem();
                // When using @IdClass, you directly set the @Id fields on the entity itself.
                newBudgetItem.setOperationalActivity(savedNewOpActivity);
                newBudgetItem.setBudgetItem(originalBudgetItem.getBudgetItem()); // Retain original BudgetItem

                // newBudgetItem.setCreateTime(LocalDateTime.now());
                newBudgetItem.setMonthAmounts(new EnumMap<>(originalBudgetItem.getMonthAmounts()));
                newBudgetItem.setExpenseType(originalBudgetItem.getExpenseType());

                operationalActivityBudgetItemRepo.save(newBudgetItem);
            }
        }
        return savedNewFormulation;
    }

}