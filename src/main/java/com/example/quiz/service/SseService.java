package com.example.quiz.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class SseService {
  private final List<SseEmitter> questionEmitters = new CopyOnWriteArrayList<>();
  private final List<SseEmitter> leaderboardEmitters = new CopyOnWriteArrayList<>();

  public SseEmitter subscribeQuestions() {
    SseEmitter emitter = new SseEmitter(0L);
    questionEmitters.add(emitter);
    emitter.onCompletion(() -> questionEmitters.remove(emitter));
    emitter.onTimeout(() -> questionEmitters.remove(emitter));
    return emitter;
  }

  public SseEmitter subscribeLeaderboard() {
    SseEmitter emitter = new SseEmitter(0L);
    leaderboardEmitters.add(emitter);
    emitter.onCompletion(() -> leaderboardEmitters.remove(emitter));
    emitter.onTimeout(() -> leaderboardEmitters.remove(emitter));
    return emitter;
  }

  public void publishQuestion(Object data) {
    broadcast(questionEmitters, data, "question");
  }

  public void publishLeaderboard(Object data) {
    broadcast(leaderboardEmitters, data, "leaderboard");
  }

  private void broadcast(List<SseEmitter> targets, Object data, String event) {
    for (SseEmitter emitter : targets) {
      try {
        emitter.send(SseEmitter.event().name(event).data(data));
      } catch (IOException e) {
        emitter.complete();
      }
    }
  }
}
