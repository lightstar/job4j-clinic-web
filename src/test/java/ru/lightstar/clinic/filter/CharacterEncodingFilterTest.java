package ru.lightstar.clinic.filter;

import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * <code>CharacterEncodingFilter</code> class tests.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class CharacterEncodingFilterTest extends Mockito {

    /**
     * Test correctness of character encoding setting in <code>doFilter</code> method.
     */
    @Test
    public void whenDoFilterThenResult() throws ServletException, IOException {
        final ServletRequest request = mock(ServletRequest.class);
        final ServletResponse response = mock(ServletResponse.class);
        final FilterChain chain = mock(FilterChain.class);

        new CharacterEncodingFilter().doFilter(request, response, chain);

        verify(request, atLeastOnce()).setCharacterEncoding("utf-8");
        verify(chain, atLeastOnce()).doFilter(request, response);
    }
}
