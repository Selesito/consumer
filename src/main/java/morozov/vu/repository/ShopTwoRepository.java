package morozov.vu.repository;

import morozov.vu.domain.ShopTwo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ShopTwo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShopTwoRepository extends JpaRepository<ShopTwo, Long> {}
