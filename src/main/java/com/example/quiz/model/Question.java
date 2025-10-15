package com.example.quiz.model;

import java.util.List;

public class Question {
  private final String id;
  private final String text;
  private final List<String> options;
  private final int correctIndex;

  public Question(String id, String text, List<String> options, int correctIndex) {
    this.id = id;
    this.text = text;
    this.options = options;
    this.correctIndex = correctIndex;
  }

  public String getId() { return id; }
  public String getText() { return text; }
  public List<String> getOptions() { return options; }
  public int getCorrectIndex() { return correctIndex; }
}
