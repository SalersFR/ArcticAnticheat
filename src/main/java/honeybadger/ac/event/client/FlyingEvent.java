package honeybadger.ac.event.client;

import honeybadger.ac.event.Event;
import lombok.Getter;


@Getter
public class FlyingEvent extends Event {


    private final long time;

    public FlyingEvent(long time) {

        this.time = time;

    }
}
