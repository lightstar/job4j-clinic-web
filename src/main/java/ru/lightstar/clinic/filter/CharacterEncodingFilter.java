package ru.lightstar.clinic.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * Filter to set request body's character encoding.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class CharacterEncodingFilter implements Filter {

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(final FilterConfig config) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        chain.doFilter(request, response);
    }
}
