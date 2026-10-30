package kz.KEZ.FinanceTracker.controllers;

import kz.KEZ.FinanceTracker.dto.JwtResponse;
import kz.KEZ.FinanceTracker.dto.LoginRequest;
import kz.KEZ.FinanceTracker.dto.LogoutRequest;
import kz.KEZ.FinanceTracker.dto.RegisterRequest;
import kz.KEZ.FinanceTracker.jwt.JwtUtils;
import kz.KEZ.FinanceTracker.models.User;
import kz.KEZ.FinanceTracker.models.RefreshToken;
import kz.KEZ.FinanceTracker.repositories.UserRepository;
import kz.KEZ.FinanceTracker.service.RefreshTokenService;
import kz.KEZ.FinanceTracker.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final RegistrationService registrationService;
    private final ModelMapper modelMapper;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        String accessToken = jwtUtils.generateToken(user);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        return ResponseEntity.ok(new JwtResponse(accessToken, refreshToken.getToken()));
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        User user = modelMapper.map(request, User.class);
        registrationService.register(user);
        String accessToken = jwtUtils.generateToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        return ResponseEntity.ok(new JwtResponse(accessToken, refreshToken.getToken()));
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<?> refresh(@RequestParam String refreshToken) {

        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String newAccessToken = jwtUtils.generateToken(user);
                    return ResponseEntity.ok(
                            new JwtResponse(newAccessToken, refreshToken)
                    );
                })
                .orElseThrow(() -> new RuntimeException("Refresh token is not valid!"));
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout(@RequestBody LogoutRequest logoutRequest) {
        refreshTokenService.deleteByToken(logoutRequest.getRefreshToken());
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("User logged out successfully!");
    }

    @GetMapping("/protected")
    public String protectedEndpoint() {
        return "This is protected data!";
    }
}