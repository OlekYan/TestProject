package com.example.quiz.model;

public class PlayerAnswer {
  private final String username;
  private final String questionId;
  private final int selectedIndex;
  private final long timestamp;

  public PlayerAnswer(String username, String questionId, int selectedIndex, long timestamp) {
    this.username = username;
    this.questionId = questionId;
    this.selectedIndex = selectedIndex;
    this.timestamp = timestamp;
  }

  public String getUsername() { return username; }
  public String getQuestionId() { return questionId; }
  public int getSelectedIndex() { return selectedIndex; }
  public long getTimestamp() { return timestamp; }
}
