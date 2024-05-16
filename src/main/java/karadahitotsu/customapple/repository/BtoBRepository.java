package karadahitotsu.customapple.repository;

import karadahitotsu.customapple.entity.BToBRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BtoBRepository extends JpaRepository<BToBRequest,Long> {
}
