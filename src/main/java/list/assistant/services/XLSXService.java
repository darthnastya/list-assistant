package list.assistant.services;

import java.io.IOException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import list.assistant.dto.MovieDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class XLSXService {

    public List<MovieDTO> extractFromFile(MultipartFile dataFile) throws IOException {
        List<MovieDTO> moviesDTO = new ArrayList<>();

        XSSFWorkbook workbook = new XSSFWorkbook(dataFile.getInputStream());
        XSSFSheet worksheet = workbook.getSheetAt(0);

        for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
            MovieDTO movieDTO = new MovieDTO();

            XSSFRow row = worksheet.getRow(i);

            movieDTO.setName(row.getCell(1).toString());

            Date date = row.getCell(2).getDateCellValue();
            if (!StringUtils.isEmpty(date)) {
                movieDTO.setDate(date.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate());
            }
            movieDTO.setPlace(row.getCell(3).getStringCellValue());

            String party = row.getCell(4).getStringCellValue();

            List<String> persons = splitPartyByPersons(party);
            movieDTO.setPersons(persons);
            movieDTO.setScore((int) row.getCell(5).getNumericCellValue());

            moviesDTO.add(movieDTO);
        }
        return moviesDTO;
    }

    private List<String> splitPartyByPersons(String party) {
        if (StringUtils.isEmpty(party)) {
            return new ArrayList<>();
        }
        List<String> names = Arrays.asList(party.split(","));
        return names.stream().map(String::trim).collect(Collectors.toList());
    }
}
