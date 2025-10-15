package com.example.quiz.controller;

import com.example.quiz.model.Question;
import com.example.quiz.service.QuizService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
  private final QuizService quizService;

  public ViewController(QuizService quizService) { this.quizService = quizService; }

  @GetMapping("/")
  public String home() { return "redirect:/play"; }

  @GetMapping("/login")
  public String login() { return "login"; }

  @GetMapping("/play")
  public String play(Model model, Authentication auth) {
    Question q = quizService.getCurrentQuestion();
    model.addAttribute("question", q);
    model.addAttribute("rotationSeconds", quizService.getRotationSeconds());
    model.addAttribute("username", auth.getName());
    return "play";
  }

  @GetMapping("/admin")
  public String admin(Model model) {
    return "admin";
  }
}
