package org.example.numbergenerateservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;


import java.time.LocalDateTime;

@Document(collection = "generated_numbers")
@CompoundIndex(name = "fullNumber_unique", def = "{'fullNumber': 1}", unique = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeneratedNumberEntity {
    @Id
    private String id;
    private String fullNumber;
    private LocalDateTime createdAt;
}
