package space.livedev.COVID19tracker.models;

public class CoronaDataModel
{
    private String country;
    private String province;
    private int totalCases;
    private int newCases;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public int getTotalCases() {
        return totalCases;
    }

    public void setTotalCases(int totalCases) {
        this.totalCases = totalCases;
    }

    public int getNewCases() {
        return newCases;
    }

    public void setNewCases(int newCases) {
        this.newCases = newCases;
    }

    @Override
    public String toString() {
        return "CoronaDataModel{" +
                "country='" + country + '\'' +
                ", province='" + province + '\'' +
                ", totalCases=" + totalCases +
                ", newCases=" + newCases +
                '}';
    }
}
