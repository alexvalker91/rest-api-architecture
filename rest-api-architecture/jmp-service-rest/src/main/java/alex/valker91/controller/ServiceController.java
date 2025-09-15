package alex.valker91.controller;

import alex.valker91.mapper.DtoMapper;
import alex.valker91.model.Subscription;
import alex.valker91.model.SubscriptionRequestDto;
import alex.valker91.model.SubscriptionResponseDto;
import alex.valker91.model.User;
import alex.valker91.service.SubscriptionService;
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
@RequestMapping("/api/v1/subscriptions")
public class ServiceController {

    private final UserService userService;
    private final SubscriptionService subscriptionService;

    public ServiceController(UserService userService, SubscriptionService subscriptionService) {
        this.userService = userService;
        this.subscriptionService = subscriptionService;
    }

    @PostMapping
    public ResponseEntity<EntityModel<SubscriptionResponseDto>> createSubscription(@RequestBody SubscriptionRequestDto requestDto) {
        User user = userService.findById(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + requestDto.getUserId()));
        Subscription created = subscriptionService.create(DtoMapper.toSubscription(requestDto, user));
        SubscriptionResponseDto response = DtoMapper.toSubscriptionResponseDto(created);

        URI selfUri = linkTo(methodOn(ServiceController.class).getSubscription(created.getId())).toUri();
        return ResponseEntity.created(selfUri).body(toModel(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<SubscriptionResponseDto>> updateSubscription(@PathVariable Long id, @RequestBody SubscriptionRequestDto requestDto) {
        User user = userService.findById(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + requestDto.getUserId()));
        Subscription toUpdate = DtoMapper.toSubscription(requestDto, user);
        toUpdate.setId(id);
        return subscriptionService.update(toUpdate)
                .map(updated -> ResponseEntity.ok(toModel(DtoMapper.toSubscriptionResponseDto(updated))))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable Long id) {
        boolean deleted = subscriptionService.deleteById(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<SubscriptionResponseDto>> getSubscription(@PathVariable Long id) {
        return subscriptionService.findById(id)
                .map(s -> ResponseEntity.ok(toModel(DtoMapper.toSubscriptionResponseDto(s))))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<SubscriptionResponseDto>>> getAllSubscription() {
        List<EntityModel<SubscriptionResponseDto>> list = DtoMapper.toSubscriptionResponseDtoList(subscriptionService.findAll())
                .stream()
                .map(this::toModel)
                .collect(Collectors.toList());
        CollectionModel<EntityModel<SubscriptionResponseDto>> collectionModel = CollectionModel.of(list,
                linkTo(methodOn(ServiceController.class).getAllSubscription()).withSelfRel());
        return ResponseEntity.ok(collectionModel);
    }

    private EntityModel<SubscriptionResponseDto> toModel(SubscriptionResponseDto dto) {
        Long id = dto.getId();
        EntityModel<SubscriptionResponseDto> model = EntityModel.of(dto,
                linkTo(methodOn(ServiceController.class).getSubscription(id)).withSelfRel(),
                linkTo(methodOn(ServiceController.class).getAllSubscription()).withRel("subscriptions"));
        model.add(linkTo(methodOn(ServiceController.class).updateSubscription(id, null)).withRel("update"));
        model.add(linkTo(methodOn(ServiceController.class).deleteSubscription(id)).withRel("delete"));
        if (dto.getUserId() != null) {
            model.add(linkTo(methodOn(UserController.class).getUser(dto.getUserId())).withRel("user"));
        }
        return model;
    }
}
