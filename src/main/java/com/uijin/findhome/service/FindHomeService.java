package com.uijin.findhome.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.uijin.findhome.feign.TelegramClient;
import com.uijin.findhome.feign.ZigbangClient;
import com.uijin.findhome.model.TelegramMessage;
import com.uijin.findhome.model.ZigBangRequest;
import feign.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindHomeService {

  private final TelegramClient telegramClient;
  private final ZigbangClient zigbangClient;

  private boolean init = false;
  private Set<Integer> homeList = new HashSet<>();

  @Scheduled(cron = "0/15 * * * * ?")
  public void findHome() throws IOException {
    Response list = zigbangClient.getList(new ZigBangRequest(new ArrayList<>(){{add(39462023);}}));
    List<String> geoHashList = new ArrayList<>(){
      {add("wydjtz");
      add("wydjtx");
      add("wydjtw");
      add("wydjty");
      add("wydjty");
      add("wydmgg");}
    };

    List<Integer> newHome = new ArrayList<>();
    for (String geoHash : geoHashList) {
      String url = "https://apis.zigbang.com/v2/items/villa?geohash=" + geoHash
          + "&depositMin=0&depositMax=25000&salesTypes%5B0%5D=%EC%A0%84%EC%84%B8&domain=zigbang&checkAnyItemWithoutFilter=true";
      Document docs = Jsoup.connect(url).ignoreContentType(true).get();

      String s = docs.body().text();

      JsonElement jsonElement = JsonParser.parseString(s);
      JsonObject jsonObject = jsonElement.getAsJsonObject();
      JsonArray itemsArray = jsonObject.getAsJsonArray("items");

      // 신규 매물 등록 여부 확인
      for (JsonElement element : itemsArray) {
        JsonObject itemObject = element.getAsJsonObject();
        int itemId = itemObject.get("itemId").getAsInt();
        if(!init) {
          homeList.add(itemId);
        } else {
          if(!homeList.contains(itemId)) {

            homeList.add(itemId);
            newHome.add(itemId);
          }
        }
      }

      // 신규 매물 알림 전송
      if(!newHome.isEmpty()) {
        Response test = zigbangClient.getList(new ZigBangRequest(newHome));
        telegramClient.sendMessage(new TelegramMessage("6645481472", "String.valueOf(itemId)"));
      }
    }
  }
}
