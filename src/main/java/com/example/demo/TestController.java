package com.example.demo;

import net.sf.sevenzipjbinding.IInArchive;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.SevenZipException;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@RestController
public class TestController {
    private final Logger log = LoggerFactory.getLogger(TestController.class);

    @PostMapping("/upload")
    public void extractRar(@RequestBody UploadDto dto){
        byte[] decodedBytes = Base64.getDecoder().decode(dto.data());
        try {
            String filePath = dto.name();
            Files.write(Paths.get(filePath), decodedBytes);
            log.info("File saved successfully to: " + filePath);
            File file = new File(filePath);
            IInArchive archive;
            try {
                archive = SevenZip.openInArchive(null, new RandomAccessFileInStream(
                        new RandomAccessFile(file, "r")));
                log.info(String.valueOf(archive.getNumberOfItems()));
            } catch (SevenZipException | FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            archive.close();

            // 1 method
            Files.delete(Path.of(filePath));

            // 2 method
            if (file.delete()) {
                log.info("file deleted successfully");
            } else {
                log.error("failed to delete the temporary file used for unzipping");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
