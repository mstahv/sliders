package io.github.mstahv.sliders;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IntSliderTest {
    @Disabled("Need to implement KaribuTest or similar?")
    @Test
    void getMax_shouldReturnCorrectMaxValue() {
        int min = 0;
        int max = 100;
        int initialValue = 50;
        IntSlider intSlider = new IntSlider("Test Slider", min, max, initialValue);

        double actualMax = intSlider.getMax();

        assertThat(actualMax).isEqualTo(max);
    }

}