package edu.uj.cognitive.model;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.Email;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;
import org.hibernate.validator.Pattern;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.security.management.UserEnabled;
import org.jboss.seam.annotations.security.management.UserPassword;
import org.jboss.seam.annotations.security.management.UserPrincipal;
import org.jboss.seam.annotations.security.management.UserRoles;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.digest.DigestUtils;

@Entity
@Name("user")
@Scope(ScopeType.SESSION)
@Table(name = "users")
@SequenceGenerator(name = "user_seq", sequenceName = "users_seq", allocationSize = 1, initialValue = 10)
public class User implements Serializable
{
	private static final long	serialVersionUID	= 4996144773844174425L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
	private Integer				id;

	@NotNull
	@Length(min = 3, max = 255)
	@UserPrincipal
	private String				fullName;

	@ManyToMany
	private List<Publication>	publications;
	
	@ManyToMany(fetch=FetchType.EAGER)
	private List<ScienceDomain> scienceDomains;
	
	@NotNull
	@UserPassword(hash = "md5")
	private String passwordHash;

	@NotNull
	@Email
	private String email; // acts as a login
	
	private String competences;
	
	private String allowedIPs; // comma-delimited IPs or null if any IP is allowed

	@NotNull
	private Boolean accepted;

	@NotNull
	private Boolean emailConfirmed;	

	public User() {
		
	}

	public User(String fullname, String email, String password) {
		this(fullname, email, password, false, false);
	}
	
	public User(String fullname, String email, String password, boolean emailConfirmed, boolean accepted) {
		this.fullName = fullname;
		this.email = email;
		this.setPassword(password);
		this.accepted = accepted;
		this.emailConfirmed = emailConfirmed;
	}
	
	public Boolean getEmailConfirmed() {
		return emailConfirmed;
	}

	public void setEmailConfirmed(Boolean emailConfirmed) {
		this.emailConfirmed = emailConfirmed;
	}

	public void setPassword(String password) {
		this.passwordHash = DigestUtils.md5Hex(password);
	}

	public boolean hasPassword(String password) {
		return this.passwordHash.equals(DigestUtils.md5Hex(password));		
	}

	public Boolean getAccepted() {
		return accepted;
	}

	public void setAccepted(Boolean accepted) {
		this.accepted = accepted;
	}

	public Boolean getActivated() {
		return getAccepted() && getEmailConfirmed();
	}


	@UserRoles
	@ManyToMany(fetch=FetchType.EAGER)
	private Set<Role> roles;

	private String activationToken;
	

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCompetences() {
		return competences;
	}

	public void setCompetences(String competences) {
		this.competences = competences;
	}

	public String getAllowedIPs() {
		return allowedIPs;
	}

	public void setAllowedIPs(String allowedIPs) {
		this.allowedIPs = allowedIPs;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Integer getId()
	{
		return this.id;
	}

	public void setPublications(List<Publication> publications)
	{
		this.publications = publications;
	}

	public List<Publication> getPublications()
	{
		return this.publications;
	}

	public void setFullName(String fullName)
	{
		this.fullName = fullName;
	}

	public String getFullName()
	{
		return this.fullName;
	}

	public void setScienceDomains(List<ScienceDomain> scienceDomains) {
		this.scienceDomains = scienceDomains;
	}

	public List<ScienceDomain> getScienceDomains() {
		return scienceDomains;
	}

	public void setActivationToken(String string) {
		activationToken = string;
		
	}
	
	
	public String getActivationToken() {
		return activationToken;
	}

	public String getRolesList()
	{
		StringBuilder sb = new StringBuilder("");
		for (Role r : this.roles)
		{
			sb.append(r.getName() + ", ");
		}
		String res = sb.toString();
		if (res.length() < 1)
			return "";
		return res.substring(0, res.lastIndexOf(","));
	}
}
