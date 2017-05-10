package ru.lightstar.clinic.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import ru.lightstar.clinic.ClinicService;
import ru.lightstar.clinic.exception.ServiceException;
import ru.lightstar.clinic.model.Client;

import java.util.Collections;
import java.util.List;

/**
 * Authentication provider for spring security.
 *
 * @author LightStar
 * @since 0.0.1
 */
@Service("authentication-provider")
public class ClinicAuthenticationProvider implements AuthenticationProvider {

    private final ClinicService clinicService;

    @Autowired
    public ClinicAuthenticationProvider(final ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        final String login = authentication.getName();
        final String password = SecurityUtil.getHashedPassword(authentication.getCredentials().toString());

        try {
            final Client client = clinicService.findClientByName(login);

            if (!client.getPassword().equals(password)) {
                return null;
            }

            final String roleName = client.getRole().getName();
            final GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_" + roleName.toUpperCase());
            final List<GrantedAuthority> grantedAuthorities = Collections.singletonList(grantedAuthority);

            return new UsernamePasswordAuthenticationToken(login, password, grantedAuthorities);
        } catch (ServiceException e) {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(final Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
