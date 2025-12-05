package ca.gbc.comp3095.wellnessservice.service;

import ca.gbc.comp3095.wellnessservice.model.Resource;
import ca.gbc.comp3095.wellnessservice.repository.ResourceRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceService {
    private final ResourceRepository repository;

    public ResourceService(ResourceRepository repository) {
        this.repository = repository;
    }

    @Cacheable("resources")
    public List<Resource> getAllResources() {
        System.out.println("Service hit: getAllResources()");
        try {
            List<Resource> result = repository.findAll();
            System.out.println("Repository returned " + result.size() + " resources.");
            return result;
        } catch (Exception e) {
            System.out.println("Exception in getAllResources(): " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Cacheable(value = "resourcesByCategory", key = "#category")
    public List<Resource> getByCategory(String category) {
        return repository.findByCategory(category);
    }

    @Cacheable(value = "resourcesByKeyword", key = "#keyword")
    public List<Resource> searchByKeyword(String keyword) {
        return repository.findByTitleContainingIgnoreCase(keyword);
    }

    @CacheEvict(value = {"resources", "resourcesByCategory", "resourcesByKeyword"}, allEntries = true)
    public Resource create(Resource resource) {
        return repository.save(resource);
    }

    @CachePut(value = "resources", key = "#resource.resourceId")
    public Resource update(Resource resource) {
        return repository.save(resource);
    }

    @CacheEvict(value = {"resources", "resourcesByCategory", "resourcesByKeyword"}, allEntries = true)
    public void delete(Long id) {
        repository.deleteById(id);
    }
}