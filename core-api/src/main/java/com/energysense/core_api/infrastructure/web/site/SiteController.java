package com.energysense.core_api.infrastructure.web.site;

import com.energysense.core_api.application.port.in.CreateSiteUseCase;
import com.energysense.core_api.application.port.in.GetSitesUseCase;
import com.energysense.core_api.domain.model.Site;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/sites")
public class SiteController {

    private final CreateSiteUseCase createSiteUseCase;
    private final GetSitesUseCase getSitesUseCase;

    public SiteController(CreateSiteUseCase createSiteUseCase, GetSitesUseCase getSitesUseCase) {
        this.createSiteUseCase = createSiteUseCase;
        this.getSitesUseCase = getSitesUseCase;
    }

    @PostMapping
    public ResponseEntity<SiteResponse> createSite(@RequestBody SiteRequest request) {
        // TODO: remplacer par l'UUID de l'utilisateur authentifié (extrait du JWT)
        UUID ownerId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        Site site = createSiteUseCase.createSite(request.getName(), request.getLocation(), ownerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(SiteResponse.from(site));
    }

    @GetMapping
    public ResponseEntity<List<SiteResponse>> getSites() {
        // TODO: remplacer par l'UUID de l'utilisateur authentifié (extrait du JWT)
        UUID ownerId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        List<SiteResponse> sites = getSitesUseCase.getSitesByOwner(ownerId)
                .stream()
                .map(SiteResponse::from)
                .toList();
        return ResponseEntity.ok(sites);
    }
}