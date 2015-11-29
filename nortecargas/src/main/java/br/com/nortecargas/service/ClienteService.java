package br.com.nortecargas.service;

import br.com.nortecargas.model.Cliente;
import br.com.nortecargas.service.AbstractPersistence;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class ClienteService
extends AbstractPersistence<Cliente, Long> {
    
    @PersistenceContext
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return this.em;
    }

    public ClienteService() {
        super(Cliente.class);
    }
}
