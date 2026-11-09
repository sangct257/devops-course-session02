package ra.demo.service;

import ra.demo.dto.request.UserRequest;
import ra.demo.entity.User;

import java.util.List;

public interface UserService {
    List<User> getAllUser();
    User insertUser(UserRequest userRequest);
    User updateUser(Long userId, UserRequest userRequest);
    User getUserById(Long userId);
    void deleteUser(Long userId);

}
