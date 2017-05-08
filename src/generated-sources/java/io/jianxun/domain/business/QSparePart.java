package io.jianxun.domain.business;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSparePart is a Querydsl query type for SparePart
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QSparePart extends EntityPathBase<SparePart> {

    private static final long serialVersionUID = -115265419L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSparePart sparePart = new QSparePart("sparePart");

    public final io.jianxun.domain.QAbstractBusinessDepartEntity _super;

    //inherited
    public final BooleanPath active;

    // inherited
    public final QUser createdBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate;

    // inherited
    public final QDepart depart;

    public final StringPath description = createString("description");

    public final QDevice device;

    //inherited
    public final NumberPath<Long> id;

    // inherited
    public final QUser lastModifieBy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate;

    public final StringPath location = createString("location");

    public final StringPath mainPic = createString("mainPic");

    public final StringPath manufacturer = createString("manufacturer");

    public final StringPath name = createString("name");

    public final StringPath partcode = createString("partcode");

    public final StringPath partnumber = createString("partnumber");

    public final StringPath shelf = createString("shelf");

    public final StringPath specification = createString("specification");

    public final QStorehouse storehouse;

    public final QSparePartSubType subType;

    //inherited
    public final NumberPath<Long> version;

    public QSparePart(String variable) {
        this(SparePart.class, forVariable(variable), INITS);
    }

    public QSparePart(Path<? extends SparePart> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSparePart(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSparePart(PathMetadata metadata, PathInits inits) {
        this(SparePart.class, metadata, inits);
    }

    public QSparePart(Class<? extends SparePart> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new io.jianxun.domain.QAbstractBusinessDepartEntity(type, metadata, inits);
        this.active = _super.active;
        this.createdBy = _super.createdBy;
        this.createdDate = _super.createdDate;
        this.depart = _super.depart;
        this.device = inits.isInitialized("device") ? new QDevice(forProperty("device"), inits.get("device")) : null;
        this.id = _super.id;
        this.lastModifieBy = _super.lastModifieBy;
        this.lastModifiedDate = _super.lastModifiedDate;
        this.storehouse = inits.isInitialized("storehouse") ? new QStorehouse(forProperty("storehouse"), inits.get("storehouse")) : null;
        this.subType = inits.isInitialized("subType") ? new QSparePartSubType(forProperty("subType"), inits.get("subType")) : null;
        this.version = _super.version;
    }

}

