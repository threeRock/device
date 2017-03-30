package io.jianxun.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QAbstractBaseEntity is a Querydsl query type for AbstractBaseEntity
 */
@Generated("com.querydsl.codegen.SupertypeSerializer")
public class QAbstractBaseEntity extends EntityPathBase<AbstractBaseEntity> {

    private static final long serialVersionUID = 1507271119L;

    public static final QAbstractBaseEntity abstractBaseEntity = new QAbstractBaseEntity("abstractBaseEntity");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> version = createNumber("version", Long.class);

    public QAbstractBaseEntity(String variable) {
        super(AbstractBaseEntity.class, forVariable(variable));
    }

    public QAbstractBaseEntity(Path<? extends AbstractBaseEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAbstractBaseEntity(PathMetadata metadata) {
        super(AbstractBaseEntity.class, metadata);
    }

}

