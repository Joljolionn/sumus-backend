package com.sumus.sumus_backend.controllers.driver.impl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sumus.sumus_backend.domain.dtos.request.DriverUpdateRequest;
import com.sumus.sumus_backend.domain.dtos.request.PasswordUpdateRequest;
import com.sumus.sumus_backend.domain.dtos.response.DriverListResponseDTO;
import com.sumus.sumus_backend.domain.dtos.response.DriverResponseDto;
import com.sumus.sumus_backend.services.driver.DriverService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/driver")
public class DriverController {

    @Autowired
    private DriverService driverService;

    @GetMapping(path = "/all")
    public ResponseEntity<DriverListResponseDTO> getAllDrivers() {

        return new ResponseEntity<>(driverService.listAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/photo")
    public ResponseEntity<byte[]> getDriverPhoto(@AuthenticationPrincipal UserDetails userDetails) throws IOException {
        GridFsResource photoResource = driverService.getPhotoResourceByDriverEmail(userDetails.getUsername());

        if (photoResource == null) {
            return ResponseEntity.notFound().build();
        }

        String contentType = photoResource.getContentType();

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                .body(photoResource.getContentAsByteArray());
    }

    @GetMapping(path = "/")
    public ResponseEntity<DriverResponseDto> findDriverByEmail(@AuthenticationPrincipal UserDetails userDetails) {
        DriverResponseDto driverResponseDto = driverService.findByEmail(userDetails.getUsername());

        if (driverResponseDto == null) {
            return ResponseEntity.notFound().build();
        }

        return new ResponseEntity<>(driverResponseDto, HttpStatus.OK);

    }

    @PostMapping(path = "/verify")
    public ResponseEntity<DriverResponseDto> verifyDriver(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok().body(driverService.verifyDocuments(userDetails.getUsername()));
    }

    @PutMapping(path = "/")
    public ResponseEntity<DriverResponseDto> updateDriver(@AuthenticationPrincipal UserDetails userDetails,
            @ModelAttribute @Valid DriverUpdateRequest driverUpdateRequest) {
        try {
            DriverResponseDto driverResponseDto = driverService.update(userDetails.getUsername(), driverUpdateRequest);
            if (driverResponseDto == null) {

                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(driverResponseDto);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PatchMapping(path = "/password")
    public ResponseEntity<Void> updateDriverPassword(@AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid PasswordUpdateRequest passwordUpdateRequest) {
        Boolean updated = driverService.updatePassword(userDetails.getUsername(), passwordUpdateRequest);
        if (!updated) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/")
    public ResponseEntity<Void> deleteDriver(@AuthenticationPrincipal UserDetails userDetails) {
        Boolean deleted = driverService.deleteDriver(userDetails.getUsername());
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

}
