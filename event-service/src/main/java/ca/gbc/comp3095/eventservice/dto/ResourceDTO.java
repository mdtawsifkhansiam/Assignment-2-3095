package ca.gbc.comp3095.eventservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceDTO {
    private String id;
    private String name;
    private String type;
    private String description;
}