package ca.gbc.comp3095.wellnessservice.controller;

import ca.gbc.comp3095.wellnessservice.model.Resource;
import ca.gbc.comp3095.wellnessservice.service.ResourceService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resources")
public class ResourceController {

    private final ResourceService service;

    public ResourceController(ResourceService service) {
        this.service = service;
    }

    @GetMapping
    public List<Resource> getAll() {
        System.out.println("Controller hit: /api/resources");
        return service.getAllResources();
    }

    @GetMapping(path = "/ping", produces = "text/plain")
    public @ResponseBody String ping() {
        return "pong";
    }

    @GetMapping("/category/{category}")
    public List<Resource> getByCategory(@PathVariable String category) {
        return service.getByCategory(category);
    }

    @GetMapping("/search")
    public List<Resource> search(@RequestParam String keyword) {
        return service.searchByKeyword(keyword);
    }

    @PostMapping
    @PreAuthorize("hasRole('staff')")
    public Resource create(@RequestBody Resource resource) {
        return service.create(resource);
    }

    @PutMapping("/{id:[0-9]+}")
    @PreAuthorize("hasRole('staff')")
    public Resource update(@PathVariable Long id, @RequestBody Resource resource) {
        resource.setResourceId(id);
        return service.update(resource);
    }

    @DeleteMapping("/{id:[0-9]+}")
    @PreAuthorize("hasRole('staff')")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}