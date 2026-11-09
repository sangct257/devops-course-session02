package ra.demo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.demo.dto.request.UserRequest;
import ra.demo.dto.response.ApiResponse;
import ra.demo.entity.User;
import ra.demo.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> getAllUser(){
        return new ResponseEntity<>(new ApiResponse<>(
                true,
                "Lấy danh sách user thành công",
                userService.getAllUser(),
                null,
                HttpStatus.OK
        ),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<User>> addProduct(@Valid UserRequest userRequest) {
        return new ResponseEntity<>(new ApiResponse<>(
                true,
                "Thêm user thành công",
                userService.insertUser(userRequest),
                null,
                HttpStatus.CREATED
        ), HttpStatus.CREATED);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<User>> updateProduct(@PathVariable Long userId, @Valid UserRequest userRequest) {
        return new ResponseEntity<>(new ApiResponse<>(
                true,
                "Cập nhật user thành công",
                userService.updateUser(userId, userRequest),
                null,
                HttpStatus.OK
        ), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<User>> getProduct(@PathVariable Long userId) {
        return new ResponseEntity<>(new ApiResponse<>(
                true,
                "Lấy thông tin user " + userId + " thành công",
                userService.getUserById(userId),
                null,
                HttpStatus.OK
        ), HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(new ApiResponse<>(
                true,
                "Xóa user " + userId + " thành công",
                null,
                null,
                HttpStatus.NO_CONTENT
        ), HttpStatus.NO_CONTENT);
    }

}
