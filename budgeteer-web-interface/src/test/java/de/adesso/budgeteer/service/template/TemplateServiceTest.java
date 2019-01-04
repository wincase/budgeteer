package de.adesso.budgeteer.service.template;

import de.adesso.budgeteer.imports.api.ImportFile;
import de.adesso.budgeteer.persistence.template.TemplateEntity;
import de.adesso.budgeteer.persistence.template.TemplateRepository;
import de.adesso.budgeteer.service.ReportType;
import de.adesso.budgeteer.service.ServiceTestTemplate;
import de.adesso.budgeteer.web.pages.templates.templateimport.TemplateFormInputDto;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.times;
import static org.wicketstuff.lazymodel.LazyModel.from;
import static org.wicketstuff.lazymodel.LazyModel.model;

class TemplateServiceTest extends ServiceTestTemplate {

    @Autowired
    TemplateService templateService;

    @Autowired
    TemplateRepository templateRepository;

    @Test
    void doImportTest() {
        Mockito.when(templateRepository.save(any(TemplateEntity.class))).thenReturn(new TemplateEntity());
        TemplateFormInputDto testDto = new TemplateFormInputDto(1);
        testDto.setName("TEST");
        testDto.setDescription("TEST_D");
        testDto.setType(ReportType.BUDGET_REPORT);
        testDto.setDefault(false);
        templateService.doImport(1, new ImportFile("exampleTemplate1.xlsx",
                        getClass().getResourceAsStream("exampleTemplate1.xlsx")),
                        model(from(testDto)));
        Mockito.verify(templateRepository, times(1)).save(any(TemplateEntity.class));
    }

    @Test
    void editTemplateTest() {
        Mockito.when(templateRepository.save(any(TemplateEntity.class))).thenReturn(new TemplateEntity());

        TemplateFormInputDto testDto = new TemplateFormInputDto(1);
        testDto.setName("TEST");
        testDto.setDescription("TEST_D");
        testDto.setType(ReportType.BUDGET_REPORT);
        testDto.setDefault(true);
        templateService.editTemplate(1, 1,
                new ImportFile("exampleTemplate1.xlsx",
                        getClass().getResourceAsStream("exampleTemplate1.xlsx")), model(from(testDto)));
        Mockito.verify(templateRepository, times(1)).save(any(TemplateEntity.class));
    }

    @Test
    void deleteTemplateTest() {
        templateService.deleteTemplate(new TemplateEntity().getId());
        Mockito.verify(templateRepository, times(1)).delete(anyLong());
    }

    @Test
    void getExampleFileTest(){
        try {
            XSSFWorkbook testWorkbok = (XSSFWorkbook)WorkbookFactory.create(templateService.getExampleFile(ReportType.CONTRACT_REPORT).getInputStream());
            Assertions.assertNotNull(testWorkbok);
        }catch (IOException | InvalidFormatException e){
            e.printStackTrace();
            Assertions.fail();
        }
    }
}
