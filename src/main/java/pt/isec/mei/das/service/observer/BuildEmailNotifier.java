package pt.isec.mei.das.service.observer;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pt.isec.mei.das.entity.BuildResult;
import pt.isec.mei.das.service.EmailService;

@Service
@RequiredArgsConstructor
public class BuildEmailNotifier implements BuildObserver {

    private final EmailService emailService;

    @Value("${mail.email-to}")
    private String emailTo;

    @Override
    public void update(BuildResult buildResult) {
        if (buildResult.isNotificationNeeded()) {
            String message = "Your build with id " + buildResult.getId() + " for a project " + buildResult.getProject().getId() + " is ready";
            emailService.sendEmail(emailTo, "Build ready", message);
        }
    }
}
