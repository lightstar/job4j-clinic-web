package ru.lightstar.clinic.persistence;

import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.model.Role;

import java.util.List;

/**
 * Service operating on roles.
 *
 * @author LightStar
 * @since 0.0.1
 */
public interface RoleService {

    /**
     * Get all roles from database.
     *
     * @return list of all roles.
     */
    List<Role> getAllRoles();

    /**
     * Get role by given name or null if it doesn't exists.
     *
     * @param name role's name.
     * @return role or null if it doesn't exists.
     */
    Role getRoleByName(final String name) throws ServiceException;

    /**
     * Add new role.
     *
     * @param name role's name.
     */
    void addRole(final String name) throws ServiceException;

    /**
     * Delete role.
     *
     * @param name role's name.
     */
    void deleteRole(final String name) throws ServiceException;
}
