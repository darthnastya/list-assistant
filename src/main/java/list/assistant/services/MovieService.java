package list.assistant.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import list.assistant.dto.MovieDTO;
import list.assistant.entities.Movie;
import list.assistant.entities.Person;
import list.assistant.repository.MovieRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private PersonService personService;


    public List<MovieDTO> getAll() {
        List<Movie> movies = movieRepository.findAll();
        return movies.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public MovieDTO create(MovieDTO movieDTO) {

        Movie movie = convertToEntity(movieDTO);
        movieRepository.save(movie);

        return movieDTO;
    }

    private Set<Person> getPersonsList(List<String> names) {
        Set<Person> party = new HashSet<>();

        if (names == null) {
            return party;
        }
        for (String personName : names) {
            Optional<Person> personOptional = personService.getByName(personName);
            Person person;

            if (!personOptional.isPresent()) {
                person = Person.builder().name(personName).build();
            } else {
                person = personOptional.get();
            }
            party.add(person);
        }
        return party;
    }

    private MovieDTO convertToDto(Movie movie) {
        return MovieDTO.builder()
                .name(movie.getName())
                .score(movie.getScore())
                .place(movie.getPlace())
                .date(movie.getDate())
                .persons(
                        movie.getPersons().stream().map(Person::getName).collect(Collectors.toList())
                ).build();
    }

    private Movie convertToEntity(MovieDTO movieDTO) {
        LocalDate date;
        if (movieDTO.getDate() == null) {
            date = LocalDate.now();
        } else {
            date = movieDTO.getDate();
        }

        Set<Person> party = getPersonsList(movieDTO.getPersons());

        return Movie.builder()
                .name(movieDTO.getName())
                .persons(party)
                .date(date)
                .place(movieDTO.getPlace())
                .score(movieDTO.getScore())
                .build();
    }

    public void createList(List<MovieDTO> movieDTOS) {

        Map<String, Person> personsMap = personService.getPersonsMap();
        List<Movie> movies = new ArrayList<>();
        for (MovieDTO movieDTO : movieDTOS) {
            LocalDate date;
            if (movieDTO.getDate() == null) {
                date = LocalDate.now();
            } else {
                date = movieDTO.getDate();
            }

            Set<Person> party = new HashSet<>();

            for (String personName : movieDTO.getPersons()) {
                if (personsMap.containsKey(personName)) {
                    party.add(personsMap.get(personName));
                } else {
                    Person person = Person.builder().name(personName).build();
                    party.add(person);
                    personsMap.put(personName, person);
                }
            }
            Movie movie = Movie.builder()
                    .name(movieDTO.getName())
                    .persons(party)
                    .date(date)
                    .place(movieDTO.getPlace())
                    .score(movieDTO.getScore())
                    .build();
            movies.add(movie);
        }
        movieRepository.saveAll(movies);
    }
}
