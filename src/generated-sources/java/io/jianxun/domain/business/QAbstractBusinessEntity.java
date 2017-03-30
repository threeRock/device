package io.jianxun.domain.business;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAbstractBusinessEntity is a Querydsl query type for AbstractBusinessEntity
 */
@Generated("com.querydsl.codegen.SupertypeSerializer")
public class QAbstractBusinessEntity extends EntityPathBase<AbstractBusinessEntity> {

    private static final long serialVersionUID = -864667302L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAbstractBusinessEntity abstractBusinessEntity = new QAbstractBusinessEntity("abstractBusinessEntity");

    public final io.jianxun.domain.QAbstractBaseAuditableEntity _super;

    public final BooleanPath active = createBoolean("active");

    // inherited
    public final io.jianxun.domain.business.user.QUser createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate;

    //inherited
    public final NumberPath<Long> id;

    // inherited
    public final io.jianxun.domain.business.user.QUser lastModifieBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate;

    //inherited
    public final NumberPath<Long> version;

    public QAbstractBusinessEntity(String variable) {
        this(AbstractBusinessEntity.class, forVariable(variable), INITS);
    }

    public QAbstractBusinessEntity(Path<? extends AbstractBusinessEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAbstractBusinessEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAbstractBusinessEntity(PathMetadata metadata, PathInits inits) {
        this(AbstractBusinessEntity.class, metadata, inits);
    }

    public QAbstractBusinessEntity(Class<? extends AbstractBusinessEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new io.jianxun.domain.QAbstractBaseAuditableEntity(type, metadata, inits);
        this.createdBy = _super.createdBy;
        this.createdDate = _super.createdDate;
        this.id = _super.id;
        this.lastModifieBy = _super.lastModifieBy;
        this.lastModifiedDate = _super.lastModifiedDate;
        this.version = _super.version;
    }

}

