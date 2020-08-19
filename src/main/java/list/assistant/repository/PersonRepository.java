package list.assistant.repository;

import java.util.List;
import list.assistant.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, String> {

    List<Person> findByName(String name);
}
