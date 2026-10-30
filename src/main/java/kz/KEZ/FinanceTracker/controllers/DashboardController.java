package kz.KEZ.FinanceTracker.controllers;


import kz.KEZ.FinanceTracker.dto.DashboardDTO;
import kz.KEZ.FinanceTracker.models.User;
import kz.KEZ.FinanceTracker.repositories.UserRepository;
import kz.KEZ.FinanceTracker.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class DashboardController {

    private final UserRepository userRepository;
    private final DashboardService dashboardService;

    @GetMapping("/api/dashboard")
    public DashboardDTO dashboard() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow();
        return dashboardService.build(user);
    }
}
