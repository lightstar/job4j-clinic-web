package ru.lightstar.clinic.controller;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import ru.lightstar.clinic.security.SecurityUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Spring mvc interceptor for all controllers.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class ClinicInterceptor extends HandlerInterceptorAdapter {

    /**
     * {@inheritDoc}
     */
    @Override
    public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler,
                           final ModelAndView modelAndView)
            throws Exception {
        request.setAttribute("me", SecurityUtil.getAuthName());
    }
}
