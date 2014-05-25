
package org.parceler;

import java.util.HashMap;
import java.util.Map;
import cl.dcc.Groups_Organizer.data.Event;
import cl.dcc.Groups_Organizer.data.Event$$Parcelable;

@Generated(value = "org.parceler.ParcelAnnotationProcessor", date = "2014-05-25T00:40-0400")
public class Parceler$$Parcels
    implements Repository<org.parceler.Parcels.ParcelableFactory>
{

    private final Map<Class, org.parceler.Parcels.ParcelableFactory> map$$0 = new HashMap<Class, org.parceler.Parcels.ParcelableFactory>();

    public Parceler$$Parcels() {
        map$$0 .put(Event.class, new Parceler$$Parcels.Event$$Parcelable$$0());
    }

    public Map<Class, org.parceler.Parcels.ParcelableFactory> get() {
        return map$$0;
    }

    private final static class Event$$Parcelable$$0
        implements org.parceler.Parcels.ParcelableFactory<Event>
    {


        @Override
        public Event$$Parcelable buildParcelable(Event input) {
            return new Event$$Parcelable(input);
        }

    }

}
