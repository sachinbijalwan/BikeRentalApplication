package groupfour.software.bikerentalapplication.models;

public class UserInformationModel {

    private Session     session;
    private UserModel   user;
    private PersonModel person;

    public UserInformationModel() {
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public PersonModel getPerson() {
        return person;
    }

    public void setPerson(PersonModel person) {
        this.person = person;
    }
}
