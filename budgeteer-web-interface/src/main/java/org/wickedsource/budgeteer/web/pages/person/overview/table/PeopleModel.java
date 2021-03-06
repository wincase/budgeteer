package org.wickedsource.budgeteer.web.pages.person.overview.table;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.person.PersonBaseData;
import org.wickedsource.budgeteer.service.person.PersonService;

import java.util.List;

public class PeopleModel extends LoadableDetachableModel<List<PersonBaseData>> {

    @SpringBean
    private PersonService service;

    private long projectId;

    public PeopleModel(long projectId) {
        this.projectId = projectId;
        Injector.get().inject(this);
    }

    @Override
    protected List<PersonBaseData> load() {
        return service.loadPeopleBaseData(this.projectId);
    }
}
