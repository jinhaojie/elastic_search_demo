import io.searchbox.annotations.JestId;

import java.util.Date;

/**
 * Created by jhj on 17-10-16.
 */
public class UserDO {
    @JestId
    private Integer id;

    private Date birth;

    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
