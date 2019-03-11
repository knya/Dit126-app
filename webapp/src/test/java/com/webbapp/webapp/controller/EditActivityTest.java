package com.webbapp.webapp.controller;

import com.webbapp.webapp.model.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class EditActivityTest {

    @InjectMocks
    private EditActivity ac;

    @Mock
    private AddActivityFacade activityFacade;
    @Mock
    private LocationFacade locationFacade;
    @Mock
    private Login login;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Getter setter test")
    public void gettersTest(){
        AppUsersEntity user = new AppUsersEntity();
        user.setUserName("Alice");
        user.setUserPassword("alice_password");
        when(login.getUser()).thenReturn(user);

        ac.setTitle("Hello");
        ac.setDescription("Desc");
        ac.setLat("57.710532072641925");
        ac.setLng("11.958837619599421");
        ac.setType(ActivityType.TYPE5);

        assertEquals("Hello", ac.getTitle());
        assertEquals("Desc", ac.getDescription());
        assertEquals("57.710532072641925", ac.getLat());
        assertEquals("11.958837619599421", ac.getLng());
        assertEquals(ac.getType(), ActivityType.TYPE5);
    }

    @Test
    @DisplayName("Loading test")
    public void LoadingActivity(){
        AppUsersEntity user = new AppUsersEntity();
        user.setUserName("Alice");
        user.setUserPassword("alice_password");
        when(login.getUser()).thenReturn(user);

        ActivityEntity activity = new ActivityEntity();
        activity.setType(ActivityType.TYPE1.name());
        activity.setTitle("Test");
        activity.setDescription("Desc");
        LocationEntity loc = new LocationEntity();
        loc.setLatitude(1);
        loc.setLongitude(2);
        activity.setLocationByLocationId(loc);
        activity.setAppUsersByUserId(user);

        ac.setId("1");
        when(activityFacade.find(1)).thenReturn(activity);

        ac.onLoad();

        assertEquals("Test", ac.getTitle());
        assertEquals("Desc", ac.getDescription());
        assertEquals("1.0", ac.getLat());
        assertEquals("2.0", ac.getLng());
        assertEquals(ActivityType.TYPE1, ac.getType());

    }

    @Test
    @DisplayName("Add Test")
    public void testActivity(){
        AppUsersEntity user = new AppUsersEntity();
        user.setUserName("Alice");
        user.setUserPassword("alice_password");
        when(login.getUser()).thenReturn(user);

        ActivityEntity activity = new ActivityEntity();
        activity.setType(ActivityType.TYPE1.name());
        activity.setTitle("Test");
        activity.setDescription("Desc");
        LocationEntity loc = new LocationEntity();
        loc.setLatitude(1);
        loc.setLongitude(2);
        activity.setLocationByLocationId(loc);
        activity.setAppUsersByUserId(user);

        ac.setId("1");
        when(activityFacade.find(1)).thenReturn(activity);

        ac.onLoad();

        ac.edit();

        verify(activityFacade).edit(argThat((ActivityEntity ac) -> ac.getTitle().equals("Test")
                                                                                && ac.getDescription().equals("Desc")
                                                                                && ac.getType().equals(ActivityType.TYPE1.name())
                                                                                && ac.getLocationByLocationId().getLatitude() == 1.0
                                                                                && ac.getLocationByLocationId().getLongitude() == 2.0
                                                                                && ac.getAppUsersByUserId().getUserName().equals("Alice")));

    }

    @Test
    @DisplayName("Edit invalid activity")
    public void addInvalidActivity(){
        AppUsersEntity user = new AppUsersEntity();
        user.setUserName("Alice");
        user.setUserPassword("alice_password");
        when(login.getUser()).thenReturn(user);

        ActivityEntity activity = new ActivityEntity();
        activity.setType(ActivityType.TYPE1.name());
        activity.setTitle(null);
        activity.setDescription("Desc");
        LocationEntity loc = new LocationEntity();
        loc.setLatitude(1);
        loc.setLongitude(2);
        activity.setLocationByLocationId(loc);
        activity.setAppUsersByUserId(user);

        ac.setId("1");
        when(activityFacade.find(1)).thenReturn(activity);

        ac.onLoad();

        ac.edit();

        verify(activityFacade, never()).edit(Mockito.any());
    }


    @Test
    @DisplayName("Add not loggedin activity")
    public void notLoggedinActivity(){
        when(login.getUser()).thenReturn(null);

        ActivityEntity activity = new ActivityEntity();
        activity.setType(ActivityType.TYPE1.name());
        activity.setTitle("Title");
        activity.setDescription("Desc");
        LocationEntity loc = new LocationEntity();
        loc.setLatitude(1);
        loc.setLongitude(2);
        activity.setLocationByLocationId(loc);
        activity.setAppUsersByUserId(null);

        ac.setId("1");
        when(activityFacade.find(1)).thenReturn(activity);

        String redirect = ac.onLoad();

        assertEquals("index.xhtml", redirect);

        ac.edit();
        verify(activityFacade, never()).edit(Mockito.any());
    }

    @Test
    @DisplayName("City test")
    public void correctCityName(){
        AppUsersEntity user = new AppUsersEntity();
        user.setUserName("Alice");
        user.setUserPassword("alice_password");
        when(login.getUser()).thenReturn(user);

        ActivityEntity activity = new ActivityEntity();
        activity.setType(ActivityType.TYPE1.name());
        activity.setTitle("Test");
        activity.setDescription("Desc");
        LocationEntity loc = new LocationEntity();
        loc.setLatitude(57.710532072641925);
        loc.setLongitude(11.958837619599421);
        activity.setLocationByLocationId(loc);
        activity.setAppUsersByUserId(user);

        ac.setId("1");
        when(activityFacade.find(1)).thenReturn(activity);

        ac.onLoad();

        ac.edit();

        verify(activityFacade).edit(argThat((ActivityEntity ac) -> ac.getTitle().equals("Test")
                && ac.getDescription().equals("Desc")
                && ac.getType().equals(ActivityType.TYPE1.name())
                && ac.getLocationByLocationId().getLatitude() == 57.710532072641925
                && ac.getLocationByLocationId().getLongitude() == 11.958837619599421
                && ac.getAppUsersByUserId().getUserName().equals("Alice")
                && ac.getLocationByLocationId().getCity().equals("gothenburg")));

    }
}