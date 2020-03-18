package space.livedev.COVID19tracker.models;

public class CovidDataRecoveredModel implements Comparable<CovidDataRecoveredModel>
{
    private String country;
    private String province;
    private int totalRecovered;
    private int newlyRecovered;

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

    public int getTotalRecovered() {
        return totalRecovered;
    }

    public void setTotalRecovered(int totalRecovered) {
        this.totalRecovered = totalRecovered;
    }

    public int getNewlyRecovered() {
        return newlyRecovered;
    }

    public void setNewlyRecovered(int newlyRecovered) {
        this.newlyRecovered = newlyRecovered;
    }

    @Override
    public int compareTo(CovidDataRecoveredModel other) {
        if (this.getTotalRecovered() > other.getTotalRecovered())
        {
            return -1;
        }
        else if (this.getTotalRecovered() == other.getTotalRecovered())
        {
            return 0;
        }
        else { return 1;}
    }

    @Override
    public String toString() {
        return "CovidDataRecoveredModel{" +
                "country='" + country + '\'' +
                ", province='" + province + '\'' +
                ", totalRecovered=" + totalRecovered +
                ", newlyRecovered=" + newlyRecovered +
                '}';
    }
}
