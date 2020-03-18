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
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class CovidDataConfirmedService
{
    private String COVID_CONFIRMED_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_19-covid-Confirmed.csv";
    private List<CovidDataConfirmedModel> allData = new ArrayList<>();
    public List<CovidDataConfirmedModel> getAllData() {
        return allData;
    }

    private List<CovidDataConfirmedModel> allCanadaData = new ArrayList<>();
    public List<CovidDataConfirmedModel> getAllCanadaData() {
        return allCanadaData;
    }

    private List<CovidDataConfirmedModel> allUSData = new ArrayList<>();
    public List<CovidDataConfirmedModel> getAllUSData() {
        return allUSData;
    }

    private List<CovidDataConfirmedModel> allIndiaData = new ArrayList<>();
    public List<CovidDataConfirmedModel> getAllIndiaData() {
        return allIndiaData;
    }

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

    private int totalIndiaCases;
    public int getTotalIndiaCases() {
        return totalIndiaCases;
    }


    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    public void getData() throws Exception
    {

        List<CovidDataConfirmedModel> allDataTemp = new ArrayList<>();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(COVID_CONFIRMED_URL)).build();

        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        StringReader stringReader = new StringReader(httpResponse.body());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(stringReader);
        for (CSVRecord record : records)
        {
            CovidDataConfirmedModel dataModel = new CovidDataConfirmedModel();

            dataModel.setCountry(record.get("Country/Region"));
            dataModel.setProvince(record.get("Province/State"));

            int recordSize = record.size();

            int totalCases = stringToInt(record.get(recordSize-1));
            dataModel.setTotalCases(totalCases);

            int newCases = stringToInt(record.get(recordSize-1)) - stringToInt(record.get(recordSize-2));
            if (newCases > 0){
                dataModel.setNewCases(newCases);
            }
            else
            {
                dataModel.setNewCases(0);
            }

            allDataTemp.add(dataModel);
        }
        this.allData=allDataTemp;

        Collections.sort(allData);

        this.totalGlobalCases = allData.stream().mapToInt(i -> i.getTotalCases()).sum();

        this.totalCanadaCases = totalCasesFunc("Canada");
        this.totalUSCases = totalCasesFunc("US");
        this.totalIndiaCases = totalCasesFunc("India");

        this.allCanadaData = allDataByCountry("Canada");
        Collections.sort(allCanadaData);

        this.allUSData = allDataByCountry("US");
        Collections.sort(allUSData);

        this.allIndiaData = allDataByCountry("India");
        Collections.sort(allIndiaData);

    }

    public List<CovidDataConfirmedModel> allDataByCountry(String country){
        List<CovidDataConfirmedModel> data = new ArrayList<>();
        for (CovidDataConfirmedModel c : allData)
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
        for (CovidDataConfirmedModel c : allData)
        {
            if (c.getCountry().equals(country))
            {
                cases +=c.getTotalCases();
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
