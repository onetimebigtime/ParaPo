package com.example.parapo.ui.home;

public class TripsUser {
    String route, plate_number;
    int seat_1, seat_2, seat_3, seat_4, seat_5, seat_6, seat_7, seat_8, seat_9, seat_10, seatCount;
    double longitude, latitude;
    boolean is_online;

    public String getRoute() {
        return route;
    }

    public String getPlate_number() {
        return plate_number;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public boolean isIs_online() {
        return is_online;
    }

    public int getSeatCount() {
        seatCount = this.seat_1 + this.seat_2 + this.seat_3 + this.seat_4 + this.seat_5 + this.seat_6 + this.seat_7 + this.seat_8 + this.seat_9 + this.seat_10;
        return seatCount;
    }

    public int getSeat_1() {
        return seat_1;
    }

    public int getSeat_2() {
        return seat_2;
    }

    public int getSeat_3() {
        return seat_3;
    }

    public int getSeat_4() {
        return seat_4;
    }

    public int getSeat_5() {
        return seat_5;
    }

    public int getSeat_6() {
        return seat_6;
    }

    public int getSeat_7() {
        return seat_7;
    }

    public int getSeat_8() {
        return seat_8;
    }

    public int getSeat_9() {
        return seat_9;
    }

    public int getSeat_10() {
        return seat_10;
    }
}
