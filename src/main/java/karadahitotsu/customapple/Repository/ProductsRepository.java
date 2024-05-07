package karadahitotsu.customapple.repository;

import karadahitotsu.customapple.entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductsRepository extends JpaRepository<Products,Long> {
    List<Products> findByModelIn(String[] model);
    List<Products> findByCategory(String category);
}
