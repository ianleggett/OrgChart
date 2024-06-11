package webtool.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import webtool.pojo.FileDB;
import webtool.pojo.RespStatus;
import webtool.pojo.StorageProperties;
import webtool.repository.FileDBRepository;

@Service
public class StorageService {
	static Logger log = Logger.getLogger(StorageService.class);
	Path rootLocation;

	@Autowired
	private FileDBRepository fileDBRepository;

	@Autowired
	StorageProperties properties;
	
	@PostConstruct
	private void init() {
		this.rootLocation = Paths.get(properties.getLocation());
		deleteAll();
		createDir();
	}
	
	public void deleteAll() {
		//FileSystemUtils.deleteRecursively(rootLocation.toFile());
	}
	
	public void createDir() {
		try {
			Files.createDirectories(rootLocation);
		}
		catch (IOException e) {
			log.error("Could not initialize storage", e);
		}
	}
//  public FileDB store(String caseguid, int slot, MultipartFile file) throws IOException {
//    //String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//    FileDB FileDB = new FileDB(caseguid, slot, file.getContentType(), file.getBytes());
//
//    return fileDBRepository.save(FileDB);
//  }
	public Path store(MultipartFile file) {
		try {
			if (file.isEmpty()) {
				return null;
			}		
			
			Path destinationFile = rootLocation.resolve(
					Paths.get(file.getOriginalFilename()))
					.normalize().toAbsolutePath();
			if (!destinationFile.getParent().equals(rootLocation.toAbsolutePath())) {
				// This is a security check
				return null;
			}
			
			log.info("Storing "+file.getOriginalFilename() + " to "+destinationFile);
			
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, destinationFile,
					StandardCopyOption.REPLACE_EXISTING);
				return destinationFile;
			}
		}
		catch (IOException e) {
			log.error("Failed to store file."+e.getMessage());
			return null;
		}		
	}

	public Optional<FileDB> getFile(String caseguid, int slot) {
		return fileDBRepository.findByCaseGuidAndSlot(caseguid, slot);
	}

	public Stream<FileDB> getAllCaseFiles(String caseguid) {
		return fileDBRepository.findByCaseGuid().stream();
	}
}