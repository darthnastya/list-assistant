package list.assistant.controllers;

import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import java.util.List;
import list.assistant.dto.MovieDTO;
import list.assistant.dto.PersonDTO;
import list.assistant.dto.PersonStatisticDTO;
import list.assistant.services.MovieService;
import list.assistant.services.PersonService;
import list.assistant.services.XLSXService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
public class MovieController {

    @Autowired
    private MovieService movieService;
    @Autowired
    private PersonService personService;
    @Autowired
    private XLSXService xlsxService;

    @ApiOperation(value = "Currently available movies")
    @GetMapping("/movies")
    public List<MovieDTO> getMovies() {
        return movieService.getAll();
    }

    @ApiOperation(value = "All your friends")
    @GetMapping("/persons")
    public List<PersonDTO> getPersons() {
        return personService.getAll();
    }

    @ApiOperation(value = "All your friends")
    @GetMapping("/persons/statistic")
    public List<PersonStatisticDTO> getPersonsStatistic() {

        return personService.getPersonsStatistic();
    }


    @ApiOperation(value = "Add new name")
    @PostMapping("/movies/new")
    public MovieDTO postMovies(@RequestBody MovieDTO movieDTO) {
        return movieService.create(movieDTO);
    }

    @PostMapping("/movies/import")
    public ResponseEntity importMovies(@RequestParam("file") MultipartFile dataFile) {
        List<MovieDTO> movieDTOS;
        try {
            movieDTOS = xlsxService.extractFromFile(dataFile);
        } catch (IOException e) {
            return ResponseEntity.unprocessableEntity().build();
        }

        movieService.createList(movieDTOS);
        return ResponseEntity.ok().build();
    }


}
