package com.sumus.driver.domain.dtos.response;

import org.bson.types.ObjectId;
import com.sumus.driver.domain.entities.DriverDocument;

public record DriverResponseDto(

    String name,

    String email,

    String phone,

    ObjectId photoId

) {
  public DriverResponseDto(DriverDocument doc) {
    this(doc.getName(), doc.getEmail(), doc.getPhone(), doc.getPhotoId());
  }
}
