package io.github.mstahv.sliders;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RangeSliderConfig {

    private Double min = null; // default is 0
    private Double max = null; // default is 100
    private Double step = null; // default is 1
    private RangeSliderValue value = null;
    private Boolean disabled;
    private Boolean rangeSlideDisabled;
    private Boolean[] thumbsDisabled;
    private Orientation orientation;

    public enum Orientation {
        HORIZONTAL, VERTICAL;

        public String toString() {
            return name().toLowerCase();
        }
    }

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }

    public Double getStep() {
        return step;
    }

    public void setStep(Double step) {
        this.step = step;
    }

    public RangeSliderValue getValue() {
        return value;
    }

    public void setValue(RangeSliderValue value) {
        this.value = value;
    }

    public Boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public Boolean isRangeSlideDisabled() {
        return rangeSlideDisabled;
    }

    public void setRangeSlideDisabled(boolean rangeSlideDisabled) {
        this.rangeSlideDisabled = rangeSlideDisabled;
    }

    public Boolean[] getThumbsDisabled() {
        return thumbsDisabled;
    }

    public void setThumbsDisabled(Boolean[] thumbsDisabled) {
        if (thumbsDisabled.length == 1) {
            this.thumbsDisabled = new Boolean[]{thumbsDisabled[0], thumbsDisabled[0]};
        } else if (thumbsDisabled.length == 2) {
            this.thumbsDisabled = thumbsDisabled;
        } else {
            throw new IllegalArgumentException("ThumbsDisabled array must have one or two elements.");
        }
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

}
