package io.github.mstahv.sliders;

import com.fasterxml.jackson.annotation.JsonValue;

public record RangeSliderValue(double min, double max) {

    @JsonValue
    public double[] toArray() {
        return new double[]{min, max};
    }
}
