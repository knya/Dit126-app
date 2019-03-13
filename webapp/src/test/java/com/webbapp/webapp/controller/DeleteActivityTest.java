
package com.webbapp.webapp.controller;

import com.webbapp.webapp.model.*;
import com.webbapp.webapp.util.AppUserSession;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class DeleteActivityTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @InjectMocks
    private ShowActivitys showActivitys;


    @Mock
    private AppUserSession login;

    @Mock
    private ShowActivityFacade showActivityFacade;

    @Mock
    private LocationFacade locationFacade;

    @Test
    public void testDeleteNotLoggedIn() {
        when(login.getUser()).thenReturn(null);
        ActivityEntity activityEntity = new ActivityEntity();
        AppUserEntity owner = new AppUserEntity();
        owner.setUserName("alice");
        owner.setUserPassword("alicePassword");

        activityEntity.setAppUsersByUserId(owner);
        activityEntity.setLocationByLocationId(new LocationEntity());
        showActivitys.setActivityEntity(activityEntity);

        String result = showActivitys.delete();
        String expected = null;

        Assert.assertEquals(result, expected);

        verify(showActivityFacade, never()).remove(activityEntity);
    }


    @Test
    public void testDeleteNotOwner() {
        AppUserEntity loggedIn = new AppUserEntity();
        loggedIn.setUserName("alice");
        loggedIn.setUserPassword("alicePassword");

        AppUserEntity activityOwner = new AppUserEntity();
        activityOwner.setUserName("bob");
        activityOwner.setUserPassword("bobPassword");

        when(login.getUser()).thenReturn(loggedIn);
        ActivityEntity activityEntity = new ActivityEntity();
        activityEntity.setAppUsersByUserId(activityOwner);

        String result = showActivitys.delete();
        String expected = null;

        Assert.assertEquals(result, expected);

        verify(showActivityFacade, never()).remove(activityEntity);
    }

    @Test
    public void testDeleteNonExistingActivivty() {
        AppUserEntity user = new AppUserEntity();
        user.setUserName("alice");
        user.setUserPassword("alicePassword");

        when(login.getUser()).thenReturn(user);
        ActivityEntity activityEntity = new ActivityEntity();
        activityEntity.setAppUsersByUserId(user);

        LocationEntity locationEntity = new LocationEntity();

        activityEntity.setLocationByLocationId(locationEntity);

        showActivitys.setActivityid(0);
        when(showActivityFacade.find(1)).thenReturn(null);

        doNothing().when(locationFacade).remove(locationEntity);
        showActivitys.onload();
        String result = showActivitys.delete();
        String expected = null;

        Assert.assertEquals(result, expected);

        verify(showActivityFacade, never()).remove(argThat(ae -> ae.equals(activityEntity)));
    }

    @Test
    public void testDelete() {
        AppUserEntity user = new AppUserEntity();
        user.setUserName("alice");
        user.setUserPassword("alicePassword");

        when(login.getUser()).thenReturn(user);
        ActivityEntity activityEntity = new ActivityEntity();
        activityEntity.setAppUsersByUserId(user);

        LocationEntity locationEntity = new LocationEntity();

        activityEntity.setLocationByLocationId(locationEntity);

        showActivitys.setActivityid(0);
        when(showActivityFacade.find(0)).thenReturn(activityEntity);

        doNothing().when(locationFacade).remove(locationEntity);
        showActivitys.onload();
        String result = showActivitys.delete();
        String expected = "index";

        Assert.assertEquals(result, expected);

        verify(showActivityFacade).remove(argThat(ae -> ae.equals(activityEntity)));
    }


}