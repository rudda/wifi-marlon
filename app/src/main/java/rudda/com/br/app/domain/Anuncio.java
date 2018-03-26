package rudda.com.br.app.domain;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.UUID;

/**
 * Created by Rudda Beltrao on 25/03/2018.
 */

@IgnoreExtraProperties
public class Anuncio {

    private String id = UUID.randomUUID().toString();

    private String name;
    private String description;
    private String photo;

    private AccessPoint ap;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AccessPoint getAp() {
        return ap;
    }

    public void setAp(AccessPoint ap) {
        this.ap = ap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
