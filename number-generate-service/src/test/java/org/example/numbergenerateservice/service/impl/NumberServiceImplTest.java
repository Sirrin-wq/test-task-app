package org.example.numbergenerateservice.service.impl;

import com.mongodb.DuplicateKeyException;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcernResult;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.example.numbergenerateservice.model.GeneratedNumberEntity;
import org.example.numbergenerateservice.repository.NumberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NumberServiceImplTest {
    @Mock
    private NumberRepository repository;
    @InjectMocks
    private NumberServiceImpl service;

    @Test
    void testGenerateUniqueNumber() {
        GeneratedNumberEntity mockEntity = new GeneratedNumberEntity("1", "1111120241212", LocalDateTime.now());
        when(repository.save(any())).thenReturn(mockEntity);

        GeneratedNumberEntity result = service.generate();
        assertNotNull(result);
        assertEquals(5, result.getFullNumber().substring(0, 5).length());
    }

    @Test
    void testGenerateUniqueNumberWithRetries() {
        GeneratedNumberEntity mockEntity = new GeneratedNumberEntity("1", "1111120241212", LocalDateTime.now());
        BsonDocument bsonDocument = new BsonDocument();
        bsonDocument.put("key", new BsonString("dummy"));
        ServerAddress serverAddress = new ServerAddress("localhost", 27017);
        WriteConcernResult writeConcernResult = mock(WriteConcernResult.class);

        when(repository.save(any()))
                .thenThrow(new DuplicateKeyException(bsonDocument, serverAddress, writeConcernResult))
                .thenReturn(mockEntity);

        GeneratedNumberEntity result = service.generate();

        assertNotNull(result);
        assertEquals(5, result.getFullNumber().substring(0, 5).length());
    }

    @Test
    void testGenerateUniqueNumberThrowsRuntimeExceptionAfterMaxRetries() {
        BsonDocument bsonDocument = new BsonDocument();
        bsonDocument.put("key", new BsonString("dummy"));
        ServerAddress serverAddress = new ServerAddress("localhost", 27017);
        WriteConcernResult writeConcernResult = mock(WriteConcernResult.class);

        when(repository.save(any()))
                .thenThrow(new DuplicateKeyException(bsonDocument, serverAddress, writeConcernResult))
                .thenThrow(new DuplicateKeyException(bsonDocument, serverAddress, writeConcernResult))
                .thenThrow(new DuplicateKeyException(bsonDocument, serverAddress, writeConcernResult));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.generate());

        assertEquals("Failed to generate a unique number after 3 attempts", exception.getMessage());

        verify(repository, times(3)).save(any());
    }
}
