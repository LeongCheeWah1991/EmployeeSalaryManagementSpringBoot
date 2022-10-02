package leongcheewah.salarymanagement.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

public class CSVUtil {
	private static final String CSV_FILE_CONTENTYPE = "text/csv";

	public static boolean validateCSVFileFormat(MultipartFile file) {
		if (!CSV_FILE_CONTENTYPE.equals(file.getContentType())) {
			return false;
		}
		return true;
	}

	public static List<CSVRecord> parseCsv(MultipartFile file) {

		List<CSVRecord> csvRecords = null;

		try {

			BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));
			CSVParser csvParser = new CSVParser(fileReader,
					CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
			csvRecords = csvParser.getRecords();

			csvParser.close();

		} catch (Exception ex) {

		}
		return csvRecords;

	}
}
