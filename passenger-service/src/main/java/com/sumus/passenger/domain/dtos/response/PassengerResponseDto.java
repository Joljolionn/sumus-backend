package com.sumus.passenger.domain.dtos.response;

import org.bson.types.ObjectId;
import com.sumus.passenger.domain.entities.PassengerDocument;

public record PassengerResponseDto(

    String name,

    String email,

    String phone,

    ObjectId photoId

) {
  public PassengerResponseDto(PassengerDocument doc) {
    this(doc.getName(), doc.getEmail(), doc.getPhone(), doc.getPhotoId());
  }
}
