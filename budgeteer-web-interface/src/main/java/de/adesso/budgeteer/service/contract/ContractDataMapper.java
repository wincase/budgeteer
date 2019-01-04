package de.adesso.budgeteer.service.contract;


import de.adesso.budgeteer.MoneyUtil;
import de.adesso.budgeteer.persistence.budget.BudgetEntity;
import de.adesso.budgeteer.persistence.contract.ContractEntity;
import de.adesso.budgeteer.persistence.contract.ContractFieldEntity;
import de.adesso.budgeteer.persistence.contract.ContractRepository;
import de.adesso.budgeteer.persistence.invoice.InvoiceEntity;
import de.adesso.budgeteer.persistence.manualRecord.ManualRecordRepository;
import de.adesso.budgeteer.persistence.project.ProjectContractField;
import de.adesso.budgeteer.service.AbstractMapper;
import de.adesso.budgeteer.service.budget.BudgetBaseData;
import de.adesso.budgeteer.service.invoice.InvoiceDataMapper;
import de.adesso.budgeteer.web.components.fileUpload.FileUploadModel;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ContractDataMapper extends AbstractMapper<ContractEntity, ContractBaseData> {

    @Autowired
    private InvoiceDataMapper invoiceDataMapper;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private ManualRecordRepository manualRecordRepository;

    @Override
    public ContractBaseData map(ContractEntity entity) {
        if (entity == null) {
            return null;
        }
        ContractBaseData result = new ContractBaseData();
        result.setContractName(entity.getName());
        result.setContractId(entity.getId());
        result.setSortingIndex(0);
        result.setBudget(entity.getBudget());
        result.setBudgetLeft(toMoneyNullsafe(contractRepository.getBudgetLeftByContractId(entity.getId())));
        result.setBudgetSpent(toMoneyNullsafe(contractRepository.getSpentBudgetByContractId(entity.getId())));
        result.setInternalNumber(entity.getInternalNumber());
        result.setProjectId(entity.getProject().getId());
        result.setType(entity.getType());
        result.setStartDate(entity.getStartDate());
        result.setFileModel(new FileUploadModel(entity.getFileName(), entity.getFile(), entity.getLink()));
        result.setTaxRate(entity.getTaxRate() == null ? 0.0 : entity.getTaxRate().doubleValue());

        Map<String, DynamicAttributeField> contractAttributes = new HashMap<>();
        for (ProjectContractField projectContractField : entity.getProject().getContractFields()) {
            contractAttributes.put(projectContractField.getFieldName(), new DynamicAttributeField(projectContractField.getFieldName(), ""));
        }
        for (ContractFieldEntity fieldEntity : entity.getContractFields()) {
            contractAttributes.put(fieldEntity.getField().getFieldName(), new DynamicAttributeField(fieldEntity.getField().getFieldName(), fieldEntity.getValue()));
        }
        result.setContractAttributes(new ArrayList<>(contractAttributes.values()));

        result.setBelongingBudgets(new LinkedList<>());
        for (BudgetEntity budgetEntity : entity.getBudgets()) {
            result.getBelongingBudgets().add(new BudgetBaseData(budgetEntity.getId(), budgetEntity.getName()));
        }

        result.setBelongingInvoices(new LinkedList<>());
        for (InvoiceEntity invoiceEntity : entity.getInvoices()) {
            result.getBelongingInvoices().add(invoiceDataMapper.map(invoiceEntity));
        }

        return result;
    }

    public List<ContractBaseData> map(List<ContractEntity> entityList) {
        List<ContractBaseData> result = new LinkedList<>();
        for (ContractEntity entity : entityList) {
            result.add(map(entity));
        }
        return result;
    }

    private Money toMoneyNullsafe(Double cents) {
        if (cents == null) {
            return MoneyUtil.createMoneyFromCents(0L);
        } else {
            return MoneyUtil.createMoneyFromCents(Math.round(cents));
        }
    }
}