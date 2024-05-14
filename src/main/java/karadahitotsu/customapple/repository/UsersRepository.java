package karadahitotsu.customapple.repository;

import karadahitotsu.customapple.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<Users,Long> {
    List<Users> findByLoginAndPassword(String login, String password);
}
