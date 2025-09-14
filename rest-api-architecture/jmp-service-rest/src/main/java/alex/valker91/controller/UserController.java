package alex.valker91.controller;

import alex.valker91.mapper.DtoMapper;
import alex.valker91.model.User;
import alex.valker91.model.UserRequestDto;
import alex.valker91.model.UserResponseDto;
import alex.valker91.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto requestDto) {
        User toCreate = DtoMapper.toUser(requestDto);
        toCreate.setId(null);
        User created = userService.create(toCreate);
        UserResponseDto response = DtoMapper.toUserResponseDto(created);
        return ResponseEntity.created(URI.create("/api/users/" + created.getId())).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id, @RequestBody UserRequestDto requestDto) {
        User toUpdate = DtoMapper.toUser(requestDto);
        toUpdate.setId(id);
        return userService.update(toUpdate)
                .map(updated -> ResponseEntity.ok(DtoMapper.toUserResponseDto(updated)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteById(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long id) {
        return userService.findById(id)
                .map(user -> ResponseEntity.ok(DtoMapper.toUserResponseDto(user)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUser() {
        List<UserResponseDto> list = DtoMapper.toUserResponseDtoList(userService.findAll());
        return ResponseEntity.ok(list);
    }
}
