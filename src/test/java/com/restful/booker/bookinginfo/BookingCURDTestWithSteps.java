package com.restful.booker.bookinginfo;

import com.restful.booker.model.BookingDates;
import com.restful.booker.testbase.TestBaseBooking;
import com.restful.booker.utils.TestUtils;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Title;
import net.thucydides.core.annotations.WithTag;
import net.thucydides.core.annotations.WithTags;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static org.hamcrest.Matchers.hasValue;


@RunWith(SerenityRunner.class)
public class BookingCURDTestWithSteps extends TestBaseBooking {


    static String firstname = "prime" + TestUtils.getRandomValue();
    static String lastname = "Tester" + TestUtils.getRandomValue();
    static Integer totalprice = 50;
    static Boolean depositpaid = true;
    static Date checkin = new Date(122, 01, 21);
    static Date checkout = new Date(122, 01, 24);
    static String additionalneeds = "Lunch";
    static int bookingID;

    @Steps
    BookingSteps bookingSteps;

    @WithTags({
            @WithTag("Bookingfeature:SMOKE"),
            @WithTag("Bookingfeature:POSITIVE")
    })

    @Title("This will create new booking")
    @Test
    public void test001() {

        BookingDates bookingDates = new BookingDates();
        bookingDates.setCheckin(checkin);
        bookingDates.setCheckout(checkout);
        ValidatableResponse response = bookingSteps.createBooking(firstname, lastname, totalprice, depositpaid, bookingDates, additionalneeds);
        response.log().all().statusCode(200);

    }
    @WithTags({
            @WithTag("Bookingfeature:SMOKE"),
            @WithTag("Bookingfeature:NEGATIVE")
    })

    @Title("Verify if the Booking was done correctly")
    @Test
    public void test002() {
        ArrayList<HashMap<String, Object>> value = bookingSteps.getBookingInfoByFirstname(firstname);
        Assert.assertThat(value.get(0), hasValue(firstname));
        bookingID = (Integer) value.get(0).get("bookingid");
        System.out.println(bookingID);
    }

    @Title("Update the booking and verify the updated information")
    @Test
    public void test003(){

        firstname = firstname + "(Updated)";
        lastname = lastname + "(Updated)";
        additionalneeds = "Vegetarian Meal";

        BookingDates bookingDates = new BookingDates();
        bookingDates.setCheckin(checkin);
        bookingDates.setCheckout(checkout);
        bookingSteps.updateBooking(bookingID,firstname,lastname,totalprice,depositpaid,bookingDates,additionalneeds).log().all().statusCode(200);
        ArrayList<HashMap<String,Object>> value = bookingSteps.getBookingInfoByFirstname(firstname);
        Assert.assertThat(value.get(0),hasValue(firstname));
    }
    @Title("Delete the Booking and verify if the Booking is deleted!")
    @Test
    public void test004() {
        bookingSteps.deleteBooking(bookingID).statusCode(201);//it should be 204
        bookingSteps.getBookingById(bookingID).statusCode(404);
    }
    }
