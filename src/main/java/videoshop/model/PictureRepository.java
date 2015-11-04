package videoshop.model;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface PictureRepository extends CrudRepository<Picture, Long>
{
	Optional<Picture> findByName(String name);
}
