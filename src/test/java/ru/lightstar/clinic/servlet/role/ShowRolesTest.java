package ru.lightstar.clinic.servlet.role;

import org.junit.Test;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.model.Role;
import ru.lightstar.clinic.servlet.ServletTest;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * <code>ShowRoles</code> servlet tests.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class ShowRolesTest extends ServletTest {

    /**
     * Test correctness of <code>doGet</code> method.
     */
    @Test
    public void whenDoGetThenResult() throws ServletException, IOException, ServiceException {
        final List<Role> roles = Arrays.asList(new Role("admin"), new Role("client"));

        when(this.request.getRequestDispatcher("/WEB-INF/view/ShowRoles.jsp")).thenReturn(this.dispatcher);
        when(this.roleService.getAllRoles()).thenReturn(roles);

        new ShowRoles(this.clinicService, this.roleService).doGet(this.request, this.response);

        verify(this.roleService, atLeastOnce()).getAllRoles();
        verify(this.request, atLeastOnce()).setAttribute("roles", roles);
        verify(this.dispatcher, atLeastOnce()).forward(this.request, this.response);
    }
}