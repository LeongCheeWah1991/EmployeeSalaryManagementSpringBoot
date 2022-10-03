package leongcheewah.salarymanagement.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

public class CSVUtil {
	private static final String CSV_FILE_CONTENTYPE = "text/csv";
	private static final List<String> CSV_FILE_HEADERS = new ArrayList<String>(
			Arrays.asList("id", "login", "name", "salary"));

	public static boolean validateCSVFileFormat(MultipartFile file) {
		if (!CSV_FILE_CONTENTYPE.equals(file.getContentType())) {
			return false;
		}
		return true;
	}

	public static boolean validateCsvHeaders(MultipartFile file) {

		try {
			BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));
			CSVParser csvParser = new CSVParser(fileReader,
					CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());

			List<String> csvHeaders = csvParser.getHeaderNames();
			csvParser.close();

			if (CSV_FILE_HEADERS.size() != csvHeaders.size()) {
				return false;
			}

			for (String expectedHeader : CSV_FILE_HEADERS) {
				if (!csvHeaders.contains(expectedHeader)) {
					return false;
				}
			}

		} catch (Exception ex) {
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
			return null;
		}
		return csvRecords;

	}
}
