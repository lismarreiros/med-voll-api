package med.voll.api.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import med.voll.api.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
         System.out.println("filtro chamado!");
        // recupera o token
        var tokenJWT = recuperarToken(request);

        // tá vindo token?
        if (tokenJWT != null) {
            // há token, recupera o token no cabeçalho, valida se ele está correto e pega o email/usuário da pessoa que está dentro do token (securityconfiguration)
            var subject = tokenService.getSubject(tokenJWT);
            // carrega esse usuário lá do banco de dados
            var usuario = repository.findByLogin(subject);
            // cria o dto que representa o usuário
            var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
            // e força a autenticação! - spring vai considerar que você está logado.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // se não está vindo, segue o fluxo...
        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }
}
