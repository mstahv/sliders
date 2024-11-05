package io.github.mstahv.sliders;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route
public class SliderView extends VerticalLayout {
    public SliderView() {

        IntSlider doubleSlider = new IntSlider("Select a value", 0, 100, 69);
        add(doubleSlider);
        doubleSlider.addValueChangeListener(e -> {
            Notification.show("Value changed to " + e.getValue());
        });
    }
}
