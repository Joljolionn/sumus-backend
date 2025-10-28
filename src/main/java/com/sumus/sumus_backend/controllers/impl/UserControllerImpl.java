package com.sumus.sumus_backend.controllers.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sumus.sumus_backend.controllers.docs.UserControllerDocs;
import com.sumus.sumus_backend.domain.entities.UserDocument;
import com.sumus.sumus_backend.services.UserService;

@RestController
@RequestMapping("/users")
public class UserControllerImpl implements UserControllerDocs {

    @Autowired
    private UserService userService;

    @Override
    @GetMapping(path = "/all")
    // CORRIGIDO: Tipo de retorno agora é List<UserDocument>
    public ResponseEntity<List<UserDocument>> getAllUsers() {
        // O serviço retorna List<UserDocument>
        return new ResponseEntity<>(userService.listAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/{email}/photo")
    public ResponseEntity<byte[]> getUserPhoto(@PathVariable String email) throws IOException {

        GridFsResource photoResource = userService.getPhotoResourceByUserEmail(email);

        if (photoResource == null) {
            return ResponseEntity.notFound().build();
        }

        String contentType = photoResource.getContentType();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(photoResource.getContentAsByteArray());
    }

}
