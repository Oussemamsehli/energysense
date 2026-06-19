package com.energysense.core_api.infrastructure.web.threshold;

public class ThresholdRequest {

    private double minValue;
    private double maxValue;

    public ThresholdRequest() {}

    public double getMinValue() { return minValue; }
    public double getMaxValue() { return maxValue; }
}