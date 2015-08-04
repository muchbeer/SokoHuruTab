package sokohuru.muchbeer.king.sokohurutab.Sokoni;

import android.os.Parcel;
import android.os.Parcelable;

import sokohuru.muchbeer.king.sokohurutab.loggin.L;

/**
 * Created by muchbeer on 6/4/2015.
 */
public class Soko implements Parcelable{

    private int id;
    private String title;
    private String image;
    private int releaseYear;
    private String rating;
    private String genre;

    //Sokoni
    private String name;
    private String price;
    private String desc;
    private String contact;
    private String location;
    private String username;
    private String pid;
    private String created;


    public Soko() {

    }

    public Soko(Parcel input) {
        id =input.readInt();
        title=input.readString();
        image=input.readString();
        rating=input.readString();
        genre=input.readString();

        name= input.readString();
        price=input.readString();
        desc=input.readString();
        contact=input.readString();
        location=input.readString();
        username=input.readString();
        created = input.readString();
    }

    public Soko(int id,
                String title,
                String image,
                int releaseYear,
                String rating,
                String genre,

                String name,
                String price,
                String desc,
                String contact,
                String location,
                String username,
                String created) {

        this.id = id;
        this.title = title;
        this.image = image;
        this.releaseYear = releaseYear;
        this.rating = rating;
        this.genre=genre;

        this.name = name;
        this.price=price;
        this.desc=price;
        this.contact=contact;
        this.location=location;
        this.username=username;
        this.created=created;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image= image;
    }




    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return
                "ID: " +id+
                "Title: "+title+
                "Image: " + image+
                "Release Year: "+ releaseYear+
                "Rating: "+rating+
                "Genre: " + genre;
    }

    @Override
    public int describeContents() {
        L.m("Describe Contents  Movie");
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        L.m("writeToParcel Movie");
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(image);
        parcel.writeString(genre);
        parcel.writeString(rating);
    }

    public static final Parcelable.Creator<Soko> CREATOR
            = new Parcelable.Creator<Soko>() {
        public Soko createFromParcel(Parcel in) {
            L.m("create from parcel : Movie");
            return new Soko(in);
        }

        public Soko[] newArray(int size) {
            return new Soko[size];
        }
    };

}
