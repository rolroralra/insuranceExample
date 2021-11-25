package com.example.demo.domain.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class MockRepository<T extends CommonEntity> implements Repository<T> {
    private static final int DEFAULT_INIT_SIZE = 20;
    protected List<T> entityList = new ArrayList<>(DEFAULT_INIT_SIZE);

    protected MockRepository() {
        this(DEFAULT_INIT_SIZE);
    }

    protected MockRepository(int size) {
        initMockRepository(size);
    }

    @Override
    public List<T> findAll() {
        return entityList;
    }

    @Override
    public List<T> findByPredicate(Predicate<T> predicate) {
        return entityList.stream().filter(predicate).collect(Collectors.toList());
    }

    @Override
    public T findById(Long entityId) {
        return entityList.stream().filter(entity -> entityId.equals(entity.getId())).findFirst().orElse(null);
    }

    @Override
    public T save(T entity) {
        if (Objects.nonNull(entity.getId())) {
            return modify(entity);
        }
        
        initEntity(entity);
        entityList.add(entity);
        return entity;
    }

    public void deleteById(Long entityId) {
        entityList.removeIf(entity -> Objects.equals(entity.getId(), entityId));
    }
    
    public T modify(T entity) {
        for (int i = 0; i < entityList.size(); i++) {
            if (entityList.get(i).isEqualId(entity)) {
                entityList.set(i, entity);
                entity.setUpdatedAt(LocalDateTime.now());
                return entity;
            }
        }

        return null;
    }

    @Override
    public T findAny() {
        List<T> entityList = findAll();
        return entityList.get(new Random().nextInt(entityList.size()));
    }

    @SuppressWarnings("unchecked")
    protected T newEntity() {
        Class<T> actualTypeArgument = (Class<T>)
                ((ParameterizedType) getClass().getGenericSuperclass())
                        .getActualTypeArguments()[0];

        return Arrays.stream(actualTypeArgument.getDeclaredConstructors()).filter(constructor -> constructor.getParameterCount() == 0).map(constructor -> {
            try {
                T newEntity = (T) constructor.newInstance();
                initEntity(newEntity);
                return newEntity;
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

            return null;
        }).findFirst().orElse(null);
    }

    protected Long newEntityId() {
        return (long) entityList.size() + 1;
    }

    private void initMockRepository(int size) {
        for (int i = 0; i < size; i++) {
            entityList.add(newEntity());
        }
    }

    private void initEntity(T entity) {
        entity.setId(newEntityId());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
    }
}
