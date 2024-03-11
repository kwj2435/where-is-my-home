package com.uijin.findhome.feign;

import com.uijin.findhome.model.TelegramMessage;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "telegramClient", url = "https://api.telegram.org/bot6589455347:AAEbaaaki4raiiAArgDdOV5x2mE1mZ4Ys2M/sendMessage")
public interface TelegramClient {

  @PostMapping
  Response sendMessage(@RequestBody TelegramMessage telegramMessage);
}
