package com.example.quiz.service;

import com.example.quiz.model.PlayerAnswer;
import com.example.quiz.model.Question;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class QuizService {
  private final List<Question> questions = new CopyOnWriteArrayList<>();
  private final Map<String, Integer> scores = new HashMap<>();
  private final Map<String, PlayerAnswer> currentAnswers = new HashMap<>();
  private final AtomicInteger index = new AtomicInteger(0);

  private final int rotationSeconds;

  public QuizService(@Value("${app.quiz.rotationSeconds:20}") int rotationSeconds) {
    this.rotationSeconds = rotationSeconds;
    seedQuestions();
  }

  public synchronized Question getCurrentQuestion() {
    if (questions.isEmpty()) return null;
    return questions.get(Math.floorMod(index.get(), questions.size()));
  }

  public synchronized void rotateQuestion() {
    Question current = getCurrentQuestion();
    // score all currentAnswers for this question before rotating
    for (PlayerAnswer answer : currentAnswers.values()) {
      if (answer.getQuestionId().equals(current.getId())) {
        if (answer.getSelectedIndex() == current.getCorrectIndex()) {
          scores.merge(answer.getUsername(), 1, Integer::sum);
        }
      }
    }
    currentAnswers.clear();
    index.incrementAndGet();
  }

  public synchronized void submitAnswer(String username, int selectedIndex) {
    Question current = getCurrentQuestion();
    if (current == null) return;
    // allow one answer per user per question (latest overwrites in current round)
    currentAnswers.put(username, new PlayerAnswer(username, current.getId(), selectedIndex, Instant.now().toEpochMilli()));
  }

  public synchronized Map<String, Integer> getLeaderboard() {
    // return sorted by score desc then name
    List<Map.Entry<String, Integer>> entries = new ArrayList<>(scores.entrySet());
    entries.sort(Comparator.<Map.Entry<String, Integer>>comparingInt(Map.Entry::getValue).reversed()
        .thenComparing(Map.Entry::getKey));
    LinkedHashMap<String, Integer> ordered = new LinkedHashMap<>();
    for (Map.Entry<String, Integer> e : entries) {
      ordered.put(e.getKey(), e.getValue());
    }
    return ordered;
  }

  public synchronized void replaceQuestions(List<Question> newQuestions) {
    questions.clear();
    questions.addAll(newQuestions);
    index.set(0);
    currentAnswers.clear();
  }

  public int getRotationSeconds() { return rotationSeconds; }

  private void seedQuestions() {
    questions.add(new Question("q1", "Capital of France?", List.of("Berlin", "Madrid", "Paris", "Rome"), 2));
    questions.add(new Question("q2", "2 + 2 = ?", List.of("3", "4", "5", "22"), 1));
    questions.add(new Question("q3", "Color of the sky?", List.of("Blue", "Green", "Red", "Yellow"), 0));
  }
}
