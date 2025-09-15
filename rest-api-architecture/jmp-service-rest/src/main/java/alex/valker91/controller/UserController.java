package alex.valker91.controller;

import alex.valker91.mapper.DtoMapper;
import alex.valker91.model.User;
import alex.valker91.model.UserRequestDto;
import alex.valker91.model.UserResponseDto;
import alex.valker91.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Users", description = "User management API")
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Create user", description = "Creates a new user and returns it")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created",
                    content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
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
    @Operation(summary = "Update user by id", description = "Updates existing user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated",
                    content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    public ResponseEntity<EntityModel<UserResponseDto>> updateUser(@Parameter(description = "User id", required = true) @PathVariable Long id, @RequestBody UserRequestDto requestDto) {
        User toUpdate = DtoMapper.toUser(requestDto);
        toUpdate.setId(id);
        return userService.update(toUpdate)
                .map(updated -> ResponseEntity.ok(toModel(DtoMapper.toUserResponseDto(updated))))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user by id", description = "Deletes existing user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Void> deleteUser(@Parameter(description = "User id", required = true) @PathVariable Long id) {
        boolean deleted = userService.deleteById(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by id", description = "Returns existing user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    public ResponseEntity<EntityModel<UserResponseDto>> getUser(@Parameter(description = "User id", required = true) @PathVariable Long id) {
        return userService.findById(id)
                .map(user -> ResponseEntity.ok(toModel(DtoMapper.toUserResponseDto(user))))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping
    @Operation(summary = "List users", description = "Returns all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of users",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserResponseDto.class))))
    })
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
