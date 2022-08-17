package ua.goit.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ProjectDto {

    private String name;
    private String date;
    private Integer quantityDeveloper;

    @Override
    public String toString() {
        return "Project{" +
                "name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", quantityDeveloper=" + quantityDeveloper +
                '}';
    }
}
