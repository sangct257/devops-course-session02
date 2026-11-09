package ra.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.demo.entity.User;

@Repository
public interface UserReposiory extends JpaRepository <User,Long> {
}
