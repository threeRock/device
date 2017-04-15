package io.jianxun.domain.business;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSparePartSubType is a Querydsl query type for SparePartSubType
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSparePartSubType extends EntityPathBase<SparePartSubType> {

    private static final long serialVersionUID = 1441500005L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSparePartSubType sparePartSubType = new QSparePartSubType("sparePartSubType");

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

    public final QSparePartMainType mainType;

    public final StringPath name = createString("name");

    //inherited
    public final NumberPath<Long> version;

    public QSparePartSubType(String variable) {
        this(SparePartSubType.class, forVariable(variable), INITS);
    }

    public QSparePartSubType(Path<? extends SparePartSubType> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSparePartSubType(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSparePartSubType(PathMetadata metadata, PathInits inits) {
        this(SparePartSubType.class, metadata, inits);
    }

    public QSparePartSubType(Class<? extends SparePartSubType> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new io.jianxun.domain.QAbstractBusinessEntity(type, metadata, inits);
        this.active = _super.active;
        this.createdBy = _super.createdBy;
        this.createdDate = _super.createdDate;
        this.id = _super.id;
        this.lastModifieBy = _super.lastModifieBy;
        this.lastModifiedDate = _super.lastModifiedDate;
        this.mainType = inits.isInitialized("mainType") ? new QSparePartMainType(forProperty("mainType"), inits.get("mainType")) : null;
        this.version = _super.version;
    }

}

