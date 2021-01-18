package com.qbrainx_recruitment.util;

import com.qbrainx_recruitment.dto.AuditModelDto;
import com.qbrainx_recruitment.model.AuditModel;
import lombok.AllArgsConstructor;
import org.dozer.Mapper;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public abstract class  ModelConverters<V extends AuditModelDto, T extends AuditModel> {

    private final Mapper mapper;
    private final Class<V> dtoClazz;
    private final Class<T> entityClazz;
    
    public V convertEntityToVo(final T t) {
        return mapEntityToVo(mapper.map(t, dtoClazz), t);
    }
    
    public T convertVoToEntity(final V v) {
        return mapVoToEntity(mapper.map(v, entityClazz), v);
    }

    public T updateEntityFromVo(final T t, final V v) {
        mapper.map(v, mapVoToEntity(t, v));
        return t;
    }

    public V mapEntityToVo(final V v, final T t) {
        return v;
    }

    public T mapVoToEntity(final T t, final V v) {
        return t;
    }

    public List<V> convertEntityToVo(final Collection<T> tList) {
        return tList.stream().map(this::convertEntityToVo).collect(Collectors.toList());
    }

    public List<T> convertVoToEntity(final Collection<V> vList) {
        return vList.stream().map(this::convertVoToEntity).collect(Collectors.toList());
    }


}
