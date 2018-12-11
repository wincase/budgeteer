package org.wickedsource.budgeteer.web.pages.user.edituser;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.wickedsource.budgeteer.service.project.ProjectBaseData;
import org.wickedsource.budgeteer.service.project.ProjectService;
import org.wickedsource.budgeteer.service.user.EditUserData;
import org.wickedsource.budgeteer.service.user.UserService;
import org.wickedsource.budgeteer.web.AbstractWebTestTemplate;
import org.wickedsource.budgeteer.web.pages.dashboard.DashboardPage;
import org.wickedsource.budgeteer.web.pages.user.edit.EditUserPage;

import java.util.Date;

import static org.mockito.Mockito.when;

public class EditUserPageTest extends AbstractWebTestTemplate {

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @BeforeEach
    void setUpMocks() {
        when(userService.loadUserToEdit(1L)).thenReturn(new EditUserData(1L, "test", "test@budgeteer.local", "password", null, null, new ProjectBaseData(), new Date()));
        ProjectBaseData projectBaseData = new ProjectBaseData();
        projectBaseData.setId(1L);
        projectBaseData.setName("project1");
        when(projectService.getDefaultProjectForUser(1L)).thenReturn(projectBaseData);
    }

    @Test
    void test() {
        WicketTester tester = getTester();
        EditUserPage page = new EditUserPage(DashboardPage.class, new PageParameters().add("userId", 1L));
        tester.startPage(page);
        tester.assertRenderedPage(EditUserPage.class);
    }

    @Override
    protected void setupTest() {
    }
}
