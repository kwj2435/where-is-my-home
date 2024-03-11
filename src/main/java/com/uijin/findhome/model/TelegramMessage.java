package com.uijin.findhome.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TelegramMessage {
  private String chat_id;
  private String text;
}
