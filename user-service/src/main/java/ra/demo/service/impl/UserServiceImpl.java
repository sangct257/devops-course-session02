package ra.demo.service.impl;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ra.demo.dto.request.UserRequest;
import ra.demo.entity.User;
import ra.demo.repository.UserReposiory;
import ra.demo.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserReposiory userReposiory;

    @Override
    public List<User> getAllUser() {
        return userReposiory.findAll();
    }

    @Override
    public User insertUser(UserRequest userRequest) {
        User user = User.builder()
                .username(userRequest.getUsername())
                .password(userRequest.getPassword())
                .fullName(userRequest.getFullName())
                .email(userRequest.getEmail())
                .build();

        return userReposiory.save(user);
    }

    @Override
    public User updateUser(Long userId, UserRequest userRequest) {
        User user = userReposiory.findById(userId).orElseThrow(() -> new RuntimeException("Không tìm thấy user có mã: " + userId));

        user.setUsername(userRequest.getUsername());
        user.setPassword(userRequest.getPassword());
        user.setFullName(userRequest.getFullName());
        user.setEmail(userRequest.getEmail());

        return userReposiory.save(user);
    }

    @Override
    public User getUserById(Long userId) {
         return userReposiory.findById(userId).orElseThrow(() -> new RuntimeException("Không tìm thấy user có mã: " + userId));
    }

    @Override
    public void deleteUser(Long userId) {
        userReposiory.findById(userId).orElseThrow(() -> new RuntimeException("Không tìm thấy user có mã: " + userId));
        userReposiory.deleteById(userId);
    }
}
