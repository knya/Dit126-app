/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webbapp.webapp.model;

import com.webbapp.webapp.util.IncorrectPasswordException;
import com.webbapp.webapp.util.MultipleUsersFoundException;
import com.webbapp.webapp.util.UserNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;


@Stateless
public class AppUserFacade extends AbstractFacade<AppUserEntity> {

    @PersistenceContext(unitName = "NewPersistenceUnit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AppUserFacade() {
        super(AppUserEntity.class);
    }
    
    public AppUserEntity login(String username, String password) throws
            UserNotFoundException,
            MultipleUsersFoundException,
            IncorrectPasswordException {
        TypedQuery<AppUserEntity> q = em.createNamedQuery("app_user.findUsername", AppUserEntity.class);
        q.setParameter("userName", username);

        List<AppUserEntity> result = q.getResultList();
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
