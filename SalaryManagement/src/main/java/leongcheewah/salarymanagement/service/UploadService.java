package leongcheewah.salarymanagement.service;

import org.springframework.stereotype.Service;

@Service
public class UploadService {
	
	private static boolean isUpload = false;
	
	public boolean getUploadStatus () {
		return isUpload;
	}
	
	public void setUploadStatus(boolean status) {
		isUpload = status;
	}
}
