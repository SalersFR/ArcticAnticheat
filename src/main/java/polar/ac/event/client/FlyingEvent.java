package polar.ac.event.client;

import lombok.Getter;
import polar.ac.event.Event;


@Getter
public class FlyingEvent extends Event {


    private final long time;

    public FlyingEvent(long time) {

        this.time = time;

    }
}
