package com.example.quiz.schedule;

import com.example.quiz.model.Question;
import com.example.quiz.service.QuizService;
import com.example.quiz.service.SseService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RotationScheduler {
  private final QuizService quizService;
  private final SseService sseService;

  public RotationScheduler(QuizService quizService, SseService sseService) {
    this.quizService = quizService;
    this.sseService = sseService;
  }

  @Scheduled(fixedRateString = "${app.quiz.rotationSeconds:20}000")
  public void rotateAndBroadcast() {
    // rotate (scores previous answers), then broadcast new question and leaderboard
    quizService.rotateQuestion();
    Question current = quizService.getCurrentQuestion();
    sseService.publishQuestion(current);
    Map<String, Integer> leaderboard = quizService.getLeaderboard();
    sseService.publishLeaderboard(leaderboard);
  }
}
