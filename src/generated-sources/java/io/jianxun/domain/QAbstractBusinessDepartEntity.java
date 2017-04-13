package io.jianxun.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAbstractBusinessDepartEntity is a Querydsl query type for AbstractBusinessDepartEntity
 */
@Generated("com.querydsl.codegen.SupertypeSerializer")
public class QAbstractBusinessDepartEntity extends EntityPathBase<AbstractBusinessDepartEntity> {

    private static final long serialVersionUID = -2056650030L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAbstractBusinessDepartEntity abstractBusinessDepartEntity = new QAbstractBusinessDepartEntity("abstractBusinessDepartEntity");

    public final QAbstractBusinessEntity _super;

    //inherited
    public final BooleanPath active;

    // inherited
    public final io.jianxun.domain.business.QUser createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate;

    public final io.jianxun.domain.business.QDepart depart;

    //inherited
    public final NumberPath<Long> id;

    // inherited
    public final io.jianxun.domain.business.QUser lastModifieBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate;

    //inherited
    public final NumberPath<Long> version;

    public QAbstractBusinessDepartEntity(String variable) {
        this(AbstractBusinessDepartEntity.class, forVariable(variable), INITS);
    }

    public QAbstractBusinessDepartEntity(Path<? extends AbstractBusinessDepartEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAbstractBusinessDepartEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAbstractBusinessDepartEntity(PathMetadata metadata, PathInits inits) {
        this(AbstractBusinessDepartEntity.class, metadata, inits);
    }

    public QAbstractBusinessDepartEntity(Class<? extends AbstractBusinessDepartEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QAbstractBusinessEntity(type, metadata, inits);
        this.active = _super.active;
        this.createdBy = _super.createdBy;
        this.createdDate = _super.createdDate;
        this.depart = inits.isInitialized("depart") ? new io.jianxun.domain.business.QDepart(forProperty("depart"), inits.get("depart")) : null;
        this.id = _super.id;
        this.lastModifieBy = _super.lastModifieBy;
        this.lastModifiedDate = _super.lastModifiedDate;
        this.version = _super.version;
    }

}

