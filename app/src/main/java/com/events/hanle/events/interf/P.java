package com.events.hanle.events.interf;

import com.events.hanle.events.Model.AlreadyInvitedUser;
import com.events.hanle.events.Model.Attending;

/**
 * Created by Hanle on 3/30/2017.
 */

public interface P {
    public int getValue();
}


class PA implements P {

    private final AlreadyInvitedUser a;

    PA(AlreadyInvitedUser a) {
        this.a = a;
    }

    @Override
    public int getValue() {
        return a.getAlreadyinvited();
    }
}

class PB implements P {

    private final Attending b;

    PB(Attending b) {
        this.b = b;
    }

    @Override
    public int getValue() {
        return b.getId();
    }
}