package com.shzhangji.proton.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
  @Value("classpath:geo-china.json")
  private Resource geoChinaResource;

  @GetMapping(value = "/geoChina")
  public ResponseEntity<StreamingResponseBody> getGeoChina() {
    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(out -> {
          try (var in = geoChinaResource.getInputStream()) {
            in.transferTo(out);
          }
        });
  }
}
