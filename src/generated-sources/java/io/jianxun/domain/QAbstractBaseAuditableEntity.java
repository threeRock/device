package io.jianxun.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAbstractBaseAuditableEntity is a Querydsl query type for AbstractBaseAuditableEntity
 */
@Generated("com.querydsl.codegen.SupertypeSerializer")
public class QAbstractBaseAuditableEntity extends EntityPathBase<AbstractBaseAuditableEntity> {

    private static final long serialVersionUID = -1382421620L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAbstractBaseAuditableEntity abstractBaseAuditableEntity = new QAbstractBaseAuditableEntity("abstractBaseAuditableEntity");

    public final QAbstractBaseEntity _super = new QAbstractBaseEntity(this);

    public final io.jianxun.domain.business.QUser createdBy;

    public final DateTimePath<java.time.LocalDateTime> createdDate = createDateTime("createdDate", java.time.LocalDateTime.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final io.jianxun.domain.business.QUser lastModifieBy;

    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = createDateTime("lastModifiedDate", java.time.LocalDateTime.class);

    //inherited
    public final NumberPath<Long> version = _super.version;

    public QAbstractBaseAuditableEntity(String variable) {
        this(AbstractBaseAuditableEntity.class, forVariable(variable), INITS);
    }

    public QAbstractBaseAuditableEntity(Path<? extends AbstractBaseAuditableEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAbstractBaseAuditableEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAbstractBaseAuditableEntity(PathMetadata metadata, PathInits inits) {
        this(AbstractBaseAuditableEntity.class, metadata, inits);
    }

    public QAbstractBaseAuditableEntity(Class<? extends AbstractBaseAuditableEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.createdBy = inits.isInitialized("createdBy") ? new io.jianxun.domain.business.QUser(forProperty("createdBy"), inits.get("createdBy")) : null;
        this.lastModifieBy = inits.isInitialized("lastModifieBy") ? new io.jianxun.domain.business.QUser(forProperty("lastModifieBy"), inits.get("lastModifieBy")) : null;
    }

}

