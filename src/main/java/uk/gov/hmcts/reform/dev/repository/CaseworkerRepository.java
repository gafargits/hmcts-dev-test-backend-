package uk.gov.hmcts.reform.dev.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.gov.hmcts.reform.dev.entity.CaseworkerEntity;

@Repository
public interface CaseworkerRepository extends JpaRepository<CaseworkerEntity, Integer> {
}
