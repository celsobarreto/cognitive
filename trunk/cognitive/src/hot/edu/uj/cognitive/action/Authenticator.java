package edu.uj.cognitive.action;

import javax.ejb.Local;

@Local
public interface Authenticator {

    boolean authenticate();

}
