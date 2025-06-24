package uk.gov.hmcts.reform.dev.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.dev.models.Caseworker;
import uk.gov.hmcts.reform.dev.service.CaseworkerService;

import java.util.List;

@RestController
@RequestMapping("/caseworker")
public class CaseworkerController {

    private final CaseworkerService caseworkerService;

    public CaseworkerController(CaseworkerService caseworkerService) {
        this.caseworkerService = caseworkerService;
    }

    @GetMapping("/all")
    public List<Caseworker> findAll() {
        return caseworkerService.findAll();
    }
}
