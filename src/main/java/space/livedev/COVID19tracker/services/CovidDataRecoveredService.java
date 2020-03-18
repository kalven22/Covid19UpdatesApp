package space.livedev.COVID19tracker.services;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import space.livedev.COVID19tracker.models.CovidDataConfirmedModel;
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
    public List<CovidDataRecoveredModel> getAllRecoveredData() {
        return allRecoveredData;
    }

    private List<CovidDataRecoveredModel> allCanadaRecoveredData = new ArrayList<>();
    public List<CovidDataRecoveredModel> getAllCanadaRecoveredData() {
        return allCanadaRecoveredData;
    }

    private List<CovidDataRecoveredModel> allUSRecoveredData = new ArrayList<>();
    public List<CovidDataRecoveredModel> getAllUSRecoveredData() {
        return allUSRecoveredData;
    }

    private List<CovidDataRecoveredModel> allIndiaRecoveredData = new ArrayList<>();
    public List<CovidDataRecoveredModel> getAllIndiaRecoveredData() {
        return allIndiaRecoveredData;
    }

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

        this.totalCanadaRecoveredCases = totalCasesFunc("Canada");
        this.totalUSRecoveredCases = totalCasesFunc("US");
        this.totalIndiaRecoveredCases = totalCasesFunc("India");

        this.allCanadaRecoveredData = allDataByCountry("Canada");
        Collections.sort(allCanadaRecoveredData);

        this.allUSRecoveredData = allDataByCountry("US");
        Collections.sort(allUSRecoveredData);

        this.allIndiaRecoveredData = allDataByCountry("India");
        Collections.sort(allIndiaRecoveredData);
    }



    public List<CovidDataRecoveredModel> allDataByCountry(String country){
        List<CovidDataRecoveredModel> data = new ArrayList<>();
        for (CovidDataRecoveredModel c : allRecoveredData)
        {
            if (c.getCountry().equals(country))
            {
                data.add(c);
            }
        }

        return data;
    }
    public int totalCasesFunc(String country)
    {
        int cases = 0;
        for (CovidDataRecoveredModel c : allRecoveredData)
        {
            if (c.getCountry().equals(country))
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
