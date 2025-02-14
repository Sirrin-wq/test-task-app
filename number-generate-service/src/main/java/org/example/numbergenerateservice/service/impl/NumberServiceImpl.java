package org.example.numbergenerateservice.service.impl;

import com.mongodb.DuplicateKeyException;
import lombok.RequiredArgsConstructor;
import org.example.numbergenerateservice.model.GeneratedNumberEntity;
import org.example.numbergenerateservice.repository.NumberRepository;
import org.example.numbergenerateservice.service.NumberService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NumberServiceImpl implements NumberService {
    private final NumberRepository repository;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.BASIC_ISO_DATE;
    private static final int MAX_RETRIES = 3;

    @Override
    public GeneratedNumberEntity generate() {
        int retries = 0;
        while (retries < MAX_RETRIES) {
            String numberPart = generateUniqueNumber();
            String datePart = LocalDate.now().format(DATE_FORMATTER);
            String fullNumber = numberPart + datePart;

            GeneratedNumberEntity entity = new GeneratedNumberEntity(null, fullNumber, LocalDateTime.now());

            try {
                return repository.save(entity);
            } catch (DuplicateKeyException e) {
                retries++;
                if (retries >= MAX_RETRIES) {
                    throw new RuntimeException("Failed to generate a unique number after " + MAX_RETRIES + " attempts");
                }
            }
        }
        throw new RuntimeException("Failed to generate a unique number");
    }

    private String generateUniqueNumber() {
        return UUID.randomUUID().toString().substring(0, 5).toUpperCase();
    }
}
