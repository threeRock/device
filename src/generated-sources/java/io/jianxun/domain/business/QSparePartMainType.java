package io.jianxun.domain.business;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSparePartMainType is a Querydsl query type for SparePartMainType
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSparePartMainType extends EntityPathBase<SparePartMainType> {

    private static final long serialVersionUID = -476982232L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSparePartMainType sparePartMainType = new QSparePartMainType("sparePartMainType");

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

    public final StringPath name = createString("name");

    //inherited
    public final NumberPath<Long> version;

    public QSparePartMainType(String variable) {
        this(SparePartMainType.class, forVariable(variable), INITS);
    }

    public QSparePartMainType(Path<? extends SparePartMainType> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSparePartMainType(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSparePartMainType(PathMetadata metadata, PathInits inits) {
        this(SparePartMainType.class, metadata, inits);
    }

    public QSparePartMainType(Class<? extends SparePartMainType> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new io.jianxun.domain.QAbstractBusinessEntity(type, metadata, inits);
        this.active = _super.active;
        this.createdBy = _super.createdBy;
        this.createdDate = _super.createdDate;
        this.id = _super.id;
        this.lastModifieBy = _super.lastModifieBy;
        this.lastModifiedDate = _super.lastModifiedDate;
        this.version = _super.version;
    }

}

