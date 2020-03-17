package space.livedev.COVID19tracker.services;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import space.livedev.COVID19tracker.models.CovidDataConfirmedModel;

import javax.annotation.PostConstruct;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class CovidDataConfirmedService
{
    private String COVID_CONFIRMED_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_19-covid-Confirmed.csv";
    private List<CovidDataConfirmedModel> allData = new ArrayList<>();

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

    public List<CovidDataConfirmedModel> getAllData() {
        return allData;
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
        for (CSVRecord record : records) {
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

        this.totalGlobalCases = allData.stream().mapToInt(i -> i.getTotalCases()).sum();

        this.totalCanadaCases = totalCanadaCasesFunc();
        this.totalUSCases = totalUSCasesFunc();
        this.totalIndiaCases = totalIndiaCasesFunc();

    }

    public int totalCanadaCasesFunc()
    {
        int cases = 0;
        for (CovidDataConfirmedModel c : allData)
        {
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
        for (CovidDataConfirmedModel c : allData)
        {
            if (c.getCountry().equals("US"))
            {
                cases +=c.getTotalCases();
            }
        }
        return cases;
    }

    public int totalIndiaCasesFunc()
    {
        int cases = 0;
        for (CovidDataConfirmedModel c : allData)
        {
            if (c.getCountry().equals("India"))
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
