package uk.gov.hmcts.reform.dev.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.dev.entity.CaseworkerEntity;
import uk.gov.hmcts.reform.dev.models.Caseworker;
import uk.gov.hmcts.reform.dev.repository.CaseworkerRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CaseworkerService {
    private final CaseworkerRepository caseworkerRepository;

    public List<Caseworker> findAll() {
        return caseworkerRepository.findAll()
            .stream().map(this::toCaseWorker)
            .collect(Collectors.toList());
    }

    private Caseworker toCaseWorker(CaseworkerEntity caseworkerEntity) {
        return new Caseworker(caseworkerEntity.getId(), caseworkerEntity.getName());
    }
}
