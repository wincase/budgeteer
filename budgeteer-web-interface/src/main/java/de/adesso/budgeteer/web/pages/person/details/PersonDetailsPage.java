package de.adesso.budgeteer.web.pages.person.details;

import de.adesso.budgeteer.service.person.PersonService;
import de.adesso.budgeteer.web.Mount;
import de.adesso.budgeteer.web.components.confirm.ConfirmationForm;
import de.adesso.budgeteer.web.pages.base.basepage.BasePage;
import de.adesso.budgeteer.web.pages.base.basepage.breadcrumbs.BreadcrumbsModel;
import de.adesso.budgeteer.web.pages.base.delete.DeleteDialog;
import de.adesso.budgeteer.web.pages.dashboard.DashboardPage;
import de.adesso.budgeteer.web.pages.person.details.chart.BudgetDistributionChart;
import de.adesso.budgeteer.web.pages.person.details.chart.BudgetDistributionChartModel;
import de.adesso.budgeteer.web.pages.person.details.highlights.PersonHighlightsModel;
import de.adesso.budgeteer.web.pages.person.details.highlights.PersonHighlightsPanel;
import de.adesso.budgeteer.web.pages.person.edit.EditPersonPage;
import de.adesso.budgeteer.web.pages.person.hours.PersonHoursPage;
import de.adesso.budgeteer.web.pages.person.monthreport.PersonMonthReportPage;
import de.adesso.budgeteer.web.pages.person.overview.PeopleOverviewPage;
import de.adesso.budgeteer.web.pages.person.weekreport.PersonWeekReportPage;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

@Mount("people/details/${id}")
public class PersonDetailsPage extends BasePage {

    @SpringBean
    private PersonService personService;

    public PersonDetailsPage(PageParameters parameters) {
        super(parameters);
        add(new PersonHighlightsPanel("highlightsPanel", new PersonHighlightsModel(getParameterId())));
        add(new BudgetDistributionChart("distributionChart", new BudgetDistributionChartModel(getParameterId())));
        add(createEditPersonLink("editPersonLink"));
        add(new BookmarkablePageLink<PersonWeekReportPage>("weekReportLink", PersonWeekReportPage.class, PersonWeekReportPage.createParameters(getParameterId())));
        add(new BookmarkablePageLink<PersonWeekReportPage>("monthReportLink", PersonMonthReportPage.class, PersonMonthReportPage.createParameters(getParameterId())));
        add(new BookmarkablePageLink<PersonWeekReportPage>("hoursLink", PersonHoursPage.class, PersonHoursPage.createParameters(getParameterId())));

        Form deleteForm = new ConfirmationForm("deleteForm", this, "confirmation.delete") {
            @Override
            public void onSubmit() {

                setResponsePage(new DeleteDialog() {
                    @Override
                    protected void onYes() {
                        personService.deletePerson(getParameterId());
                        setResponsePage(PeopleOverviewPage.class);
                    }

                    @Override
                    protected void onNo() {
                        setResponsePage(PersonDetailsPage.class, getPageParameters());
                    }

                    @Override
                    protected String confirmationText() {
                        return PersonDetailsPage.this.getString("confirmation.delete");
                    }
                });
            }
        };
        deleteForm.add(new SubmitLink("deletePersonLink"));
        add(deleteForm);
    }

    private Link createEditPersonLink(String id) {
        return new Link(id) {
            @Override
            public void onClick() {
                WebPage page = new EditPersonPage(EditPersonPage.createParameters(getParameterId()), PersonDetailsPage.class, getPageParameters());
                setResponsePage(page);
            }
        };
    }

    @Override
    @SuppressWarnings("unchecked")
    protected BreadcrumbsModel getBreadcrumbsModel() {
        BreadcrumbsModel model = new BreadcrumbsModel(DashboardPage.class, PeopleOverviewPage.class);
        model.addBreadcrumb(PersonDetailsPage.class, getPageParameters());
        return model;
    }

}