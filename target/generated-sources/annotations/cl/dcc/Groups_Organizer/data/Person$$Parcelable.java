
package cl.dcc.Groups_Organizer.data;

import android.os.Parcelable;
import android.os.Parcelable.Creator;
import org.parceler.Generated;
import org.parceler.ParcelWrapper;

@Generated(value = "org.parceler.ParcelAnnotationProcessor", date = "2014-05-23T11:33-0500")
public class Person$$Parcelable
    implements Parcelable, ParcelWrapper<cl.dcc.Groups_Organizer.data.Person>
{

    private cl.dcc.Groups_Organizer.data.Person person$$2;
    @SuppressWarnings("UnusedDeclaration")
    public final static Person$$Parcelable.Creator$$1 CREATOR = new Person$$Parcelable.Creator$$1();

    public Person$$Parcelable(android.os.Parcel parcel$$3) {
        person$$2 = new cl.dcc.Groups_Organizer.data.Person();
        person$$2 .email = parcel$$3 .readString();
        person$$2 .age = parcel$$3 .readInt();
        person$$2 .name = parcel$$3 .readString();
        person$$2 .gender = parcel$$3 .readString();
    }

    public Person$$Parcelable(cl.dcc.Groups_Organizer.data.Person person$$3) {
        person$$2 = person$$3;
    }

    @Override
    public void writeToParcel(android.os.Parcel parcel$$4, int flags) {
        parcel$$4 .writeString(person$$2 .email);
        parcel$$4 .writeInt(person$$2 .age);
        parcel$$4 .writeString(person$$2 .name);
        parcel$$4 .writeString(person$$2 .gender);
    }

    @Override
    public int describeContents() {
        return  0;
    }

    @Override
    public cl.dcc.Groups_Organizer.data.Person getParcel() {
        return person$$2;
    }

    private final static class Creator$$1
        implements Creator<Person$$Parcelable>
    {


        @Override
        public Person$$Parcelable createFromParcel(android.os.Parcel parcel$$5) {
            return new Person$$Parcelable(parcel$$5);
        }

        @Override
        public Person$$Parcelable[] newArray(int size) {
            return new Person$$Parcelable[size] ;
        }

    }

}
