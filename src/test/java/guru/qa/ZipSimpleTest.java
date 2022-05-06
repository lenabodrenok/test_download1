package guru.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import static org.assertj.core.api.Assertions.assertThat;

public class ZipSimpleTest {
    ClassLoader classLoader = getClass().getClassLoader();

    @Test
    void zipReadTest() throws Exception {
        try (InputStream is = classLoader.getResourceAsStream("files/archive.zip");
             ZipInputStream zis = new ZipInputStream(is)) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().equals("file3.pdf")) {
                    {
                        PDF pdf = new PDF(zis);
                        assertThat(pdf.text).contains("salut");
                    }
                }
                else if (entry.getName().equals("file1.xlsx")) {
                    {
                        XLS xls = new XLS(zis);
                        String stringCellValue = xls.excel.getSheetAt(0).getRow(2).getCell(1).getStringCellValue();
                        assertThat(stringCellValue).contains("hola");
                    }
                }
                else if (entry.getName().equals("file2.csv")) {
                    CSVReader reader = new CSVReader(new InputStreamReader(zis));
                    List<String[]> content = reader.readAll();
                    assertThat(content.get(0)).contains("hello");
                }
            }
        }
    }
}
