package groupfour.software.bikerentalapplication.admin;

class Complaint {
    private final int    complaintid;
    private final String complaint;
    private final String User;

    Complaint(int cid, String comp, String user) {
        complaintid = cid;
        complaint = comp;
        User = user;
    }

    public int getComplaintid() {
        return complaintid;
    }

    public String getComplaint() {
        return complaint;
    }

    public String getUser() {
        return User;
    }

}
