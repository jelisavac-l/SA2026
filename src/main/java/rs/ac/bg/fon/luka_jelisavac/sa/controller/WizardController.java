package rs.ac.bg.fon.luka_jelisavac.sa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.bg.fon.luka_jelisavac.sa.domain.Wizard;
import rs.ac.bg.fon.luka_jelisavac.sa.service.WizardService;

import java.util.List;

/**
 * @author Luka Jelisavac (jelisavac-l)
 */

@RestController
@RequestMapping("/api/wizards")
@RequiredArgsConstructor
public class WizardController {

    private final WizardService wizardService;

    @GetMapping("/search")
    public ResponseEntity<List<Wizard>> searchByLastName(@RequestParam String lastName) {
        List<Wizard> results = wizardService.searchByName(lastName);
        return ResponseEntity.ok(results);
    }
}
