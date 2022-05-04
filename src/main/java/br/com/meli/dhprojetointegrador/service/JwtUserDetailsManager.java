package br.com.meli.dhprojetointegrador.service;

import br.com.meli.dhprojetointegrador.entity.Role;
import br.com.meli.dhprojetointegrador.entity.User;
import br.com.meli.dhprojetointegrador.enums.RoleType;
import br.com.meli.dhprojetointegrador.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class JwtUserDetailsManager implements ICustomUserDetailsService<User> {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findById(username).orElseThrow(() ->
                new UsernameNotFoundException(String.format("User with the username %s wasn't found", username)));
        log.debug("User with username {} was successfully found", user.getUsername());
        return user;
    }

    @Override
    public User createUser(UserDetails user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());

        Set<Role> roles = user.getAuthorities()
                .stream()
                .map(grantedAuthority -> Role.builder().role(RoleType.valueOfIgnoreCase(grantedAuthority.getAuthority())).build())
                .collect(Collectors.toSet());

        User userToBePersisted = User.builder()
                .username(user.getUsername())
                .password(encodedPassword)
                .role(roles)
                .build();

        User persistedUser = userRepository.save(userToBePersisted);
        log.debug("User with username {} was successfully persisted", persistedUser.getUsername());
        return persistedUser;
    }
}
