package com.models.controller;

import com.models.model.ApiModel;
import com.models.model.ApiRequest;
import com.models.model.ApiValidationResponse;

import com.models.service.ApiModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ApiModelsController {

    @Autowired
    private ApiModelService apiModelService;

    @PostMapping("/models/validate")
    public ResponseEntity<ApiValidationResponse> validate(@RequestBody ApiRequest apiRequest) {
        ApiValidationResponse apiCheckResponse = apiModelService.validate(apiRequest);
        return ResponseEntity.ok(apiCheckResponse);
    }

    @PostMapping("/api-models")
    public ResponseEntity<Void> registerModels(@RequestBody ApiModel[] apiModel) {
        apiModelService.registerModel(apiModel);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api-models")
    public ResponseEntity<ApiModel[]> getModels() {
        return ResponseEntity.ok(apiModelService.getModels().toArray(new ApiModel[0]));
    }

    @GetMapping("/api-model")
    public ResponseEntity<ApiModel> getModel(@RequestParam String path, @RequestParam String method) {
        return ResponseEntity.ok(apiModelService.findById(path, method));
    }
}
