package ra.demo.initData;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ra.demo.entity.User;
import ra.demo.repository.UserReposiory;

import java.util.List;

@Component
@RequiredArgsConstructor
public class initUser implements CommandLineRunner {
    private final UserReposiory userReposiory;
    @Override
    public void run(String... args) throws Exception {
        if(userReposiory.findAll().isEmpty()) {
            List<User> products = List.of(
                    new User(null, "username1", "1234", "Nguyen Van A","anguyenvan@gmail.com")
            );

            userReposiory.saveAll(products);
        }
    }
}
