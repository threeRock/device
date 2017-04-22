package io.jianxun.domain.business;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDepart is a Querydsl query type for Depart
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QDepart extends EntityPathBase<Depart> {

    private static final long serialVersionUID = 1417884361L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDepart depart = new QDepart("depart");

    public final io.jianxun.domain.QAbstractBusinessEntity _super;

    //inherited
    public final BooleanPath active;

    // inherited
    public final QUser createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate;

    //inherited
    public final NumberPath<Long> id;

    // inherited
    public final QUser lastModifieBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate;

    public final StringPath levelCode = createString("levelCode");

    public final StringPath name = createString("name");

    public final QDepart parent;

    //inherited
    public final NumberPath<Long> version;

    public QDepart(String variable) {
        this(Depart.class, forVariable(variable), INITS);
    }

    public QDepart(Path<? extends Depart> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDepart(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDepart(PathMetadata metadata, PathInits inits) {
        this(Depart.class, metadata, inits);
    }

    public QDepart(Class<? extends Depart> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new io.jianxun.domain.QAbstractBusinessEntity(type, metadata, inits);
        this.active = _super.active;
        this.createdBy = _super.createdBy;
        this.createdDate = _super.createdDate;
        this.id = _super.id;
        this.lastModifieBy = _super.lastModifieBy;
        this.lastModifiedDate = _super.lastModifiedDate;
        this.parent = inits.isInitialized("parent") ? new QDepart(forProperty("parent"), inits.get("parent")) : null;
        this.version = _super.version;
    }

}

