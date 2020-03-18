package space.livedev.COVID19tracker.services;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import space.livedev.COVID19tracker.models.CovidDataRecoveredModel;

import javax.annotation.PostConstruct;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CovidDataRecoveredService
{
    private String COVID_Recovered_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_19-covid-Recovered.csv";
    private List<CovidDataRecoveredModel> allRecoveredData = new ArrayList<>();

    private int totalGlobalRecoveredCases;

    public int getTotalGlobalRecoveredCases() {
        return totalGlobalRecoveredCases;
    }
    private int totalCanadaRecoveredCases;
    public int getTotalCanadaRecoveredCases() {
        return totalCanadaRecoveredCases;
    }

    private int totalUSRecoveredCases;
    public int getTotalUSRecoveredCases() {
        return totalUSRecoveredCases;
    }

    private int totalIndiaRecoveredCases;
    public int getTotalIndiaRecoveredCases() {
        return totalIndiaRecoveredCases;
    }

    public List<CovidDataRecoveredModel> getAllRecoveredData() {
        return allRecoveredData;
    }
    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    public void getData() throws Exception
    {

        List<CovidDataRecoveredModel> allDataTemp = new ArrayList<>();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(COVID_Recovered_URL)).build();

        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        StringReader stringReader = new StringReader(httpResponse.body());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(stringReader);
        for (CSVRecord record : records) {
            CovidDataRecoveredModel recoveredModel = new CovidDataRecoveredModel();

            recoveredModel.setCountry(record.get("Country/Region"));
            recoveredModel.setProvince(record.get("Province/State"));

            int recordSize = record.size();

            int totalRecoveredCases = stringToInt(record.get(recordSize-1));
            recoveredModel.setTotalRecovered(totalRecoveredCases);


            int newRecovered = stringToInt(record.get(recordSize-1)) - stringToInt(record.get(recordSize-2));
            recoveredModel.setNewlyRecovered(newRecovered);

            allDataTemp.add(recoveredModel);
        }
        this.allRecoveredData =allDataTemp;

        Collections.sort(allRecoveredData);

        this.totalGlobalRecoveredCases = allRecoveredData.stream().mapToInt(i->i.getTotalRecovered()).sum();

        this.totalCanadaRecoveredCases = totalCanadaCasesFunc();
        this.totalUSRecoveredCases = totalUSCasesFunc();
        this.totalIndiaRecoveredCases = totalIndiaCasesFunc();
    }

    public int totalCanadaCasesFunc()
    {
        int cases = 0;
        for (CovidDataRecoveredModel c : allRecoveredData)
        {
            if (c.getCountry().equals("Canada"))
            {
                cases +=c.getTotalRecovered();
            }
        }
        return cases;
    }

    public int totalUSCasesFunc()
    {
        int cases = 0;
        for (CovidDataRecoveredModel c : allRecoveredData)
        {
            if (c.getCountry().equals("US"))
            {
                cases +=c.getTotalRecovered();
            }
        }
        return cases;
    }

    public int totalIndiaCasesFunc()
    {
        int cases = 0;
        for (CovidDataRecoveredModel c : allRecoveredData)
        {
            if (c.getCountry().equals("India"))
            {
                cases +=c.getTotalRecovered();
            }
        }
        return cases;
    }

    public int stringToInt(String in)
    {
        if (in.length() == 0){
            return 0;
        }
        else{
            return Integer.parseInt(in);
        }

    }
}
