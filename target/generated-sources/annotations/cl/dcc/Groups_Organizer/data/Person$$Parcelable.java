
package cl.dcc.Groups_Organizer.data;

import android.os.Parcelable;
import android.os.Parcelable.Creator;
import org.parceler.Generated;
import org.parceler.ParcelWrapper;

@Generated(value = "org.parceler.ParcelAnnotationProcessor", date = "2014-05-22T17:52-0500")
public class Person$$Parcelable
    implements Parcelable, ParcelWrapper<cl.dcc.Groups_Organizer.data.Person>
{

    private cl.dcc.Groups_Organizer.data.Person person$$0;
    @SuppressWarnings("UnusedDeclaration")
    public final static Person$$Parcelable.Creator$$0 CREATOR = new Person$$Parcelable.Creator$$0();

    public Person$$Parcelable(android.os.Parcel parcel$$0) {
        person$$0 = new cl.dcc.Groups_Organizer.data.Person();
        person$$0 .email = parcel$$0 .readString();
        person$$0 .age = parcel$$0 .readInt();
        person$$0 .name = parcel$$0 .readString();
        person$$0 .gender = parcel$$0 .readString();
    }

    public Person$$Parcelable(cl.dcc.Groups_Organizer.data.Person person$$1) {
        person$$0 = person$$1;
    }

    @Override
    public void writeToParcel(android.os.Parcel parcel$$1, int flags) {
        parcel$$1 .writeString(person$$0 .email);
        parcel$$1 .writeInt(person$$0 .age);
        parcel$$1 .writeString(person$$0 .name);
        parcel$$1 .writeString(person$$0 .gender);
    }

    @Override
    public int describeContents() {
        return  0;
    }

    @Override
    public cl.dcc.Groups_Organizer.data.Person getParcel() {
        return person$$0;
    }

    private final static class Creator$$0
        implements Creator<Person$$Parcelable>
    {


        @Override
        public Person$$Parcelable createFromParcel(android.os.Parcel parcel$$2) {
            return new Person$$Parcelable(parcel$$2);
        }

        @Override
        public Person$$Parcelable[] newArray(int size) {
            return new Person$$Parcelable[size] ;
        }

    }

}
