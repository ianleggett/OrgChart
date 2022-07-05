package webtool.service;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import webtool.pojo.FileDB;
import webtool.repository.FileDBRepository;

@Service
public class StorageService {

  @Autowired
  private FileDBRepository fileDBRepository;

  public FileDB store(String caseguid, int slot, MultipartFile file) throws IOException {
    //String fileName = StringUtils.cleanPath(file.getOriginalFilename());
    FileDB FileDB = new FileDB(caseguid, slot, file.getContentType(), file.getBytes());

    return fileDBRepository.save(FileDB);
  }

  public Optional<FileDB> getFile(String caseguid, int slot) {
    return fileDBRepository.findByCaseGuidAndSlot(caseguid,slot);    
  }
  
  public Stream<FileDB> getAllCaseFiles(String caseguid) {
    return fileDBRepository.findByCaseGuid().stream();
  }
}