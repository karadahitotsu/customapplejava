package karadahitotsu.customapple.repository;

import karadahitotsu.customapple.entity.Cart;
import karadahitotsu.customapple.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository  extends JpaRepository<Cart,Long> {
    List<Cart> findByUserid(Users userid);
}
