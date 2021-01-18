package com.qbrainx_recruitment.util;

import com.qbrainx_recruitment.dto.AuditModelDto;
import com.qbrainx_recruitment.model.AuditModel;
import lombok.experimental.UtilityClass;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

@UtilityClass
public class CustomConverters {

    public <V extends AuditModelDto, T extends AuditModel> ModelConverters<V, T> converter(final DozerBeanMapper mapper,
                                                                                          final Class<V> dtoClass,
                                                                                          final Class<T> entityClass) {

        return new GenericConverter<>(mapper, dtoClass, entityClass);
    }

    private static class GenericConverter<V extends AuditModelDto, T extends AuditModel>
            extends ModelConverters<V, T> {

        private GenericConverter(final Mapper mapper, final Class<V> dtoClazz, final Class<T> entityClazz) {
            super(mapper, dtoClazz, entityClazz);
        }
    }
}
