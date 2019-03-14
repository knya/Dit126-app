package com.webbapp.webapp.controller;

import com.webbapp.webapp.model.ActivityEntity;
import com.webbapp.webapp.model.ActivityFacade;
import lombok.Getter;
import lombok.Setter;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Named
@ViewScoped
public class ListActivity implements Serializable {
    @Getter
    @Setter
    private String types;

    @Inject
    ActivityFacade activityFacade;


    public List<ActivityEntity> getList() {
        List<ActivityEntity> activities = activityFacade.findByTypes(Arrays.asList(types.split(",")));

        return activities;
    }
}