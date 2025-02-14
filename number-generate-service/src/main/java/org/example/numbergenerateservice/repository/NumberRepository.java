package org.example.numbergenerateservice.repository;

import org.example.numbergenerateservice.model.GeneratedNumberEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NumberRepository extends MongoRepository<GeneratedNumberEntity, String> {
}
