package org.wickedsource.budgeteer.web.pages.invoice.edit.form;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.wickedsource.budgeteer.service.contract.DynamicAttributeField;
import org.wickedsource.budgeteer.service.invoice.InvoiceBaseData;
import org.wickedsource.budgeteer.service.invoice.InvoiceService;
import org.wickedsource.budgeteer.web.components.customFeedback.CustomFeedbackPanel;
import org.wickedsource.budgeteer.web.components.money.MoneyTextField;

import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

public class EditInvoiceForm  extends Form<InvoiceBaseData> {

    @SpringBean
    private InvoiceService service;

    private String nameOfAttribute = "";

    private WebMarkupContainer table;

    private TextField<String> newAttributeField;
    private CustomFeedbackPanel feedbackPanel;

    public EditInvoiceForm(String id, final long contractId){
        super(id);
        super.setDefaultModel(Model.of(service.getEmptyInvoiceModel(contractId)));
        addComponents();
    }


    public EditInvoiceForm(String id, IModel<InvoiceBaseData> model) {
        super(id, model);
        Injector.get().inject(this);
        addComponents();
    }

    private void addComponents() {
        feedbackPanel = new CustomFeedbackPanel("feedback");
        feedbackPanel.setOutputMarkupId(true);
        add(feedbackPanel);

        TextField<String> invoiceNameTextfield = new TextField<String>("invoiceName", model(from(getModelObject()).getInvoiceName()));
        invoiceNameTextfield.setRequired(true);
        add(invoiceNameTextfield);

        TextField<String> nameTextfield = new TextField<String>("contractName", model(from(getModelObject()).getContractName()));
        nameTextfield.setEnabled(false);
        add(nameTextfield);

        TextField<String> internalNumberTextfield = new TextField<String>("internalNumber", model(from(getModelObject()).getInternalNumber()));
        internalNumberTextfield.setRequired(true);
        add(internalNumberTextfield);

        MoneyTextField budgetTextfield = new MoneyTextField("sum", model(from(getModelObject()).getSum()));
        budgetTextfield.setRequired(true);
        add(budgetTextfield);

        add(new NumberTextField<Integer>("year", model(from(getModelObject()).getYear())));
        add(new NumberTextField<Integer>("month", model(from(getModelObject()).getMonth())));

        add(new CheckBox("paid", model(from(getModelObject()).isPaid())));

        table = new WebMarkupContainer("attributeTable");
        table.setOutputMarkupId(true);
        table.setOutputMarkupPlaceholderTag(true);
        table.add(new ListView<DynamicAttributeField>("invoiceAttributes", model(from(getModelObject()).getInvoiceAttributes())) {
            @Override
            protected void populateItem(ListItem<DynamicAttributeField> item) {
                item.add(new Label("attributeTitle", item.getModelObject().getName()));
                item.add(new TextField<String>("attributeValue", model(from(item.getModelObject()).getValue())));
            }
        });
        add(table);
        newAttributeField = new TextField<String>("nameOfNewAttribute", Model.of(nameOfAttribute));
        newAttributeField.setOutputMarkupId(true);
        add(newAttributeField);
        AjaxButton addAttributeButton = new AjaxButton("addAttribute") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                if (newAttributeField.getModelObject() != null) {
                    ((InvoiceBaseData) form.getModelObject()).getInvoiceAttributes().add(new DynamicAttributeField(newAttributeField.getModelObject(), ""));
                    target.add(table, newAttributeField, feedbackPanel);
                } else {
                    this.error(getString("feedback.error.nameEmpty"));
                    target.add(feedbackPanel);
                }
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(feedbackPanel);
            }
        };
        add(addAttributeButton);
        add(new AjaxButton("save") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                try {
                    service.save((InvoiceBaseData) form.getModelObject());
                    this.success(getString("feedback.success"));
                } catch(Exception e){
                    e.printStackTrace();
                    this.error(getString("feedback.error"));
                }
                target.add(feedbackPanel);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(feedbackPanel);
            }
        });
    }
}