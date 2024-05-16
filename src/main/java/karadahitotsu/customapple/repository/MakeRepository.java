package karadahitotsu.customapple.repository;

import karadahitotsu.customapple.entity.MakeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MakeRepository extends JpaRepository<MakeRequest,Long> {
}
