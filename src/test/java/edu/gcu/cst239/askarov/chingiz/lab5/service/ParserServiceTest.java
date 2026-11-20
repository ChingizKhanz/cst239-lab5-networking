package edu.gcu.cst239.askarov.chingiz.lab5.service;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.gcu.cst239.askarov.chingiz.lab5.model.Person;

class ParserServiceTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private DataService dataService;
    private ParserService parserService;

    @BeforeEach
    void setUp() {
        ArrayList<Person> people = new ArrayList<>();
        people.add(new Person(1, "Alice", 25));
        people.add(new Person(2, "Bob", 30));
        people.add(new Person(3, "Charlie", 35));
        dataService = new DataService(people);
        parserService = new ParserService(dataService);
    }

    @Test
    void allReturnsEveryPerson() throws Exception {
        JsonNode result = objectMapper.readTree(parserService.processMessage("ALL"));
        assertEquals(3, result.size());
    }

    @Test
    void getReturnsRequestedPerson() throws Exception {
        JsonNode result = objectMapper.readTree(parserService.processMessage("GET 2"));
        assertEquals(1, result.size());
        assertEquals("Bob", result.get(0).get("name").asText());
    }

    @Test
    void addChangesSubsequentResults() throws Exception {
        parserService.processMessage("ADD 10 Dana 22");
        JsonNode result = objectMapper.readTree(parserService.processMessage("GET 10"));
        assertEquals("Dana", result.get(0).get("name").asText());
    }

    @Test
    void deleteRemovesPerson() throws Exception {
        parserService.processMessage("DELETE 2");
        JsonNode result = objectMapper.readTree(parserService.processMessage("GET 2"));
        assertTrue(result.isEmpty());
    }

    @Test
    void unknownCommandReturnsError() throws Exception {
        JsonNode result = objectMapper.readTree(parserService.processMessage("DANCE"));
        assertTrue(result.get(0).asText().startsWith("ERROR:"));
    }

    @Test
    void findReturnsAlice() throws Exception {
        JsonNode result = objectMapper.readTree(parserService.processMessage("FIND al"));
        assertEquals(1, result.size());
        assertEquals("Alice", result.get(0).get("name").asText());
    }
}
