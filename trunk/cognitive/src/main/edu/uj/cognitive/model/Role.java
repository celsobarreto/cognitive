package edu.uj.cognitive.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;
import javax.persistence.Version;
import org.hibernate.validator.Length;
import org.jboss.seam.annotations.security.management.RoleName;

@Entity
@Table(name = "roles")
public class Role implements Serializable
{
	public final static String ADMIN_ROLE = "admin";
	public final static String SCIENTIST_ROLE = "scientist";
	public final static String ENTREPRENEUR_ROLE = "entrepreneur";
	
    private Integer id;

    private String name;

    @Id @GeneratedValue
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @RoleName
    @Length(max = 20)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
