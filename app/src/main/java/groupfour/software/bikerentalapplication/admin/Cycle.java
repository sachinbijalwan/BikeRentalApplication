package groupfour.software.bikerentalapplication.admin;

public class Cycle {
    private final String cycleid;
    private final String locationid;

    Cycle(String cid, String lid) {
        cycleid = cid;
        locationid = lid;
    }

    public String getCycleid() {
        return cycleid;
    }

    public String getLocationid() {
        return locationid;
    }
}
