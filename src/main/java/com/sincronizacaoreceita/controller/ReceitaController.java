package com.sincronizacaoreceita.controller;

import com.sincronizacaoreceita.serviço.ReceitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Autor: Matheus Lone
 */
@RestController
public class ReceitaController {

    @Autowired
    private ReceitaService receitaService;

    @PostMapping("/upload-arquivo")
    public ResponseEntity<Resource> carregar(@RequestPart("arquivo") MultipartFile arquivo) throws IOException {

        InputStreamResource inputStreamResource = new InputStreamResource(receitaService.carregar(arquivo));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + arquivo.getOriginalFilename())
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(inputStreamResource);
    }

    @GetMapping("/download-file")
    public ResponseEntity<Resource> download()  {
        InputStreamResource inputStreamResource = new InputStreamResource(receitaService.download());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=testingsss.csv")
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(inputStreamResource);
    }
}
