package ca.gbc.comp3095.wellnessservice.repository;

import ca.gbc.comp3095.wellnessservice.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
    List<Resource> findByCategory(String category);
    List<Resource> findByTitleContainingIgnoreCase(String keyword);
}