package ru.lightstar.clinic.tool;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.exception.NameException;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.persistence.RoleService;
import ru.lightstar.clinic.security.SecurityUtil;

/**
 * Class used to populate database with initial data using spring.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class InitTool {

    /**
     * Entry point.
     *
     * @param args not used.
     */
    public static void main(final String[] args) throws ServiceException, NameException {
        final ApplicationContext context = new ClassPathXmlApplicationContext("spring-context.xml");
        final ClinicService clinicService = context.getBean(ClinicService.class);
        final RoleService roleService = context.getBean(RoleService.class);

        roleService.addRole("admin");
        roleService.addRole("manager");
        roleService.addRole("client");

        clinicService.addClient(0, "Admin", "", "", roleService.getRoleByName("admin"),
                SecurityUtil.getHashedPassword("qwerty"));
    }
}
