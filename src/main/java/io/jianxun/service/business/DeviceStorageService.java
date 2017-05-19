package io.jianxun.service.business;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import io.jianxun.service.BusinessException;

@Component
public class DeviceStorageService {

	private final Path rootLocation;

	private DeviceStorageService(StorageProperties properties) {
		this.rootLocation = Paths.get(properties.getLocation());
	}

	public Path getRootLocation() {
		return rootLocation;
	}

	public String store(String prefix, MultipartFile file) {
		try {
			if (file.isEmpty()) {
				throw new BusinessException("Failed to store empty file " + file.getOriginalFilename());
			}
			if (!Files.exists(this.rootLocation))
				Files.createDirectory(rootLocation);
			String path = getFilePathString(prefix, file);
			Files.copy(file.getInputStream(), this.rootLocation.resolve(path),
					StandardCopyOption.REPLACE_EXISTING);
			return path;
		} catch (IOException e) {
			throw new BusinessException("Failed to store file " + file.getOriginalFilename(), e);
		}
	}

	public String getFilePathString(String prefix, MultipartFile file) {
		String fileName = file.getOriginalFilename();
		String suffix=fileName.substring(fileName.lastIndexOf(".")+1);
		return prefix + Calendar.getInstance().getTimeInMillis()+ "." + suffix;
	}

	public Path load(String filename) {
		return rootLocation.resolve(filename);
	}

	public Resource loadAsResource(String filename) {
		try {
			Path file = load(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new BusinessException("Could not read file: " + filename);

			}
		} catch (MalformedURLException e) {
			throw new BusinessException("Could not read file: " + filename, e);
		}
	}

}
