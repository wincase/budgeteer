package de.adesso.budgeteer.service.record;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.querydsl.core.types.Predicate;
import de.adesso.budgeteer.IntegrationTestTemplate;
import de.adesso.budgeteer.ListUtil;
import de.adesso.budgeteer.persistence.record.WorkRecordEntity;
import de.adesso.budgeteer.persistence.record.WorkRecordRepository;
import de.adesso.budgeteer.service.DateRange;
import de.adesso.budgeteer.service.budget.BudgetBaseData;
import de.adesso.budgeteer.service.person.PersonBaseData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

class WorkRecordQueriesIntegrationTest extends IntegrationTestTemplate {

    private DateFormat format = new SimpleDateFormat("dd.MM.yyyy");

    @Autowired
    private WorkRecordRepository repository;

    @Test
    @DatabaseSetup("findByFilter.xml")
    @DatabaseTearDown(value = "findByFilter.xml", type = DatabaseOperation.DELETE_ALL)
    void testFindByEmptyFilter() throws Exception {
        WorkRecordFilter filter = new WorkRecordFilter(1L);
        Predicate query = WorkRecordQueries.findByFilter(filter);
        List<WorkRecordEntity> records = ListUtil.toArrayList(repository.findAll(query));
        Assertions.assertEquals(4, records.size());
    }

    @Test
    @DatabaseSetup("findByFilter.xml")
    @DatabaseTearDown(value = "findByFilter.xml", type = DatabaseOperation.DELETE_ALL)
    void testFindByPersonFilter() throws Exception {
        WorkRecordFilter filter = new WorkRecordFilter(1L);
        filter.getPersonList().add(new PersonBaseData(1L));
        Predicate query = WorkRecordQueries.findByFilter(filter);
        List<WorkRecordEntity> records = ListUtil.toArrayList(repository.findAll(query));
        Assertions.assertEquals(2, records.size());
    }

    @Test
    @DatabaseSetup("findByFilter.xml")
    @DatabaseTearDown(value = "findByFilter.xml", type = DatabaseOperation.DELETE_ALL)
    void testFindByBudgetFilter() throws Exception {
        WorkRecordFilter filter = new WorkRecordFilter(1L);
        filter.getBudgetList().add(new BudgetBaseData(1L, "budget1"));
        Predicate query = WorkRecordQueries.findByFilter(filter);
        List<WorkRecordEntity> records = ListUtil.toArrayList(repository.findAll(query));
        Assertions.assertEquals(2, records.size());
    }

    @Test
    @DatabaseSetup("findByFilter.xml")
    @DatabaseTearDown(value = "findByFilter.xml", type = DatabaseOperation.DELETE_ALL)
    void testFindByDateFilter() throws Exception {
        WorkRecordFilter filter = new WorkRecordFilter(1L);
        filter.setDateRange(new DateRange(format.parse("01.01.2015"), format.parse("15.08.2015")));
        Predicate query = WorkRecordQueries.findByFilter(filter);
        List<WorkRecordEntity> records = ListUtil.toArrayList(repository.findAll(query));
        Assertions.assertEquals(3, records.size());
    }

    @Test
    @DatabaseSetup("findByFilter.xml")
    @DatabaseTearDown(value = "findByFilter.xml", type = DatabaseOperation.DELETE_ALL)
    void testFindByMixedFilter() throws Exception {
        WorkRecordFilter filter = new WorkRecordFilter(1L);
        filter.getPersonList().add(new PersonBaseData(1L));
        filter.getBudgetList().add(new BudgetBaseData(1L, "budget1"));
        filter.setDateRange(new DateRange(format.parse("01.01.2015"), format.parse("02.01.2015")));
        Predicate query = WorkRecordQueries.findByFilter(filter);
        List<WorkRecordEntity> records = ListUtil.toArrayList(repository.findAll(query));
        Assertions.assertEquals(2, records.size());
    }

}