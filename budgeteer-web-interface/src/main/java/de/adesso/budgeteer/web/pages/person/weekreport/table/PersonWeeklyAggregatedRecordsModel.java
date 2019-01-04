package de.adesso.budgeteer.web.pages.person.weekreport.table;

import de.adesso.budgeteer.service.record.AggregatedRecord;
import de.adesso.budgeteer.service.record.RecordService;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class PersonWeeklyAggregatedRecordsModel extends LoadableDetachableModel<List<AggregatedRecord>> {

    @SpringBean
    private RecordService service;

    private long personId;

    public PersonWeeklyAggregatedRecordsModel(long personId) {
        Injector.get().inject(this);
        this.personId = personId;
    }

    @Override
    protected List<AggregatedRecord> load() {
        return service.getWeeklyAggregationForPerson(personId);
    }
}