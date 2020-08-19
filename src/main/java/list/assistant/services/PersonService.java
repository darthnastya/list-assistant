package list.assistant.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import list.assistant.dto.PersonDTO;
import list.assistant.dto.PersonStatisticDTO;
import list.assistant.entities.Person;
import list.assistant.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    Optional<Person> getByName(String name) {
        List<Person> persons = personRepository.findByName(name);
        if (persons.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(personRepository.findByName(name).get(0));
    }


    public List<PersonDTO> getAll() {
        List<Person> persons = personRepository.findAll();
        return persons.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public List<PersonStatisticDTO> getPersonsStatistic() {
        List<Person> persons = personRepository.findAll();
        return persons.stream().map(this::convertToStatisticDto).collect(Collectors.toList());
    }

    private PersonDTO convertToDto(Person person) {
        return PersonDTO.builder()
                .name(person.getName())
                .build();
    }

    private PersonStatisticDTO convertToStatisticDto(Person person) {
        return PersonStatisticDTO.builder()
                .id(person.getId())
                .name(person.getName())
                .count(person.getMovies().size())
                .build();
    }

    public Map<String, Person> getPersonsMap() {
        List<Person> persons = personRepository.findAll();

        return persons.stream().collect(Collectors.toMap(Person::getName, item -> item));
    }
}
