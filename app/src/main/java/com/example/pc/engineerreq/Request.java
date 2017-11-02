package com.example.pc.engineerreq;

/**
 * Created by pc on 2017-08-09.
 */

public class Request {

    private String email;
    private String phoneNumber;
    private String address;
    private String problem;
    private String status;
    private String temp_key;
    private String lat;
    private String lng;

    public Request(String email, String phoneNumber, String address, String problem, String status, String temp_key, String lat, String lng){
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.problem = problem;
        this.status = status;
        this.temp_key = temp_key;
        this.lat = lat;
        this.lng = lng;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getTemp_key() {
        return temp_key;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getProblem() {
        return problem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Request request = (Request) o;

        if (email != null ? !email.equals(request.email) : request.email != null) return false;
        if (phoneNumber != null ? !phoneNumber.equals(request.phoneNumber) : request.phoneNumber != null)
            return false;
        if (address != null ? !address.equals(request.address) : request.address != null)
            return false;
        if (problem != null ? !problem.equals(request.problem) : request.problem != null)
            return false;
        return temp_key != null ? temp_key.equals(request.temp_key) : request.temp_key == null;

    }

    @Override
    public int hashCode() {
        int result = email != null ? email.hashCode() : 0;
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (problem != null ? problem.hashCode() : 0);
        result = 31 * result + (temp_key != null ? temp_key.hashCode() : 0);
        return result;
    }
}
