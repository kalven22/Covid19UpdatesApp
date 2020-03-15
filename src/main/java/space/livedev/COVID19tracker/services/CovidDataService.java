package space.livedev.COVID19tracker.services;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import space.livedev.COVID19tracker.models.CoronaDataModel;

import javax.annotation.PostConstruct;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class CovidDataService
{
    private String COVID_CONFIRMED = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_19-covid-Confirmed.csv";

    private List<CoronaDataModel> allData = new ArrayList<>();

    private int totalGlobalCases;

    public int getTotalGlobalCases() {
        return totalGlobalCases;
    }
    private int totalCanadaCases;
    public int getTotalCanadaCases() {
        return totalCanadaCases;
    }

    private int totalUSCases;
    public int getTotalUSCases() {
        return totalUSCases;
    }

    public List<CoronaDataModel> getAllData() {
        return allData;
    }
    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    public void getData() throws Exception
    {
        List<CoronaDataModel> allDataTemp = new ArrayList<>();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(COVID_CONFIRMED)).build();

        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        StringReader stringReader = new StringReader(httpResponse.body());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(stringReader);
        for (CSVRecord record : records) {
            CoronaDataModel dataModel = new CoronaDataModel();

            dataModel.setCountry(record.get("Country/Region"));
            dataModel.setProvince(record.get("Province/State"));

            int recordSize = record.size();

            int totalCases = stringToInt(record.get(recordSize-1));
            dataModel.setTotalCases(totalCases);

            int newCases = stringToInt(record.get(recordSize-1)) - stringToInt(record.get(recordSize-2));
            dataModel.setNewCases(newCases);

            allDataTemp.add(dataModel);
        }
        this.allData=allDataTemp;

        this.totalGlobalCases = allData.stream().mapToInt(i -> i.getTotalCases()).sum();

        this.totalCanadaCases = totalCanadaCasesFunc();
        this.totalUSCases = totalUSCasesFunc();
        System.out.println(totalCanadaCases);
    }

    public int totalCanadaCasesFunc()
    {
        int cases = 0;
        for (CoronaDataModel c : allData)
        {
            System.out.println(c.getCountry());
            if (c.getCountry().equals("Canada"))
            {
                cases +=c.getTotalCases();
            }
        }
        return cases;
    }

    public int totalUSCasesFunc()
    {
        int cases = 0;
        for (CoronaDataModel c : allData)
        {
            System.out.println(c.getCountry());
            if (c.getCountry().equals("US"))
            {
                cases +=c.getTotalCases();
            }
        }
        return cases;
    }

    public int stringToInt(String in)
    {
        return Integer.parseInt(in);
    }
}
