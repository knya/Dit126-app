package com.webbapp.webapp.model.facade;

import com.webbapp.webapp.model.entity.AppUserEntity;
import com.webbapp.webapp.util.exception.*;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * facade that use BCrypt to resolve if the user's credentials
 * are correct.
 */
@Stateless
public class LoginFacade extends AbstractFacade<AppUserEntity> {

    @PersistenceContext(unitName = "NewPersistenceUnit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LoginFacade() {
        super(AppUserEntity.class);
    }

    public List<AppUserEntity> findUsername(String username) {
        TypedQuery<AppUserEntity> q = em.createNamedQuery("app_user.findUsername", AppUserEntity.class);
        q.setParameter("userName", username);
        return q.getResultList();
    }
    
    public AppUserEntity login(String username, String password) throws
            UserNotFoundException,
            MultipleUsersFoundException,
            IncorrectPasswordException {

        List<AppUserEntity> result = this.findUsername(username);

        if (result.size() == 0) {
            throw new UserNotFoundException();
        } else if (result.size() > 1) {
            throw new MultipleUsersFoundException();
        }

        if (BCrypt.checkpw(password, result.get(0).getUserPassword())) {
            return result.get(0);
        } else {
            throw new IncorrectPasswordException();
        }
    }
    
}