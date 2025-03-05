package com.dt042g.photochronicle.controller;

import com.dt042g.photochronicle.model.ChronicleModel;
import com.dt042g.photochronicle.view.ChronicleView;

public class ChronicleController {
    private final ChronicleView view;
    private final ChronicleModel model;

    public ChronicleController() {
        view = new ChronicleView();
        model = new ChronicleModel();
    }

    public void initialize() {

    }
}
