package com.essalud.sispoi.service.impl;

import java.util.List;

import com.essalud.sispoi.repo._IGenericRepo;
import com.essalud.sispoi.service._ICRUD;

public abstract class _CRUDImpl<T, ID> implements _ICRUD<T, ID> {

    protected abstract _IGenericRepo<T, ID> getRepo();

    @Override
    public T save(T t) {
        return getRepo().save(t);
    }

    @Override
    public T update(T t) {
        return getRepo().save(t);
    }

    @Override
    public T findById(ID id) {
        return getRepo().findById(id).orElse(null);
    }

    @Override
    public List<T> findAll() {
        return getRepo().findAll();
    }

    @Override
    public void delete(ID id) {
        getRepo().deleteById(id);
    }

}
