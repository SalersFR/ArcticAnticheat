package honeybadger.ac.event.client;

import com.comphenix.packetwrapper.WrapperPlayClientFlying;
import honeybadger.ac.event.Event;
import lombok.Getter;



@Getter
public class FlyingEvent extends Event {

    private final WrapperPlayClientFlying wrapper;
    private final boolean onGround;
    private final long time;

    public FlyingEvent(WrapperPlayClientFlying wrapper) {
        this.wrapper = wrapper;
        this.onGround = wrapper.getOnGround();
        this.time = System.currentTimeMillis();

    }
}
