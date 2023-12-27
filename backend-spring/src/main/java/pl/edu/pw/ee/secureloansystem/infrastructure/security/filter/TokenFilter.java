package pl.edu.pw.ee.secureloansystem.infrastructure.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.edu.pw.ee.secureloansystem.domain.user.entity.User;
import pl.edu.pw.ee.secureloansystem.domain.user.mapper.UserMapper;
import pl.edu.pw.ee.secureloansystem.infrastructure.security.utils.TokenUtils;

@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenFilter extends OncePerRequestFilter {

  final TokenUtils tokenUtils;
  final UserDetailsService userDetailsService;
  final UserMapper userMapper;

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    @NonNull HttpServletResponse response,
    @NonNull FilterChain filterChain
  ) throws ServletException, IOException {
    final String authHeaderPrefix = "Bearer ";
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (authHeader != null && authHeader.startsWith(authHeaderPrefix)) {
      final String token = authHeader.substring(authHeaderPrefix.length());
      if (tokenUtils.isTokenExpired(token)) {
        log.debug("Token expired");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().write("Token expired");
        return;
      }
      final String email = tokenUtils.extractEmail(token);
      final User loadedUser = (User) userDetailsService.loadUserByUsername(
        email
      );

      final UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(
          userMapper.getAuthUser(loadedUser),
          null,
          loadedUser.getAuthorities()
        );

      SecurityContextHolder.getContext().setAuthentication(authenticationToken);
      log.debug("Token verified");
    } else {
      log.debug("Token not found");
    }
    filterChain.doFilter(request, response);
  }
}
