package com.example.quiz.controller;

import com.example.quiz.model.Question;
import com.example.quiz.service.QuizService;
import com.example.quiz.service.SseService;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.*;

@RestController
@RequestMapping("/api")
public class ApiController {
  private final QuizService quizService;
  private final SseService sseService;

  public ApiController(QuizService quizService, SseService sseService) {
    this.quizService = quizService;
    this.sseService = sseService;
  }

  @GetMapping("/question")
  public Question currentQuestion() {
    return quizService.getCurrentQuestion();
  }

  @PostMapping("/answer")
  public void submit(@AuthenticationPrincipal UserDetails user, @RequestParam int index) {
    quizService.submitAnswer(user.getUsername(), index);
  }

  @GetMapping("/leaderboard")
  public Map<String, Integer> leaderboard() { return quizService.getLeaderboard(); }

  @GetMapping(path = "/stream/question", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter streamQuestion() {
    SseEmitter emitter = sseService.subscribeQuestions();
    // push current immediately
    try {
      emitter.send(SseEmitter.event().name("question").data(quizService.getCurrentQuestion()));
    } catch (Exception ignored) {}
    return emitter;
  }

  @GetMapping(path = "/stream/leaderboard", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter streamLeaderboard() {
    SseEmitter emitter = sseService.subscribeLeaderboard();
    try {
      emitter.send(SseEmitter.event().name("leaderboard").data(quizService.getLeaderboard()));
    } catch (Exception ignored) {}
    return emitter;
  }

  @PostMapping("/admin/questions")
  public void replace(@RequestBody List<Question> questions) {
    quizService.replaceQuestions(questions);
    sseService.publishQuestion(quizService.getCurrentQuestion());
  }
}
