package com.shzhangji.proton.controller;

import com.shzhangji.proton.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@Tag(name = "dashboard", description = "Dashboard data")
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {
  private final DashboardService dashboardService;

  @Value("classpath:geo-china.json")
  private Resource geoChinaResource;

  @Operation(summary = "Get geospatial data of China")
  @GetMapping("/geoChina")
  public ResponseEntity<StreamingResponseBody> getGeoChina() {
    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(out -> {
          try (var in = geoChinaResource.getInputStream()) {
            in.transferTo(out);
          }
        });
  }

  @GetMapping("/activeUser")
  public DashboardService.ActiveUserData getActiveUser() {
    return dashboardService.getActiveUser();
  }

  @GetMapping("/primaryData")
  public DashboardService.PrimaryData getPrimaryData() {
    return dashboardService.getPrimaryData(14);
  }

  @GetMapping("/userSource")
  public DashboardService.SourceData getUserSource() {
    return dashboardService.getUserSource(7);
  }

  @GetMapping("/userGeo")
  public DashboardService.UserGeoData getUserGeo() {
    return dashboardService.getUserGeo();
  }

  @GetMapping("/activeHourlyUsers")
  public UserHourlyResponse getUserHourly() {
    return new UserHourlyResponse(dashboardService.getUserHourly());
  }

  public record UserHourlyResponse(DashboardService.UserHourlyData[] data) {}

  @GetMapping("/userDevice")
  public UserDeviceResponse getUserDevice() {
    return new UserDeviceResponse(dashboardService.getUserDevice());
  }

  public record UserDeviceResponse(DashboardService.DeviceData[] devices) {}

  @GetMapping("/userRetention")
  public UserRetentionResponse getUserRetention() {
    return new UserRetentionResponse(dashboardService.getUserRetention());
  }

  public record UserRetentionResponse(DashboardService.RetentionData[] retention) {}
}
