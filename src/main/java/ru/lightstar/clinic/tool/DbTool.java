package ru.lightstar.clinic.tool;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.lightstar.clinic.drug.Drug;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.model.Client;
import ru.lightstar.clinic.model.Message;
import ru.lightstar.clinic.model.Role;
import ru.lightstar.clinic.persistence.MessageService;
import ru.lightstar.clinic.persistence.PersistentClinicService;
import ru.lightstar.clinic.persistence.PersistentDrugService;
import ru.lightstar.clinic.persistence.RoleService;

import java.util.Map;

/**
 * Class used to manipulate clinic data using spring.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class DbTool {

    /**
     * Entry point.
     *
     * @param args not used.
     */
    public static void main(final String[] args) throws ServiceException {
        final ApplicationContext context = new ClassPathXmlApplicationContext("spring-context.xml");

        final PersistentClinicService clinicService = context.getBean(PersistentClinicService.class);
        final PersistentDrugService drugService = context.getBean(PersistentDrugService.class);

        final RoleService roleService = context.getBean(RoleService.class);
        final MessageService messageService = context.getBean(MessageService.class);

        System.out.println("Roles:");
        for (final Role role : roleService.getAllRoles()) {
            System.out.println(role.getName());
        }

        System.out.println();
        System.out.println("Clients:");
        for (final Client client : clinicService.getAllClients()) {
            if (client != null) {
                System.out.println(String.format("%s, email: %s, phone: %s, role: %s.", client, client.getEmail(),
                        client.getPhone(), client.getRole().getName()));
                for (final Message message : messageService.getClientMessages(client)) {
                    System.out.println(String.format("    %s", message.getText()));
                }
            }
        }

        System.out.println();
        System.out.println("Drugs:");
        for (final Map.Entry<Drug, Integer> drugEntry : drugService.getAllDrugs().entrySet()) {
            System.out.println(String.format("%s - %d", drugEntry.getKey(), drugEntry.getValue()));
        }

    }
}
