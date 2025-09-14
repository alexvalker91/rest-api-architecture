package alex.valker91.controller;

import alex.valker91.mapper.DtoMapper;
import alex.valker91.model.Subscription;
import alex.valker91.model.SubscriptionRequestDto;
import alex.valker91.model.SubscriptionResponseDto;
import alex.valker91.model.User;
import alex.valker91.service.SubscriptionService;
import alex.valker91.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

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
    public ResponseEntity<SubscriptionResponseDto> createSubscription(@RequestBody SubscriptionRequestDto requestDto) {
        User user = userService.findById(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + requestDto.getUserId()));
        Subscription created = subscriptionService.create(DtoMapper.toSubscription(requestDto, user));
        SubscriptionResponseDto response = DtoMapper.toSubscriptionResponseDto(created);
        return ResponseEntity.created(URI.create("/api/subscriptions/" + created.getId())).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionResponseDto> updateSubscription(@PathVariable Long id, @RequestBody SubscriptionRequestDto requestDto) {
        User user = userService.findById(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + requestDto.getUserId()));
        Subscription toUpdate = DtoMapper.toSubscription(requestDto, user);
        toUpdate.setId(id);
        return subscriptionService.update(toUpdate)
                .map(updated -> ResponseEntity.ok(DtoMapper.toSubscriptionResponseDto(updated)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable Long id) {
        boolean deleted = subscriptionService.deleteById(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionResponseDto> getSubscription(@PathVariable Long id) {
        return subscriptionService.findById(id)
                .map(s -> ResponseEntity.ok(DtoMapper.toSubscriptionResponseDto(s)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionResponseDto>> getAllSubscription() {
        List<SubscriptionResponseDto> list = DtoMapper.toSubscriptionResponseDtoList(subscriptionService.findAll());
        return ResponseEntity.ok(list);
    }
}
