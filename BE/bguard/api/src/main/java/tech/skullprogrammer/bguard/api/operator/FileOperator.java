package tech.skullprogrammer.bguard.api.operator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Slf4j
public class FileOperator {

    public File saveToFile(MultipartFile multipartFile, String path, String name) throws IOException {
        log.info("Saving file {} to path {}", multipartFile.getOriginalFilename(), path);
        String extension = StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
        File file = Path.of(path).resolve(name + "." + extension).toFile();
        multipartFile.transferTo(file);
        return file;
    }
}
