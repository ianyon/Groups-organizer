
package cl.dcc.Groups_Organizer.data;

import java.util.ArrayList;
import java.util.Date;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import org.parceler.Generated;
import org.parceler.InjectionUtil;
import org.parceler.ParcelWrapper;

@Generated(value = "org.parceler.ParcelAnnotationProcessor", date = "2014-05-22T17:30-0500")
public class Event$$Parcelable
    implements Parcelable, ParcelWrapper<cl.dcc.Groups_Organizer.data.Event>
{

    private cl.dcc.Groups_Organizer.data.Event event$$0;
    @SuppressWarnings("UnusedDeclaration")
    public final static Event$$Parcelable.Creator$$0 CREATOR = new Event$$Parcelable.Creator$$0();

    public Event$$Parcelable(android.os.Parcel parcel$$0) {
        event$$0 = new cl.dcc.Groups_Organizer.data.Event();
        int int$$0 = parcel$$0 .readInt();
        ArrayList<cl.dcc.Groups_Organizer.data.Person> list$$0;
        if (int$$0 < 0) {
            list$$0 = null;
        } else {
            list$$0 = new ArrayList<cl.dcc.Groups_Organizer.data.Person>();
            for (int int$$1 = 0; (int$$1 <int$$0); int$$1 ++) {
                list$$0 .add(((ParcelWrapper<cl.dcc.Groups_Organizer.data.Person> ) parcel$$0 .readParcelable(Event$$Parcelable.class.getClassLoader())).getParcel());
            }
        }
        event$$0 .guestList = list$$0;
        InjectionUtil.setField(cl.dcc.Groups_Organizer.data.Event.class, event$$0, "confirmedCount", parcel$$0 .readInt());
        int int$$2 = parcel$$0 .readInt();
        ArrayList<cl.dcc.Groups_Organizer.data.Person> list$$1;
        if (int$$2 < 0) {
            list$$1 = null;
        } else {
            list$$1 = new ArrayList<cl.dcc.Groups_Organizer.data.Person>();
            for (int int$$3 = 0; (int$$3 <int$$2); int$$3 ++) {
                list$$1 .add(((ParcelWrapper<cl.dcc.Groups_Organizer.data.Person> ) parcel$$0 .readParcelable(Event$$Parcelable.class.getClassLoader())).getParcel());
            }
        }
        event$$0 .confirmed = list$$1;
        event$$0 .location = parcel$$0 .readString();
        InjectionUtil.setField(cl.dcc.Groups_Organizer.data.Event.class, event$$0, "guestListCount", parcel$$0 .readInt());
        event$$0 .description = parcel$$0 .readString();
        event$$0 .name = parcel$$0 .readString();
        event$$0 .datetime = ((Date) parcel$$0 .readSerializable());
    }

    public Event$$Parcelable(cl.dcc.Groups_Organizer.data.Event event$$1) {
        event$$0 = event$$1;
    }

    @Override
    public void writeToParcel(android.os.Parcel parcel$$1, int flags) {
        if (event$$0 .guestList == null) {
            parcel$$1 .writeInt(-1);
        } else {
            parcel$$1 .writeInt(event$$0 .guestList.size());
            for (cl.dcc.Groups_Organizer.data.Person person$$0 : ((java.util.List<cl.dcc.Groups_Organizer.data.Person> ) event$$0 .guestList)) {
                parcel$$1 .writeParcelable(org.parceler.Parcels.wrap(person$$0), flags);
            }
        }
        parcel$$1 .writeInt(InjectionUtil.getField(int.class, cl.dcc.Groups_Organizer.data.Event.class, event$$0, "confirmedCount"));
        if (event$$0 .confirmed == null) {
            parcel$$1 .writeInt(-1);
        } else {
            parcel$$1 .writeInt(event$$0 .confirmed.size());
            for (cl.dcc.Groups_Organizer.data.Person person$$1 : ((java.util.List<cl.dcc.Groups_Organizer.data.Person> ) event$$0 .confirmed)) {
                parcel$$1 .writeParcelable(org.parceler.Parcels.wrap(person$$1), flags);
            }
        }
        parcel$$1 .writeString(event$$0 .location);
        parcel$$1 .writeInt(InjectionUtil.getField(int.class, cl.dcc.Groups_Organizer.data.Event.class, event$$0, "guestListCount"));
        parcel$$1 .writeString(event$$0 .description);
        parcel$$1 .writeString(event$$0 .name);
        parcel$$1 .writeSerializable(event$$0 .datetime);
    }

    @Override
    public int describeContents() {
        return  0;
    }

    @Override
    public cl.dcc.Groups_Organizer.data.Event getParcel() {
        return event$$0;
    }

    private final static class Creator$$0
        implements Creator<Event$$Parcelable>
    {


        @Override
        public Event$$Parcelable createFromParcel(android.os.Parcel parcel$$2) {
            return new Event$$Parcelable(parcel$$2);
        }

        @Override
        public Event$$Parcelable[] newArray(int size) {
            return new Event$$Parcelable[size] ;
        }

    }

}
