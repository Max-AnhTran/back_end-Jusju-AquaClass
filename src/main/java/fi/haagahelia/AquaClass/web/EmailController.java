package fi.haagahelia.AquaClass.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;

import fi.haagahelia.AquaClass.dtoAndService.EmailService;

@Controller
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping
    public String showForm(Model model) {
        return "emailform";  
    }

    @PostMapping("/send")
    public String sendEmail(@RequestParam String to, 
                            @RequestParam String subject, 
                            @RequestParam String body,
                            Model model) {
        emailService.sendSimpleEmail(to, subject, body);
        model.addAttribute("message", "Email sent successfully!");
        return "emailform";
    }
}

