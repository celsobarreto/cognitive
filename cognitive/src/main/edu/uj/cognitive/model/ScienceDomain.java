package edu.uj.cognitive.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;
import javax.persistence.Version;
import org.hibernate.validator.Length;


@Entity
public class ScienceDomain implements Serializable
{
    /**
	 * 
	 */
	private static final long	serialVersionUID	= 6772441454695425796L;
	// seam-gen attributes (you should probably edit these)
    private Long id;
    private Integer version;
    private String name;
    private String description;
    private Boolean isDefault;
    private List<User> users;

    private Boolean selected;

    // add additional entity attributes

    // seam-gen attribute getters/setters with annotations (you probably should edit)

    @Id @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Version
    public Integer getVersion() {
        return version;
    }

    private void setVersion(Integer version) {
        this.version = version;
    }

    @Length(max = 20)
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

	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}
	
	@ManyToMany(mappedBy="scienceDomains")
	public List<User> getUsers() {
		return users;
	}
	
	public void setUsers(List<User> users) {
		this.users = users;
	}

	@Transient
	public Boolean getSelected(){
		return selected;
	}

	public void setSelected(Boolean selected){
		this.selected = selected;
	}

}
