package br.com.user.service.auth.service;

import br.com.user.service.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service to bridge Spring Security and the User Repository.
 */
@Service
@RequiredArgsConstructor
public class AuthorizationService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String combinedUsername) throws UsernameNotFoundException {
        // O username agora é uma combinação de login::email
        String[] parts = combinedUsername.split("::");
        if (parts.length != 2) {
            throw new UsernameNotFoundException("Invalid username format. Expected 'login::email'.");
        }
        String login = parts[0];
        String email = parts[1];

        // Busca o usuário pela combinação de login e email
        var user = userRepository.findByLoginAndEmail(login, email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with login: " + login + " and email: " + email));

        // Retorna UserDetails usando o email como principal para o Spring Security
        return User
                .withUsername(user.getEmail()) // Usamos o email como o "username" principal para o Spring Security
                .password(user.getPassword())
                .roles(user.getType())
                .build();
    }
}
