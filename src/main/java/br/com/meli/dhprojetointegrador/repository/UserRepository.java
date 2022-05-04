package br.com.meli.dhprojetointegrador.repository;

import br.com.meli.dhprojetointegrador.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
