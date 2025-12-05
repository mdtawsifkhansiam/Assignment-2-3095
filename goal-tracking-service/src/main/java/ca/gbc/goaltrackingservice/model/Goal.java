package ca.gbc.goaltrackingservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "goals")
public class Goal {
    @Id
    private String id;
    private String title;
    private String description;
    private LocalDate targetDate;
    private String status;
    private String category;
}