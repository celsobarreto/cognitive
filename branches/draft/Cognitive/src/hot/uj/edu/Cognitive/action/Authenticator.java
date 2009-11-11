package uj.edu.Cognitive.action;

import javax.ejb.Local;

@Local
public interface Authenticator {

    boolean authenticate();

}
