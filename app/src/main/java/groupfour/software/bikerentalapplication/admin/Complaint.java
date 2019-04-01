package groupfour.software.bikerentalapplication.admin;

public class Complaint {
    private int complaintid;
    private String complaint;
    private String User;
    Complaint(int cid, String comp, String user){
        complaintid=cid;
        complaint=comp;
        User=user;
    }
    public int getComplaintid(){
        return complaintid;
    }
    public String getComplaint(){
        return complaint;
    }
    public String getUser(){
        return User;
    }

}
