package alex.valker91.controller;

import alex.valker91.mapper.DtoMapper;
import alex.valker91.model.User;
import alex.valker91.model.UserRequestDto;
import alex.valker91.model.UserResponseDto;
import alex.valker91.service.UserService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<EntityModel<UserResponseDto>> createUser(@RequestBody UserRequestDto requestDto) {
        User toCreate = DtoMapper.toUser(requestDto);
        toCreate.setId(null);
        User created = userService.create(toCreate);
        UserResponseDto response = DtoMapper.toUserResponseDto(created);
        EntityModel<UserResponseDto> model = toModel(response);
        URI selfUri = linkTo(methodOn(UserController.class).getUser(created.getId())).toUri();
        return ResponseEntity.created(selfUri).body(model);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<UserResponseDto>> updateUser(@PathVariable Long id, @RequestBody UserRequestDto requestDto) {
        User toUpdate = DtoMapper.toUser(requestDto);
        toUpdate.setId(id);
        return userService.update(toUpdate)
                .map(updated -> ResponseEntity.ok(toModel(DtoMapper.toUserResponseDto(updated))))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteById(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UserResponseDto>> getUser(@PathVariable Long id) {
        return userService.findById(id)
                .map(user -> ResponseEntity.ok(toModel(DtoMapper.toUserResponseDto(user))))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<UserResponseDto>>> getAllUser() {
        List<EntityModel<UserResponseDto>> list = DtoMapper.toUserResponseDtoList(userService.findAll())
                .stream()
                .map(this::toModel)
                .collect(Collectors.toList());
        CollectionModel<EntityModel<UserResponseDto>> collectionModel = CollectionModel.of(list,
                linkTo(methodOn(UserController.class).getAllUser()).withSelfRel());
        return ResponseEntity.ok(collectionModel);
    }

    private EntityModel<UserResponseDto> toModel(UserResponseDto dto) {
        Long id = dto.getId();
        EntityModel<UserResponseDto> model = EntityModel.of(dto,
                linkTo(methodOn(UserController.class).getUser(id)).withSelfRel(),
                linkTo(methodOn(UserController.class).getAllUser()).withRel("users"));
        model.add(linkTo(methodOn(UserController.class).updateUser(id, null)).withRel("update"));
        model.add(linkTo(methodOn(UserController.class).deleteUser(id)).withRel("delete"));
        return model;
    }
}
