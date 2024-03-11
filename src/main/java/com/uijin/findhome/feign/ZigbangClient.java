package com.uijin.findhome.feign;

import com.uijin.findhome.model.TelegramMessage;
import com.uijin.findhome.model.ZigBangRequest;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "zigbangClient", url =  "https://apis.zigbang.com/v2/items/list")
public interface ZigbangClient {
  @PostMapping
  Response getList(@RequestBody ZigBangRequest zigBangRequest);
}
