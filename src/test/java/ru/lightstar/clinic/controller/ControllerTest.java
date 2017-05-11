package ru.lightstar.clinic.controller;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.DrugService;
import ru.lightstar.clinic.persistence.MessageService;
import ru.lightstar.clinic.persistence.RoleService;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

/**
 * Base class for controller tests.
 *
 * @author LightStar
 * @since 0.0.1
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-test-context.xml", "classpath:spring-web-context.xml", "classpath:spring-security.xml"})
@WebAppConfiguration
public abstract class ControllerTest extends Mockito {

    /**
     * Mock spring MVC runner.
     */
    protected MockMvc mockMvc;

    /**
     * Mocked clinic service.
     */
    @Autowired
    protected ClinicService mockClinicService;

    /**
     * Mocked drug service.
     */
    @Autowired
    protected DrugService mockDrugService;

    /**
     * Mocked role service.
     */
    @Autowired
    protected RoleService mockRoleService;

    /**
     * Mocked message service.
     */
    @Autowired
    protected MessageService mockMessageService;

    /**
     * Application context used for mvc tests.
     */
    @Autowired
    private WebApplicationContext webApplicationContext;

    /**
     * Setting up test environment.
     */
    @Before
    public void setUp() {
        reset(this.mockClinicService);
        reset(this.mockDrugService);
        reset(this.mockRoleService);
        reset(this.mockMessageService);
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.webApplicationContext)
                .apply(springSecurity())
                .build();
    }
}
